(ns cybozu-http.api.kintone.bare
  (:require [cheshire.core :as c]
            [clj-http.client :as cli])
  (:import java.util.Base64))

(def protocol "https://")

(def api-prefix "/k/v1")
(def guest-space-prefix "/k/guest/")

(defn generate-url
  [{:keys [domain subdomain] :as auth :or {domain "cybozu.com"}} api-url guest-space-id]
  (let [base-url (str protocol subdomain "." domain)]
    (-> (if guest-space-id
          (str base-url guest-space-id "/v1")
          (str base-url api-prefix))
        (str api-url))))

(defn- base64-encode [str]
  (let [encoder (Base64/getEncoder)]
    (.encodeToString encoder (.getBytes str))))

(defn auth-headers
  [{:keys [login-name password api-token] :as auth}]
  (cond-> {:headers {}}
    (and (seq login-name) (seq password))
    (assoc-in [:headers :X-Cybozu-Authorization] (base64-encode (str login-name ":" password)))
    (seq api-token)
    (assoc-in [:headers :X-Cybozu-API-Token] api-token)))

(defn api-call
  ([auth method api-url params]
   (api-call auth method api-url params nil))
  ([auth method api-url params opts]
   (let [f (case method
             :get    cli/get
             :post   cli/post
             :put    cli/put
             :delete cli/delete)
         url (generate-url auth api-url (:guest-space-id opts))
         headers (auth-headers auth)]
     (f url (merge headers params)))))

(defmulti build-params (fn [method params] method))

(defmethod build-params :default [_ params]
  {:form-params params
   :content-type :json})

(defmethod build-params :get [_ params]
  {:query-params params})

(defn- extract-args
  "`[foo :- foo-id, :bar :- :bar-id]`
  ;; -> `[(foo bar), (foo-id bar-id)]`"
  [argsv]
  (let [v (partition 3 argsv)]
    [(map first v) (map #(nth % 2) v)]))

(defmacro defapi
  ([fn-name method api-url argsv]
   `(defapi ~fn-name ~method ~api-url ~argsv nil))

  ([fn-name method api-url argsv opt-argsv]
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
        (defn ~fn-name* [~'auth ~@param-names & ~fn-opts-param]
          (let [params# (assoc {} ~@(interleave actual-param-names param-names))
                params# (reduce (fn [m# [k# v#]] (cond-> m# v# (assoc k# v#)))
                                params#
                                ~(when (seq opt-param-names)
                                   (map vector actual-opt-param-names opt-param-names)))
                params# (build-params ~method params#)
                ~'opts ~(if (seq opt-param-names)
                          `(dissoc ~'opts ~@opt-param-names)
                          'opts)]
            (api-call ~'auth ~method ~api-url params# ~'opts)))

        (def ~(vary-meta fn-name merge `{:arglists '~fn-arglists})
          (comp #(c/parse-string % true) :body ~fn-name*))))))
