(ns cybozu-http.api.kintone.guests
  (:require [cybozu-http.api.kintone.bare :refer [defapi]]))

(defapi post :post "/guests.json"
  [guests :- guests])

(defapi delete :delete "/guests.json"
  [guests :- guests])
