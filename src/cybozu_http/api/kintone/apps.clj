(ns cybozu-http.api.kintone.apps
  (:refer-clojure :exclude [get])
  (:require [cybozu-http.api.kintone.bare :refer [defapi]]))

(defapi get :get "/apps.json"
  []
  [ids      :- app-ids
   codes    :- app-codes
   name     :- name
   spaceIds :- space-ids
   limit    :- limit
   offset   :- offset])
