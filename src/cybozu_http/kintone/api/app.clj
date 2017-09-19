(ns cybozu-http.kintone.api.app
  (:refer-clojure :exclude [get])
  (:require [cybozu-http.kintone.api.bare :refer [defapi]]))

(defapi get :get "/app.json"
  [id :- app-id])
