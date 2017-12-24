(ns cybozu-http.kintone.api.app
  (:refer-clojure :exclude [get])
  (:require [cybozu-http.kintone.api.internal.app :as internal]))

(defprotocol AppAPI
  (get
    [auth app-id]
    [auth app-id opts]))

(extend-protocol AppAPI
  clojure.lang.IPersistentMap
  (get
    ([auth app-id]
     (internal/get auth app-id))
    ([auth app-id opts]
     (internal/get auth app-id opts))))

(defprotocol AppFormAPI
  (get-form
    [auth app-id]
    [auth app-id opts]))

(extend-protocol AppFormAPI
  clojure.lang.IPersistentMap
  (get-form
    ([auth app-id]
     (internal/get-form auth app-id))
    ([auth app-id opts]
     (internal/get-form auth app-id opts))))

(defprotocol AppFieldsAPI
  (get-fields
    [auth app-id]
    [auth app-id opts]))

(extend-protocol AppFieldsAPI
  clojure.lang.IPersistentMap
  (get-fields
    ([auth app-id]
     (internal/get-fields auth app-id))
    ([auth app-id opts]
     (internal/get-fields auth app-id opts))))

(defprotocol AppLayoutAPI
  (get-layout
    [auth app-id]
    [auth app-id opts]))

(extend-protocol AppLayoutAPI
  clojure.lang.IPersistentMap
  (get-layout
    ([auth app-id]
     (internal/get-layout auth app-id))
    ([auth app-id opts]
     (internal/get-layout auth app-id opts))))
