(ns cybozu-http.kintone.api.guests
  (:require [cybozu-http.kintone.api.internal.guests :as internal]))

(defprotocol GuestsAPI
  (post
    [auth guests]
    [auth guests opts])
  (delete
    [auth guests]
    [auth guests opts]))

(extend-protocol GuestsAPI
  clojure.lang.IPersistentMap
  (post
    ([auth guests]
     (internal/post auth guests))
    ([auth guests opts]
     (internal/post auth guests opts)))
  (delete
    ([auth guests]
     (internal/delete auth guests))
    ([auth guests opts]
     (internal/delete auth guests opts))))
