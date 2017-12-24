(ns cybozu-http.kintone.api.internal.apps
  (:refer-clojure :exclude [get])
  (:require [cybozu-http.kintone.api.internal.bare :refer [defapi]]))

(defapi get :get "/apps.json"
  []
  [ids      :- app-ids
   codes    :- app-codes
   name     :- name
   spaceIds :- space-ids
   limit    :- limit
   offset   :- offset]
  [:apps])
