(ns cybozu-http.kintone.api.app-test
  (:require [clojure.test :as t]
            [cybozu-http.kintone.api.app :as a]
            [cybozu-http.test-helper :as h]))

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
