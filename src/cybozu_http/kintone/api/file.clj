(ns cybozu-http.kintone.api.file
  (:require [cybozu-http.kintone.api.internal.file :as internal]))

(defprotocol FileAPI
  (upload [auth file] [auth file opts])
  (download [auth file-key] [auth file-key opts]))

(extend-protocol FileAPI
  clojure.lang.Associative
  (upload
    ([auth file]
     (internal/upload auth file))

    ([auth file opts]
     (->> (reduce into [] opts)
          (apply internal/upload auth file))))

  (download
    ([auth file-key]
     (internal/download auth file-key))

    ([auth file-key opts]
     (->> (reduce into [] opts)
          (apply internal/download auth file-key)))))
