(ns cybozu-http.api.kintone.apps-test
  (:require [cybozu-http.api.kintone.apps :as a]
            [clojure.test :as t]
            [test-helper :as h]))

(def db (atom {}))

(reset-meta! *ns* {})
(t/use-fixtures :once (h/wrap-setup db))

(t/deftest get-test
  (t/testing "get app test"
    (let [{:keys [auth space-id]} @db
          res (a/get auth {:space-ids [space-id]})]
      (t/is (= (count res) 1))
      (t/is (= (-> res first :name) "cybozu-http test app")))))
