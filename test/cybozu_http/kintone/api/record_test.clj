(ns cybozu-http.kintone.api.record-test
  (:require [clojure.test :as t]
            [cybozu-http.kintone.api.record :as r]
            [cybozu-http.test-helper :as h]))

(t/deftest post-test
  (t/testing "record post test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [app-id]} :app} kintone
            res (r/post auth app-id {:record h/record})]
        (t/is (contains? res :id))
        (t/is (contains? res :revision))

        (t/is (re-matches #"^\d+$" (:id res)))
        (t/is (= (:revision res) "1"))))))

(t/deftest get-test
  (t/testing "record get test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [app-id]} :app} kintone
            record1 (-> h/record
                        (assoc-in ["number" :value] 42)
                        (assoc-in ["single_text" :value] "response1")
                        (->> (hash-map :record)
                             (r/post auth app-id)))
            record2 (-> h/record
                        (assoc-in ["number" :value] 64)
                        (assoc-in ["single_text" :value] "response2")
                        (->> (hash-map :record)
                             (r/post auth app-id)))
            res1 (r/get auth app-id (:id record1))
            res2 (r/get auth app-id (:id record2))]
        (t/are [res number single-text] (and (= (get-in res [:number :value]) number)
                                             (= (get-in res [:single_text :value]) single-text))
          res1 "42" "response1"
          res2 "64" "response2")))))

(t/deftest put-test
  (t/testing "record put test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [app-id]} :app} kintone
            record (r/post auth app-id {:record h/record})
            before (r/get auth app-id (:id record))
            res (r/put auth app-id {:id (:id record)
                                    :record {:number {:value 42}}})
            after (r/get auth app-id (:id record))]
        (t/is (= (:revision res) "2"))
        (t/is (not= (get-in before [:number :value])
                    (get-in after [:number :value])))
        (t/is (= (get-in after [:number :value]) "42"))))))

(t/deftest put-status-test
  (t/testing "put status test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [app-id]} :app} kintone
            record (r/post auth app-id {:record h/record})]
        (try (r/put-status auth app-id (:id record) "done")
             (catch clojure.lang.ExceptionInfo e
               (t/is (= (:message (ex-data e))
                        "操作に失敗しました。プロセス管理機能が無効化されています。"))))))))

(t/deftest post-comment-test
  (t/testing "post comment test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [app-id]} :app} kintone
            record (r/post auth app-id {:record h/record})
            res (r/post-comment auth app-id (:id record) {:text "Hello, world"})]
        (t/is (contains? res :id))
        (t/is (re-matches #"^\d+$" (:id res)))))))

(t/deftest get-comments-test
  (t/testing "get comments test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [app-id]} :app} kintone
            record (r/post auth app-id {:record h/record})
            comment1 (r/post-comment auth app-id (:id record) {:text "Hello, world"})
            comment2 (r/post-comment auth app-id (:id record) {:text "Good bye"})
            res (r/get-comments auth app-id (:id record) {:order "asc"})]
        (t/is (= (map :text res)
                 ["Hello, world " "Good bye "]))))))

(t/deftest delete-comment-test
  (t/testing "delete comment test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [app-id]} :app} kintone
            record (r/post auth app-id {:record h/record})
            comment1 (r/post-comment auth app-id (:id record) {:text "Hello, world"})
            comment2 (r/post-comment auth app-id (:id record) {:text "Good bye"})
            before (r/get-comments auth app-id (:id record))
            res (r/delete-comment auth app-id (:id record) (:id comment1))
            after (r/get-comments auth app-id (:id record))]
        (t/is (empty? res))
        (t/is (= (count before) 2))
        (t/is (= (count after) 1))
        (t/is (map :text after)
              ["Good bye "])))))
