(ns cybozu-http.kintone.url-test
  (:require [cybozu-http.kintone.url :as sut]
            [clojure.test :as t]))

(t/deftest parse-test
  (t/testing "parse default space app"
    (t/are [url parsed] (= (sut/parse url) parsed)
      "https://foo.cybozu.com/k/1"
      {:subdomain "foo" :domain "cybozu.com" :app-id "1"}

      "https://foo.cybozu.com/k/99999999"
      {:subdomain "foo" :domain "cybozu.com" :app-id "99999999"}

      "https://foo.s.cybozu.com/k/1"
      {:subdomain "foo" :domain "cybozu.com" :app-id "1"}

      "https://foo-bar.cybozu.com/k/1"
      {:subdomain "foo-bar" :domain "cybozu.com" :app-id "1"}

      "https://foo-bar-baz.cybozu.com/k/1"
      {:subdomain "foo-bar-baz" :domain "cybozu.com" :app-id "1"}

      "https://foo99.cybozu.com/k/1"
      {:subdomain "foo99" :domain "cybozu.com" :app-id "1"}

      "https://foo99.cybozu.com/k/1/show"
      {:subdomain "foo99" :domain "cybozu.com" :app-id "1"}

      "https://foo99.cybozu.com/k/1/?q=foo%20%3D%20\"1\""
      {:subdomain "foo99" :domain "cybozu.com" :app-id "1"}

      "https://foo_bar.cybozu.com/k/1"
      nil))

  (t/testing "parse guest space app"
    (t/are [url parsed] (= (sut/parse url) parsed)
      "https://foo.cybozu.com/k/guest/11/1"
      {:subdomain "foo" :domain "cybozu.com" :guest-space-id "11" :app-id "1"}

      "https://foo.cybozu.com/k/guest/11/99999999"
      {:subdomain "foo" :domain "cybozu.com" :guest-space-id "11" :app-id "99999999"}

      "https://foo.s.cybozu.com/k/guest/11/1"
      {:subdomain "foo" :domain "cybozu.com" :guest-space-id "11" :app-id "1"}

      "https://foo-bar.cybozu.com/k/guest/11/1"
      {:subdomain "foo-bar" :domain "cybozu.com" :guest-space-id "11" :app-id "1"}

      "https://foo-bar-baz.cybozu.com/k/guest/11/1"
      {:subdomain "foo-bar-baz" :domain "cybozu.com" :guest-space-id "11" :app-id "1"}

      "https://foo99.cybozu.com/k/guest/11/1"
      {:subdomain "foo99" :domain "cybozu.com" :guest-space-id "11" :app-id "1"}

      "https://foo99.cybozu.com/k/guest/11/1/show"
      {:subdomain "foo99" :domain "cybozu.com" :guest-space-id "11" :app-id "1"}

      "https://foo99.cybozu.com/k/guest/11/1/?q=foo%20%3D%20\"1\""
      {:subdomain "foo99" :domain "cybozu.com" :guest-space-id "11" :app-id "1"}

      "https://foo_bar.cybozu.com/k/guest/11/1"
      nil))
  )
