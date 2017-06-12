(ns cybozu-http.api.kintone.space-test
  (:require [clojure.test :as t]
            [cybozu-http.api.kintone.space :as s]
            [test-helper :as h]
            [clojure.set :as set]))

(def db (atom {}))

(reset-meta! *ns* {})
(t/use-fixtures :once (h/wrap-setup db))

(t/deftest post-test
  (t/testing "space post test"
    (let [conf (h/read-config-file)
          auth (:login-info conf)
          template-id (get-in conf [:space :template-id])
          members [{:entity {:type "USER" :code (:login-name auth)}
                    :isAdmin true}]
          res (s/post auth template-id "cool space" members)]
      (t/is (re-matches #"^\d+$" res))

      ;; clean-up
      (s/delete auth res))))

(t/deftest get-test
  (t/testing "space get test"
    (let [conf (h/read-config-file)
          auth (:login-info conf)
          template-id (get-in conf [:space :template-id])
          members [{:entity {:type "USER" :code (:login-name auth)}
                    :isAdmin true}]
          space-id (s/post auth template-id "hello my space" members)
          res (s/get auth space-id)]
      (t/is (= (:name res) "hello my space"))
      (t/is (every? #(contains? res %) [:fixedMember :memberCount :coverUrl :modifier :creator
                                        :defaultThread :name :coverType :useMultiThread :id
                                        :attachedApps :isPrivate :body :isGuest :coverKey]))

      ;; clean-up
      (s/delete auth space-id))))

(t/deftest put-body-test
  (t/testing "space put body test"
    (let [{:keys [auth space-id]} @db
          before (s/get auth space-id)
          res (s/put-body auth space-id "Memory Trash Can")
          after (s/get auth space-id)]
      (t/is (empty? res))
      (t/is (nil? (:body before)))
      (t/is (= (:body after) "Memory Trash Can")))))

(t/deftest get-members-test
  (t/testing "space get members test"
    (let [{:keys [auth space-id]} @db
          res (s/get-members auth space-id)]
      (t/is (= (count res) 1))
      (t/is (= (get-in res [0 :entity :code]) (:login-name auth))))))

(t/deftest put-members-test
  (t/testing "space get members test"
    (let [{:keys [auth space-id]} @db
          members [{:entity {:type "USER" :code "Administrator"}
                    :isAdmin true}
                   {:entity {:type "GROUP" :code "Administrators"}
                    :isAdmin true}]
          before (s/get-members auth space-id)
          res (s/put-members auth space-id members)
          after (s/get-members auth space-id)]
      (t/is (empty? res))
      (t/is (= (set/difference (set after) (set before))
               #{{:entity {:type "GROUP" :code "Administrators"}
                  :isAdmin true}})))))
