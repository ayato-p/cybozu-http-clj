(ns cybozu-http.kintone.api.thread
  (:require [cybozu-http.kintone.api.internal.thread :as internal])
  (:import cybozu_http.kintone.api.Boundary))

(defprotocol ThreadAPI
  (put
    [auth thread-id]
    [auth thread-id opts])
  (post-comment
    [auth space-id thread-id comment]
    [auth space-id thread-id comment opts]))

(extend-protocol ThreadAPI
  cybozu_http.kintone.api.Boundary
  (put
    ([auth thread-id]
     (internal/put auth thread-id))
    ([auth thread-id opts]
     (internal/put auth thread-id opts)))
  (post-comment
    ([auth space-id thread-id comment]
     (internal/post-comment auth space-id thread-id comment))
    ([auth space-id thread-id comment opts]
     (internal/post-comment auth space-id thread-id comment opts))))
