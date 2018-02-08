(ns cybozu-http.kintone.api.record
  (:refer-clojure :exclude [get])
  (:require [cybozu-http.kintone.api.internal.record :as internal]))

(defprotocol RecordAPI
  (get
    [auth app-id id]
    [auth app-id id opts])
  (post
    [auth app-id]
    [auth app-id opts])
  (put
    [auth app-id]
    [auth app-id opts]))

(extend-protocol RecordAPI
  clojure.lang.Associative
  (get
    ([auth app-id id]
     (internal/get auth app-id id))
    ([auth app-id id opts]
     (internal/get auth app-id id opts)))
  (post
    ([auth app-id]
     (internal/post auth app-id))
    ([auth app-id opts]
     (internal/post auth app-id opts)))
  (put
    ([auth app-id]
     (internal/put auth app-id))
    ([auth app-id opts]
     (internal/put auth app-id opts))))

(defprotocol RecordProcessAPI
  (put-assignees
    [auth app-id record-id assignees]
    [auth app-id record-id assignees opts])
  (put-status
    [auth app-id record-id action]
    [auth app-id record-id action opts]))

(extend-protocol RecordProcessAPI
  clojure.lang.Associative
  (put-assignees
    ([auth app-id record-id assignees]
     (internal/put-assignees auth app-id record-id assignees))
    ([auth app-id record-id assignees opts]
     (internal/put-assignees auth app-id record-id assignees opts)))
  (put-status
    ([auth app-id record-id action]
     (internal/put-status auth app-id record-id action))
    ([auth app-id record-id action opts]
     (internal/put-status auth app-id record-id action opts))))

(defprotocol RecordCommentAPI
  (get-comments
    [auth app-id record-id]
    [auth app-id record-id opts])
  (post-comment
    [auth app-id record-id comment]
    [auth app-id record-id comment opts])
  (delete-comment
    [auth app-id record-id comment-id]
    [auth app-id record-id comment-id opts]))

(extend-protocol RecordCommentAPI
  clojure.lang.Associative
  (get-comments
    ([auth app-id record-id]
     (internal/get-comments auth app-id record-id))
    ([auth app-id record-id opts]
     (internal/get-comments auth app-id record-id opts)))
  (post-comment
    ([auth app-id record-id comment]
     (internal/post-comment auth app-id record-id comment))
    ([auth app-id record-id comment opts]
     (internal/post-comment auth app-id record-id comment opts)))
  (delete-comment
    ([auth app-id record-id comment-id]
     (internal/delete-comment auth app-id record-id comment-id))
    ([auth app-id record-id comment-id opts]
     (internal/delete-comment auth app-id record-id comment-id opts))))
