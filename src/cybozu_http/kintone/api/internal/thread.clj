(ns cybozu-http.kintone.api.internal.thread
  (:require [cybozu-http.kintone.api.internal.bare :refer [defapi]]))

(defapi put :put "/space/thread.json"
  [id :- thread-id]
  [name :- name
   body :- body])

(defapi post-comment :post "/space/thread/comment.json"
  [space   :- space-id
   thread  :- thread-id
   comment :- comment])
