(ns cybozu-http.kintone.api.records
  (:refer-clojure :exclude [get])
  (:require [cybozu-http.kintone.api.bare :refer [defapi]]))

(defapi get :get "/records.json"
  [app :- app-id]
  [fields     :- fields
   query      :- query
   totalCount :- total-count])

(defapi post :post "/records.json"
  [app     :- app-id
   records :- records])

(defapi put :put "/records.json"
  [app     :- app-id
   records :- records]
  []
  [:records])

(defapi delete :delete "/records.json"
  [app :- app-id
   ids :- record-ids]
  [revisions :- revisions])
