(ns cybozu-http.api.kintone.bare
  (:require [camel-snake-kebab.core :as csk]
            [cheshire.core :as c]
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

(defn api-call [auth method api-url params & opts]
  (let [f (case method
            :get    cli/get
            :post   cli/post
            :put    cli/put
            :delete cli/delete)
        url (generate-url auth api-url (:guest-space-id opts))
        headers (auth-headers auth)]
    (f url (merge headers params))))

(defmulti build-params (fn [method params] method))

(defmethod build-params :default [_ params]
  {:form-params params
   :content-type :json})

(defmethod build-params :get [_ params]
  {:query-params params})

(defmacro defapi
  ([fn-name method api-url argsv]
   `(defapi ~fn-name ~method ~api-url ~argsv nil))

  ([fn-name method api-url argsv opt-argsv]
   (let [fn-name* (symbol (str fn-name "*"))
         arglists (-> (into ['auth] argsv)
                      (conj '& {:keys opt-argsv :as 'opts})
                      list)
         opt-arg-keys (map keyword opt-argsv)]
     `(do
        (defn ~fn-name* [~'auth ~@argsv & {:keys ~opt-argsv :as ~'opts}]
          (let [params# (assoc {} ~@(mapcat vector (map csk/->camelCaseKeyword argsv) argsv))
                params# (reduce (fn [m# [k# v#]] (cond-> m# v# (assoc k# v#)))
                                params#
                                (->> (interleave '~(map csk/->camelCaseKeyword opt-arg-keys)
                                                 ((juxt ~@opt-arg-keys) ~'opts))
                                     (partition 2)))
                params# (build-params ~method params#)
                ~'opts (dissoc ~'opts ~@opt-arg-keys)]
            (apply api-call ~'auth ~method ~api-url params# ~'opts)))

        (def ~(vary-meta fn-name merge `{:arglists '~arglists})
          (comp #(c/parse-string % true) :body ~fn-name*))))))
