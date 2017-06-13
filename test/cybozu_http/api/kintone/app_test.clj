(ns cybozu-http.api.kintone.app-test
  (:require [clojure.test :as t]
            [cybozu-http.api.kintone.app :as a]
            [test-helper :as h]))

(def db (atom {}))

(reset-meta! *ns* {})
(t/use-fixtures :once (h/wrap-setup db))

(t/deftest get-test
  (t/testing "get app test"
    (let [{:keys [auth app-id]} @db
          res (a/get auth app-id)]
      (t/is (every? #(contains? res %) [:description :threadId :modifier :creator :name
                                        :spaceId :createdAt :appId :code :modifiedAt]))
      (t/is (= (:name res) "cybozu-http test app")))))
