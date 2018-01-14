(ns ^:eftest/synchronized cybozu-http.kintone.api.preview-app-test
  (:require [clojure.java.io :as io]
            [clojure.test :as t]
            [cybozu-http.kintone.api.file :as f]
            [cybozu-http.kintone.api.preview-app :as p]
            [cybozu-http.test-helper :as h]))

(t/deftest create-test
  (t/testing "create test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [space-id thread-id]} :space} kintone
            res (p/create auth "my app" {:space-id space-id :thread-id thread-id})]
        (t/is (every? #(contains? res %) [:app :revision]))
        (t/is (every? #(re-matches #"^\d+$" (get res %)) [:app :revision]))))))

(t/deftest deploy-apps-test
  (t/testing "deploy apps test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [space-id thread-id]} :space} kintone
            {app-id1 :app} (p/create auth "my app 1" {:space-id space-id :thread-id thread-id})
            {app-id2 :app} (p/create auth "my app 2" {:space-id space-id :thread-id thread-id})
            res (p/deploy-apps auth [{:app app-id1} {:app app-id2}])]
        (t/is (empty? res))

        (h/wait-deploying auth [app-id1 app-id2])))))

(t/deftest deploy-test
  (t/testing "deploy app test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [space-id thread-id]} :space} kintone
            {app-id :app} (p/create auth "my app 1" {:space-id space-id :thread-id thread-id})
            res (p/deploy auth app-id)]
        (t/is (empty? res))

        (h/wait-deploying auth [app-id])))))

(t/deftest get-deploy-statuses-test
  (t/testing "get deploy statuses test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [app-id]} :app} kintone
            res (p/get-deploy-statuses auth [app-id])]
        (t/is (= (count res) 1))
        (t/is (contains? (first res) :app))
        (t/is (contains? (first res) :status))
        (t/is (= (:status (first res)) "SUCCESS"))))))

(t/deftest get-deploy-status-test
  (t/testing "get deploy status test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [app-id]} :app} kintone
            res (p/get-deploy-status auth app-id)]
        (t/is (= (:status res) "SUCCESS"))))))

(t/deftest get-settings-test
  (t/testing "get settings test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [app-id]} :app} kintone
            res (p/get-settings auth app-id)]
        (t/is (= (:name res) "cybozu-http test app"))))))

(t/deftest put-settings-test
  (t/testing "put settings test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [app-id]} :app} kintone
            before (p/get-settings auth app-id)
            res (p/put-settings auth app-id {:name "my first app"})
            after (p/get-settings auth app-id)]
        (t/is (contains? res :revision))
        (t/is (not= (:name before) (:name after)))
        (t/is (= (:name after) "my first app"))))))

(def simple-app-fields
  {"title" {:type "SINGLE_LINE_TEXT" :code "title" :label "title" :required true}
   "content" {:type "MULTI_LINE_TEXT" :code "content" :label "content"}})

(t/deftest get-fields-test
  (t/testing "get fields test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [space-id thread-id]} :space} kintone
            app-id (:app (p/create auth "simple" {:space-id space-id :thread-id thread-id}))
            _ (p/post-fields auth app-id simple-app-fields)
            res (p/get-fields auth app-id)]
        (t/is (contains? res :revision))
        (t/is (contains? res :properties))
        (t/are [field-code ftype fcode flable frequired] (let [field (get-in res [:properties field-code])]
                                                           (and (= (:type field) ftype)
                                                                (= (:code field) fcode)
                                                                (= (:label field) flable)
                                                                (= (:required field) frequired)))
          :title "SINGLE_LINE_TEXT" "title" "title" true
          :content "MULTI_LINE_TEXT" "content" "content" false)))))

