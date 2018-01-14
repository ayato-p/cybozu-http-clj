(ns cybozu-http.kintone.api.apps
  (:refer-clojure :exclude [get])
  (:require [cybozu-http.kintone.api.internal.apps :as internal])
  (:import cybozu_http.kintone.api.Boundary))

(defprotocol AppsAPI
  (get [auth] [auth opts]))

(extend-protocol AppsAPI
  cybozu_http.kintone.api.Boundary
  (get
    ([auth]
     (internal/get auth))
    ([auth opts]
     (internal/get auth opts))))
