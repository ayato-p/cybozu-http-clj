(ns cybozu-http.kintone.api.file-test
  (:require [clojure.java.io :as io]
            [clojure.test :as t]
            [cybozu-http.kintone.api.file :as f]
            [cybozu-http.kintone.api.record :as r]
            [cybozu-http.test-helper :as h]))

(def db (atom {}))

(reset-meta! *ns* {})
(t/use-fixtures :once (h/wrap-setup db))

(defn- byte-array? [x]
  (= (type x)
     (Class/forName "[B")))

(t/deftest upload-download-test
  (let [{:keys [auth app-id]} @db
        file (io/file (io/resource "upload-test-file.org"))
        upload-res (f/upload auth file)
        {id :id} (->> (assoc-in h/record ["attachment_file" :value 0 :fileKey] upload-res)
                      (hash-map :record)
                      (r/post auth app-id))
        res (r/get auth app-id id)
        file-key (get-in res [:attachment_file :value 0 :fileKey])
        download-res (f/download auth file-key)]
    (t/testing "file upload test"
      (t/is (string? upload-res))
      (t/is (string? (:body download-res)))
      (t/is (= (:body download-res) (slurp file))))

    (t/testing "download as byte-array test"
      (let [download-res (f/download auth file-key {:as-byte-array? true})]
        (t/is (byte-array? (:body download-res)))))))
