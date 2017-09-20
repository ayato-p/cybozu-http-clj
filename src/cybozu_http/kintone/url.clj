(ns cybozu-http.kintone.url
  (:require [clojure.string :as str]))

(def domain-list
  ["cybozu.com"
   "cybozu-dev.com"
   "kintone.com"
   "kintone-dev.com"
   "cybozu.cn"
   "cybozu-dev.cn"])

(def re-base-url*
  (str "^https://([a-z0-9][a-z0-9\\-]{1,30}[a-z0-9])(?:\\.s)?\\."
       "("
       (->> (map #(str/replace % "." "\\.") domain-list)
            (str/join "|"))
       ")"))

(def re-base-url
  (re-pattern re-base-url*))

(defn extract-base-url [url]
  (some-> (re-find re-base-url url) first))

(defn valid-base-url? [url]
  (not (str/blank? (extract-base-url url))))

(def re-url
  (re-pattern (str re-base-url* "/k/(\\d++).*")))

(def re-guest-url
  (re-pattern (str re-base-url* "/k/guest/(\\d++)/(\\d++).*")))

(defn parse-app-url [url]
  (or
   (when-let [[_ subdomain domain app-id] (re-matches re-url url)]
     {:domain domain
      :subdomain subdomain
      :app-id app-id})
   (when-let [[_ subdomain domain guest-space-id app-id] (re-matches re-guest-url url)]
     {:domain domain
      :subdomain subdomain
      :guest-space-id guest-space-id
      :app-id app-id})))

(defn valid-app-url? [url]
  (some? (or (re-matches re-url url)
             (re-matches re-guest-url url))))
