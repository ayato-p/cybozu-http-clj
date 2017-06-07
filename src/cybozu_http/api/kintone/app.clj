(ns cybozu-http.api.kintone.app
  (:refer-clojure :exclude [get])
  (:require [cybozu-http.api.kintone.bare :refer [defapi]]))

(defapi get :get "/app.json"
  [id :- app-id])
