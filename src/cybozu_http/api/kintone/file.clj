(ns cybozu-http.api.kintone.file
  (:require [cheshire.core :as c]
            [clojure.java.io :as io]
            [cybozu-http.api.kintone.bare :as bare])
  (:import java.nio.file.attribute.FileAttribute
           java.nio.file.Files))

(defn- rename-file [file new-filename]
  (let [parent (Files/createTempDirectory "cybozu-http-clj-" (into-array FileAttribute []))
        new-file (io/file parent new-filename)]
    (io/copy file new-file)
    new-file))

(defn upload* [auth file & {:keys [filename] :as opts}]
  (let [file (cond-> file (seq filename) (rename-file filename))
        params {:multipart [{:name "file" :content file}]}]
    (bare/api-call auth :post "/file.json" params)))

(defn upload [auth file & {:keys [filename]}]
  (-> (upload* auth file :filename filename)
      :body
      (c/parse-string true)
      :fileKey))

(defn download* [auth file-key & {:as opts}]
  (let [params (bare/build-params :get {:fileKey file-key})]
    (bare/api-call auth :get "/file.json" params)))
