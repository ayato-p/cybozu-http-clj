(ns cybozu-http.kintone.api.records-test
  (:require [clojure.test :as t]
            [cybozu-http.kintone.api.records :as r]
            [cybozu-http.test-helper :as h]))

(def db (atom {}))

(reset-meta! *ns* {})
(t/use-fixtures :once (h/wrap-setup db))

(t/deftest post-test
  (t/testing "records post test"
    (let [{:keys [auth app-id]} @db
          res (r/post auth app-id [(assoc-in h/record ["number" :value] 11)
                                   (assoc-in h/record ["number" :value] 21)])]
      (t/is (contains? res :ids))
      (t/is (contains? res :revisions))

      (t/is (every? #(re-matches #"^\d+$" %) (:ids res)))
      (t/is (every? #(= "1" %) (:revisions res))))))

(t/deftest get-test
  (t/testing "record get test"
    (let [{:keys [auth app-id]} @db
          {ids :ids} (->> [(-> h/record
                               (assoc-in ["number" :value] 12)
                               (assoc-in ["single_text" :value] "record1"))
                           (-> h/record
                               (assoc-in ["number" :value] 22)
                               (assoc-in ["single_text" :value] "record2"))]
                          (r/post auth app-id))
          res (r/get auth app-id {:query (-> (apply str "$id in (" (interpose "," ids)) (str ")"))
                                  :total-count true})]
      (t/is (= (:totalCount res) (str (count ids))))
      (t/are [number single-text] (-> (->> (:records res)
                                           (filter #(= (get-in % [:number :value]) number))
                                           first)
                                      (get-in [:single_text :value])
                                      (= single-text))
        "12" "record1"
        "22" "record2"))))

(t/deftest put-test
  (t/testing "record put test"
    (let [{:keys [auth app-id]} @db
          {[id1 id2 :as ids] :ids} (->> [(-> h/record
                                             (assoc-in ["number" :value] 24)
                                             (assoc-in ["single_text" :value] "record1"))
                                         (-> h/record
                                             (assoc-in ["number" :value] 42)
                                             (assoc-in ["single_text" :value] "record2"))]
                                        (r/post auth app-id))
          before (r/get auth app-id {:query (-> (apply str "$id in (" (interpose "," ids)) (str ")"))})
          res (r/put auth app-id [{:id id1 :record {"number" {:value 10}}}
                                  {:id id2 :record {"number" {:value 20}}}])
          after (r/get auth app-id {:query (-> (apply str "$id in (" (interpose "," ids)) (str ")"))})]
      (t/is (= (count res) 2))
      (t/is (every? #((set ids) (:id %)) res))
      (t/are [b a] (let [id (->> (:records before)
                                 (filter #(= (get-in % [:number :value]) b))
                                 first
                                 :$id
                                 :value)]
                     (-> (->> (:records after)
                              (filter #(= (get-in % [:$id :value]) id))
                              first)
                         (get-in [:number :value])
                         (= a)))
        "24" "10"
        "42" "20"))))

(t/deftest delete-test
  (t/testing "record delete test"
    (let [{:keys [auth app-id]} @db
          {[id1 id2 id3 id4 :as ids] :ids} (->> [h/record h/record h/record h/record]
                                                (r/post auth app-id))
          before (r/get auth app-id {:query (-> (apply str "$id in (" (interpose "," ids)) (str ")"))})
          res (r/delete auth app-id [id1 id3])
          after (r/get auth app-id {:query (-> (apply str "$id in (" (interpose "," ids)) (str ")"))})]
      (t/is (empty? res))
      (t/is (= (count (:records before)) 4))
      (t/is (= (count (:records after)) 2))
      (t/is (every? (set ids) (map (comp :value :$id) (:records before))))
      (t/is (every? #{id2 id4} (map (comp :value :$id) (:records after)))))))
