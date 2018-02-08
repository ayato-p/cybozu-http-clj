(ns cybozu-http.kintone.api.records
  (:refer-clojure :exclude [get])
  (:require [cybozu-http.kintone.api.internal.records :as internal]))

(defprotocol RecordsAPI
  (get
    [auth app-id]
    [auth app-id  opts])
  (post
    [auth app-id records]
    [auth app-id records opts])
  (put
    [auth app-id records]
    [auth app-id records opts])
  (delete
    [auth app-id record-ids]
    [auth app-id record-ids opts]))

(extend-protocol RecordsAPI
  clojure.lang.Associative
  (get
    ([auth app-id]
     (internal/get auth app-id))
    ([auth app-id  opts]
     (internal/get auth app-id opts)))
  (post
    ([auth app-id records]
     (internal/post auth app-id records))
    ([auth app-id records opts]
     (internal/post auth app-id records opts)))
  (put
    ([auth app-id records]
     (internal/put auth app-id records))
    ([auth app-id records opts]
     (internal/put auth app-id records opts)))
  (delete
    ([auth app-id record-ids]
     (internal/delete auth app-id record-ids))
    ([auth app-id record-ids opts]
     (internal/delete auth app-id record-ids opts))))