(t/deftest post-fields-test
  (t/testing "post fields test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [space-id thread-id]} :space} kintone
            app-id (:app (p/create auth "yet another simple app" {:space-id space-id :thread-id thread-id}))
            res (p/post-fields auth app-id simple-app-fields)]
        (t/is (contains? res :revision))
        (t/is (re-matches #"^\d+$" (:revision res)))))))

(t/deftest put-fields-test
  (t/testing "put fields test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [space-id thread-id]} :space} kintone
            app-id (:app (p/create auth "yet another simple app" {:space-id space-id :thread-id thread-id}))
            _ (p/post-fields auth app-id simple-app-fields)
            ya-fields {"title" {:type "SINGLE_LINE_TEXT" :code "title" :label "title" :required true
                                :maxLength 20 :minLength 10}}
            res (p/put-fields auth app-id ya-fields)
            after (p/get-fields auth app-id)]
        (t/is (contains? res :revision))
        (t/is (re-matches #"^\d+$" (:revision res)))
        (t/is (let [field (get-in after [:properties :title])]
                (and (= (:maxLength field) "20")
                     (= (:minLength field) "10"))))))))

(t/deftest delete-fields-test
  (t/testing "delete fields test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [space-id thread-id]} :space} kintone
            app-id (:app (p/create auth "yet another simple app" {:space-id space-id :thread-id thread-id}))
            _ (p/post-fields auth app-id simple-app-fields)
            res (p/delete-fields auth app-id ["title"])
            after (p/get-fields auth app-id)]
        (t/is (contains? res :revision))
        (t/is (re-matches #"^\d+$" (:revision res)))
        (t/is (not (contains? (:properties after) :title)))
        (t/is (contains? (:properties after) :content))))))

(t/deftest get-layout-test
  (t/testing "get layout test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [space-id thread-id]} :space} kintone
            app-id (:app (p/create auth "yet another simple app" {:space-id space-id :thread-id thread-id}))
            _ (p/post-fields auth app-id simple-app-fields)
            res (p/get-layout auth app-id)]
        (t/is (contains? res :layout))
        (t/is (contains? res :revision))
        (t/are [row-number row-type field-type field-code] (let [row (get (:layout res) row-number)
                                                                 field (first (:fields row))]
                                                             (and (= (:type row) row-type)
                                                                  (= (:type field) field-type)
                                                                  (= (:code field) field-code)))
          0 "ROW" "SINGLE_LINE_TEXT" "title"
          1 "ROW" "MULTI_LINE_TEXT" "content")))))

(t/deftest put-layout-test
  (t/testing "put layout test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [space-id thread-id]} :space} kintone
            app-id (:app (p/create auth "yet another simple app" {:space-id space-id :thread-id thread-id}))
            _ (p/post-fields auth app-id simple-app-fields)
            res (p/put-layout auth app-id [{:type "ROW"
                                            :fields [{:type "MULTI_LINE_TEXT" :code "content"}
                                                     {:type "SINGLE_LINE_TEXT" :code "title"}]}])
            after (p/get-layout auth app-id)]
        (t/is (contains? res :revision))
        (t/are [idx field-type field-code] (let [field (get-in after [:layout 0 :fields idx])]
                                             (and (= (:type field) field-type)
                                                  (= (:code field) field-code)))
          0 "MULTI_LINE_TEXT" "content"
          1 "SINGLE_LINE_TEXT" "title")))))

(t/deftest get-views-test
  (t/testing "get views test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [space-id thread-id]} :space} kintone
            app-id (:app (p/create auth "yet another simple app" {:space-id space-id :thread-id thread-id}))
            _ (p/post-fields auth app-id simple-app-fields)
            res (p/get-views auth app-id)]
        (t/is (contains? res :views))
        (t/is (contains? res :revision))
        (t/is (map? (:views res)))))))

(t/deftest put-views-test
  (t/testing "put views test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [space-id thread-id]} :space} kintone
            app-id (:app (p/create auth "yet another simple app" {:space-id space-id :thread-id thread-id}))
            _ (p/post-fields auth app-id simple-app-fields)
            res (p/put-views auth app-id {"only title" {:index 0 :type "LIST" :name "only title"
                                                        :fields ["title"]}
                                          "with content" {:index 1 :type "LIST" :name "with content"
                                                          :fields ["title" "content"]}})]
        (t/is (contains? res :views))
        (t/is (contains? res :revision))
        (t/are [view-name] (let [view (get-in res [:views (keyword view-name)])]
                             (and (contains? view :id)
                                  (re-matches #"^\d+$" (:id view))))
          "only title" "with content")))))

