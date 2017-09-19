(ns cybozu-http.kintone.api.bare-test
  (:require [clojure.test :as t]
            [cybozu-http.kintone.api.bare :as b]
            [test-helper :as h]))

(t/deftest generate-url-test
  (t/is (= (b/generate-url {:subdomain "acme"} "/foo.json" nil)
           "https://acme.cybozu.com/k/v1/foo.json"))

  (t/is (= (b/generate-url {:domain "cybozu.net" :subdomain "acme"} "/foo.json" nil)
           "https://acme.cybozu.net/k/v1/foo.json"))

  (t/is (= (b/generate-url {:subdomain "acme"} "/foo.json" 1)
           "https://acme.cybozu.com/k/guest/1/v1/foo.json")))

(t/deftest auth-headers-test
  (t/is (= (b/auth-headers {:login-name "admin" :password "foobar"})
           {:headers {:X-Cybozu-Authorization "YWRtaW46Zm9vYmFy"}}))

  (t/is (= (b/auth-headers {:api-token "this_is_api_token"})
           {:headers {:X-Cybozu-API-Token "this_is_api_token"}})))
