(ns cybozu-http.api.kintone.space
  (:refer-clojure :exclude [get])
  (:require [cybozu-http.api.kintone.bare :refer [defapi]]))

(defapi post :post "/template/space.json"
  [id      :- template-id
   name    :- name
   members :- members]
  [isPrivate   :- private?
   isGuest     :- guest?
   fixedMember "-" fixed-member]
  [:id])

(def space-url "/space.json")

(defapi get :get space-url
  [id :- space-id])

(defapi delete :delete space-url
  [id :- space-id])

(defapi put-body :put "/space/body.json"
  [id   :- space-id
   body :- body])

(defapi get-members :get "/space/members.json"
  [id :- space-id]
  []
  [:members])

(defapi put-members :put "/space/members.json"
  [id      :- space-id
   members :- members])

(defapi put-guests :put "/space/guests.json"
  [id     :- guest-space-id
   guests :- guests])
