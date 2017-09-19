(ns cybozu-http.kintone.api.guests
  (:require [cybozu-http.kintone.api.bare :refer [defapi]]))

(defapi post :post "/guests.json"
  [guests :- guests])

(defapi delete :delete "/guests.json"
  [guests :- guests])
