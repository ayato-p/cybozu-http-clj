(ns cybozu-http.kintone.api.apps-test
  (:require [clojure.test :as t]
            [cybozu-http.kintone.api.apps :as a]
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
