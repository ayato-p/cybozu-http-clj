(ns cybozu-http.api.kintone.record
  (:refer-clojure :exclude [get])
  (:require [cybozu-http.api.kintone.bare :refer [defapi]]))

(defapi get :get "/record.json" [app id])

(defapi post :post "/record.json" [app] [record])

(defapi put :put "/record.json" [app] [id update-key record revision])

(defapi put-assignees :put "/record/assignees.json" [app id assignees] [revision])

(defapi put-status :put "/record/status.json" [app id action] [assignee revision])

(defapi get-comments :get "/record/comments.json" [app record] [order offset limit])

(defapi post-comment :post "/record/comment.json" [app record comment])

(defapi delete-comment :delete "/record/comment.json" [app record comment])
