(ns cybozu-http.kintone.api.apps-test
  (:require [clojure.test :as t]
            [cybozu-http.kintone.api.apps :as a]
            [cybozu-http.test-helper :as h]))

(t/deftest get-test
  (t/testing "get app test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [space-id]} :space} kintone
            res (a/get auth {:space-ids [space-id]})]
        (t/is (= (count res) 1))
        (t/is (= (-> res first :name) "cybozu-http test app"))))))
