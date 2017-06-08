(ns cybozu-http.api.kintone.record
  (:refer-clojure :exclude [get])
  (:require [cybozu-http.api.kintone.bare :refer [defapi]]))

(defapi get :get "/record.json"
  [app :- app-id
   id  :- id]
  []
  [:record])

(defapi post :post "/record.json"
  [app :- app-id]
  [record :- record])

(defapi put :put "/record.json"
  [app :- app-id]
  [id        :- id
   updateKey :- update-key
   record    :- record
   revision  :- revision])

(defapi put-assignees :put "/record/assignees.json"
  [app       :- app-id
   id        :- record-id
   assignees :- assignees]
  [revision :- revision])

(defapi put-status :put "/record/status.json"
  [app    :- app-id
   id     :- record-id
   action :- action]
  [assignee revision])

(defapi get-comments :get "/record/comments.json"
  [app    :- app-id
   record :- record-id]
  [order  :- order
   offset :- offset
   limit  :- limit]
  [:comments])

(defapi post-comment :post "/record/comment.json"
  [app     :- app-id
   record  :- record-id
   comment :- comment])

(defapi delete-comment :delete "/record/comment.json"
  [app     :- app-id
   record  :- record-id
   comment :- comment-id])
