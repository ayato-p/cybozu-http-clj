(ns cybozu-http.kintone.api.app
  (:refer-clojure :exclude [get])
  (:require [cybozu-http.kintone.api.internal.app :as internal])
  (:import cybozu_http.kintone.api.Boundary))

(defprotocol AppAPI
  (get
    [auth app-id]
    [auth app-id opts]))

(extend-protocol AppAPI
  cybozu_http.kintone.api.Boundary
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
  cybozu_http.kintone.api.Boundary
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
  cybozu_http.kintone.api.Boundary
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
  cybozu_http.kintone.api.Boundary
  (get-layout
    ([auth app-id]
     (internal/get-layout auth app-id))
    ([auth app-id opts]
     (internal/get-layout auth app-id opts))))
