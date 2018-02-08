(ns cybozu-http.kintone.api.thread
  (:require [cybozu-http.kintone.api.internal.thread :as internal]))

(defprotocol ThreadAPI
  (put
    [auth thread-id]
    [auth thread-id opts])
  (post-comment
    [auth space-id thread-id comment]
    [auth space-id thread-id comment opts]))

(extend-protocol ThreadAPI
  clojure.lang.Associative
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
