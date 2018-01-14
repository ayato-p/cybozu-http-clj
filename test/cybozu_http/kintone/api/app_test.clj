(ns cybozu-http.kintone.api.app-test
  (:require [clojure.test :as t]
            [cybozu-http.kintone.api.app :as a]
            [cybozu-http.test-helper :as h]))

(t/deftest get-test
  (t/testing "get app test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [app-id]} :app} kintone
            res (a/get auth app-id)]
        (t/is (every? #(contains? res %) [:description :threadId :modifier :creator :name
                                          :spaceId :createdAt :appId :code :modifiedAt]))
        (t/is (= (:name res) "cybozu-http test app"))))))
