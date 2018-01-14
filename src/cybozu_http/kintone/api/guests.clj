(ns cybozu-http.kintone.api.guests
  (:require [cybozu-http.kintone.api.internal.guests :as internal])
  (:import cybozu_http.kintone.api.Boundary))

(defprotocol GuestsAPI
  (post
    [auth guests]
    [auth guests opts])
  (delete
    [auth guests]
    [auth guests opts]))

(extend-protocol GuestsAPI
  cybozu_http.kintone.api.Boundary
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
