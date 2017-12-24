(ns cybozu-http.kintone.api.internal.guests
  (:require [cybozu-http.kintone.api.internal.bare :refer [defapi]]))

(defapi post :post "/guests.json"
  [guests :- guests])

(defapi delete :delete "/guests.json"
  [guests :- guests])
