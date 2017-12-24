(ns cybozu-http.kintone.api.space
  (:refer-clojure :exclude [get])
  (:require [cybozu-http.kintone.api.internal.space :as internal]))

(defprotocol SpaceAPI
  (post
    [auth template-id name members]
    [auth template-id name members opts])
  (get
    [auth space-id]
    [auth space-id opts])
  (delete
    [auth space-id]
    [auth space-id opts])
  (put-body
    [auth space-id body]
    [auth space-id body opts]))

(extend-protocol SpaceAPI
  clojure.lang.IPersistentMap
  (post
    ([auth template-id name members]
     (internal/post auth template-id name members))
    ([auth template-id name members opts]
     (internal/post auth template-id name members opts)))
  (get
    ([auth space-id]
     (internal/get auth space-id))
    ([auth space-id opts]
     (internal/get auth space-id opts)))
  (delete
    ([auth space-id]
     (internal/delete auth space-id))
    ([auth space-id opts]
     (internal/delete auth space-id opts)))
  (put-body
    ([auth space-id body]
     (internal/put-body auth space-id body))
    ([auth space-id body opts]
     (internal/put-body auth space-id body opts))))


(defprotocol SpaceMemberAPI
  (put-members
    [auth space-id members]
    [auth space-id members opts])
  (get-members
    [auth space-id]
    [auth space-id opts])
  (put-guests
    [auth guest-space-id guests]
    [auth guest-space-id guests opts]))

(extend-protocol SpaceMemberAPI
  clojure.lang.IPersistentMap
  (get-members
    ([auth space-id]
     (internal/get-members auth space-id))
    ([auth space-id opts]
     (internal/get-members auth space-id opts)))
  (put-members
    ([auth space-id members]
     (internal/put-members auth space-id members))
    ([auth space-id members opts]
     (internal/put-members auth space-id members opts)))
  (put-guests
    ([auth guest-space-id guests]
     (internal/put-guests auth guest-space-id guests))
    ([auth guest-space-id guests opts]
     (internal/put-guests auth guest-space-id guests opts))))
