(ns cybozu-http.api.kintone.file-test
  (:require [clojure.test :as t]
            [cybozu-http.api.kintone.file :as f]
            [test-helper :as h]
            [clojure.java.io :as io]
            [cybozu-http.api.kintone.record :as r]))

(def db (atom {}))

(reset-meta! *ns* {})
(t/use-fixtures :once (h/wrap-setup db))

(t/deftest upload-download-test
  (t/testing "file upload test"
    (let [{:keys [auth app-id]} @db
          file (io/file (io/resource "upload-test-file.org"))
          upload-res (f/upload auth file)
          {id :id} (->> (assoc-in h/record ["attachment_file" :value 0 :fileKey] upload-res)
                        (hash-map :record)
                        (r/post auth app-id))
          res (r/get auth app-id id)
          download-res (f/download* auth (get-in res [:attachment_file :value 0 :fileKey]))]
      (t/is (string? upload-res))
      (t/is (string? (:body download-res)))
      (t/is (= (:body download-res) (slurp file))))))
