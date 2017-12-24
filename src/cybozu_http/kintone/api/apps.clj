(ns cybozu-http.kintone.api.apps
  (:refer-clojure :exclude [get])
  (:require [cybozu-http.kintone.api.internal.apps :as internal]))

(defprotocol AppsAPI
  (get [auth] [auth opts]))

(extend-protocol AppsAPI
  clojure.lang.IPersistentMap
  (get
    ([auth]
     (internal/get auth))
    ([auth opts]
     (internal/get auth opts))))