(t/deftest get-acl-test
  (t/testing "get acl test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [space-id thread-id]} :space} kintone
            app-id (:app (p/create auth "yet another simple app" {:space-id space-id :thread-id thread-id}))
            _ (p/post-fields auth app-id simple-app-fields)
            res (p/get-acl auth app-id)]
        (t/is (contains? res :rights))
        (t/is (contains? res :revision))))))

(t/deftest put-acl-test
  (t/testing "put acl test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [space-id thread-id]} :space} kintone
            app-id (:app (p/create auth "yet another simple app" {:space-id space-id :thread-id thread-id}))
            _ (p/post-fields auth app-id simple-app-fields)
            res (p/put-acl auth app-id [{:entity {:type "CREATOR" :code nil}
                                         :appEditable true}])]
        (t/is (contains? res :revision))))))

(t/deftest get-record-acl-test
  (t/testing "get record acl test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [space-id thread-id]} :space} kintone
            app-id (:app (p/create auth "yet another simple app" {:space-id space-id :thread-id thread-id}))
            _ (p/post-fields auth app-id simple-app-fields)
            res (p/get-record-acl auth app-id)]
        (t/is (contains? res :rights))
        (t/is (contains? res :revision))))))

(t/deftest put-record-acl-test
  (t/testing "put record acl test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [space-id thread-id]} :space} kintone
            app-id (:app (p/create auth "yet another simple app" {:space-id space-id :thread-id thread-id}))
            _ (p/post-fields auth app-id simple-app-fields)
            res (p/put-record-acl auth app-id [{:entities [{:entity {:type "USER" :code "Administrator"}
                                                            :viewable true}]}])]
        (t/is (contains? res :revision))))))

(t/deftest get-field-acl-test
  (t/testing "get field acl test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [space-id thread-id]} :space} kintone
            app-id (:app (p/create auth "yet another simple app" {:space-id space-id :thread-id thread-id}))
            _ (p/post-fields auth app-id simple-app-fields)
            res (p/get-field-acl auth app-id)]
        (t/is (contains? res :rights))
        (t/is (contains? res :revision))))))

(t/deftest put-field-acl-test
  (t/testing "put field acl test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [space-id thread-id]} :space} kintone
            app-id (:app (p/create auth "yet another simple app" {:space-id space-id :thread-id thread-id}))
            _ (p/post-fields auth app-id simple-app-fields)
            res (p/put-field-acl auth app-id [{:code "title"
                                               :entities [{:entity {:type "USER" :code "Administrator"}
                                                           :accessibility "READ"}]}])]
        (t/is (contains? res :revision))))))

(t/deftest get-customize-test
  (t/testing "get customize test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [space-id thread-id]} :space} kintone
            app-id (:app (p/create auth "yet another simple app" {:space-id space-id :thread-id thread-id}))
            _ (p/post-fields auth app-id simple-app-fields)
            res (p/get-customize auth app-id)]
        (t/is (contains? res :scope))
        (t/is (contains? res :desktop))
        (t/is (contains? res :mobile))
        (t/is (contains? res :revision))))))

(t/deftest post-customize-test
  (t/testing "post customize test"
    (h/with-kintone-test-space kintone
      (let [{auth :auth {:keys [space-id thread-id]} :space} kintone
            app-id (:app (p/create auth "yet another simple app" {:space-id space-id :thread-id thread-id}))
            _ (p/post-fields auth app-id simple-app-fields)
            desktop-js (f/upload auth (io/file (io/resource "desktop.js")))
            desktop-css (f/upload auth (io/file (io/resource "desktop.css")))
            mobile-js (f/upload auth (io/file (io/resource "mobile.js")))
            res (p/put-customize auth app-id {:scope "ALL"
                                              :desktop {:js [{:file {:fileKey desktop-js}}]
                                                        :css [{:file {:fileKey desktop-css}}]}
                                              :mobile {:js [{:file {:fileKey mobile-js}}]}})]
        (t/is (contains? res :revision))))))
