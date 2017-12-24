(ns cybozu-http.kintone.api.internal.app
  (:refer-clojure :exclude [get])
  (:require [cybozu-http.kintone.api.internal.bare :refer [defapi]]))

(defapi get :get "/app.json"
  [id :- app-id])

(defapi get-form :get "/form.json"
  [app :- app-id])

;;; form configurations
;;; doc: https://developer.cybozu.io/hc/ja/articles/204783170
;;;      https://developer.cybozu.io/hc/ja/articles/204529724

(defapi get-fields :get "/app/form/fields.json"
  [app :- app-id]
  [lang :- language])

(defapi get-layout :get "/app/form/layout.json"
  [app    :- app-id])
