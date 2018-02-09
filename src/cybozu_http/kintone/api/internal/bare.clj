(ns cybozu-http.kintone.api.internal.bare
  (:require [cheshire.core :as c]
            [clj-http.client :as cli]
            [cybozu-http.kintone.api :as api]
            [slingshot.slingshot :refer [try+]]
            [clojure.string :as str])
  (:import cybozu_http.kintone.api.Boundary
           java.util.Base64))

(def protocol "https://")

(def api-prefix "/k/v1")
(def guest-space-prefix "/k/guest/")

(defn generate-url*
  [{:keys [domain subdomain] :as auth :or {domain "cybozu.com"}} api-url guest-space-id]
  {:pre [(seq subdomain)]}
  (let [base-url (str protocol subdomain "." domain)]
    (-> (if guest-space-id
          (str base-url guest-space-prefix guest-space-id "/v1")
          (str base-url api-prefix))
        (str api-url))))

(defprotocol GenerateURL
  (generate-url [auth api-url] [auth api-url guest-space-id]))

(extend-protocol GenerateURL
  cybozu_http.kintone.api.Boundary
  (generate-url
    ([auth api-url] (generate-url auth api-url nil))
    ([auth api-url guest-space-id]
     (-> (cond-> auth (str/blank? (:domain auth)) (assoc :domain "cybozu.com"))
         (generate-url* api-url guest-space-id))))

  clojure.lang.IPersistentMap
  (generate-url
    ([auth api-url] (generate-url auth api-url nil))
    ([auth api-url guest-space-id] (generate-url* auth api-url guest-space-id))))

(defn- base64-encode [str]
  (let [encoder (Base64/getEncoder)]
    (.encodeToString encoder (.getBytes str))))

(defn auth-headers
  [{:keys [basic-login-name basic-password login-name password api-token] :as auth}]
  (cond-> {:headers {}}
    (and (seq basic-login-name) (seq basic-password))
    (assoc-in [:headers :Authorization] (str "Basic "(base64-encode (str basic-login-name ":" basic-password))))
    (and (seq login-name) (seq password))
    (assoc-in [:headers :X-Cybozu-Authorization] (base64-encode (str login-name ":" password)))
    (seq api-token)
    (assoc-in [:headers :X-Cybozu-API-Token] api-token)))

(defmulti build-params (fn [method params] method))

(defmethod build-params :default [_ params]
  {:form-params params
   :content-type :json})

(defmethod build-params :get [_ params]
  {:query-params params})

(defn- try-json-parse [json]
  (try
    (c/parse-string json true)
    (catch Exception e
      {:raw json})))

(defn- should-override-http-method?
  "ref. https://developer.cybozu.io/hc/ja/articles/201941754-kintone-REST-API%E3%81%AE%E5%85%B1%E9%80%9A%E4%BB%95%E6%A7%98#step9"
  ([^String url]
   (should-override-http-method? url nil))
  ([^String url ^String query-string]
   (let [ch-cnt (cond-> (alength (.getBytes url))
                  (not (str/blank? query-string))
                  (+ (alength (.getBytes query-string))))]
     (>= ch-cnt 4000))))

(defn api-call
  "opts
  :suppress-build-params bool"
  ([auth method api-url params]
   (api-call auth method api-url params nil))
  ([auth method api-url params {:keys [suppress-build-params] :as opts}]
   (let [url ^String (generate-url auth api-url (:guest-space-id opts))
         params' (if suppress-build-params params (build-params method params))
         should-override? (->> (cli/generate-query-string (:query-params params'))
                               (should-override-http-method? url))
         method' (if should-override? :post method)
         ;; for HTTP method override
         params' (if suppress-build-params params (build-params method' params))
         f (case method'
             :get    cli/get
             :post   cli/post
             :put    cli/put
             :delete cli/delete)
         headers (cond-> (auth-headers auth)
                   should-override?
                   (assoc-in [:headers :X-HTTP-Method-Override] (.toUpperCase (name method))))]
     (try+
      (f url (merge headers params'))
      (catch [:type :clj-http.client/unexceptional-status] {:keys [status body]}
        (throw (ex-info "kintone api error"
                        (-> (try-json-parse body)
                            (assoc :status status)
                            (assoc :request {:url url
                                             :method method'
                                             :headers headers
                                             :params params'})
                            (assoc :type ::api/exception)))))))))

(defn- extract-args
  "`[foo :- foo-id, :bar :- :bar-id]`
  ;; -> `[(foo bar), (foo-id bar-id)]`"
  [argsv]
  (let [v (partition 3 argsv)]
    [(mapv first v) (mapv #(nth % 2) v)]))

(defmacro defapi
  ([fn-name method api-url argsv]
   `(defapi ~fn-name ~method ~api-url ~argsv [] []))

  ([fn-name method api-url argsv opt-argsv]
   `(defapi ~fn-name ~method ~api-url ~argsv ~opt-argsv []))

  ([fn-name method api-url argsv opt-argsv returning-keys]
   (let [fn-name* (symbol (str fn-name "*"))
         [actual-param-names param-names] (extract-args argsv)
         [actual-opt-param-names opt-param-names] (extract-args opt-argsv)
         actual-param-names (map keyword actual-param-names)
         actual-opt-param-names (map keyword actual-opt-param-names)
         fn-opts-param (cond-> {:as 'opts} (seq opt-param-names) (assoc :keys opt-param-names))
         fn-arglists (-> (into ['auth] param-names)
                         (conj '& fn-opts-param)
                         list)]
     `(do
        (defn ~fn-name*
          ([~'auth ~@param-names]
           (~fn-name* ~'auth ~@param-names {}))
          ([~'auth ~@param-names ~fn-opts-param]
           (let [params# ~(when (seq param-names)
                            `(assoc {} ~@(interleave actual-param-names param-names)))
                 params# (reduce (fn [m# [k# v#]] (cond-> m# v# (assoc k# v#)))
                                 params#
                                 ~(when (seq opt-param-names)
                                    (mapv vector actual-opt-param-names opt-param-names)))
                 ~'opts ~(if (seq opt-param-names)
                           `(dissoc ~'opts ~@opt-param-names)
                           'opts)]
             (api-call ~'auth ~method ~api-url params# ~'opts))))

        (defn ~fn-name
          ([~'auth ~@param-names]
           (~fn-name ~'auth ~@param-names {}))
          ([~'auth ~@param-names ~fn-opts-param]
           (-> (~fn-name* ~'auth ~@param-names ~'opts)
               :body
               (c/parse-string true)
               ~(if (seq returning-keys)
                  `(get-in ~returning-keys)
                  identity))))))))
