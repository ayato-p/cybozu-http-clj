(ns cybozu-http.api.kintone.records
  (:refer-clojure :exclude [get])
  (:require [cybozu-http.api.kintone.bare :refer [defapi]]))

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
