(ns test-helper
  (:require [baum.core :as b]
            [clojure.java.io :as io]
            [cybozu-http.api.kintone.preview-app :as preview-app]
            [cybozu-http.api.kintone.space :as space]))

(defn read-config-file* []
  (b/read-file (io/resource "config.edn")))

(def read-config-file
  (memoize read-config-file*))

(def app-fields
  {"single_text"     {:code           "single_text"
                      :defaultValue   ""
                      :expression     ""
                      :hideExpression false
                      :maxLength      64
                      :minLength      0
                      :label          "Single Text"
                      :noLabel        false
                      :required       true
                      :type           "SINGLE_LINE_TEXT"
                      :unique         false}
   "number"          {:code         "number"
                      :defaultValue "12345"
                      :digit        true
                      :displayScale ""
                      :maxValue     64
                      :minValue     0
                      :label        "Number"
                      :required     false
                      :type         "NUMBER"
                      :unique       false
                      :unit         "$"
                      :unitPosition "BEFORE"}
   "radio_button"    {:code         "radio_button"
                      :defaultValue "sample2"
                      :label        "radio_button"
                      :options      {"sample1" {:label "sample1" :index 0}
                                     "sample2" {:label "sample2" :index 1}
                                     "sample3" {:label "sample3" :index 2}
                                     "sample4" {:label "sample4" :index 3}}
                      :align        "horizontal"
                      :required     true
                      :type         "RADIO_BUTTON"}
   "checkbox"        {:code         "checkbox"
                      :defaultValue ["sample1"]
                      :label "Checkbox"
                      :noLabel false
                      :options {"sample1" {:label "sample1" :index 0}
                                "sample2" {:label "sample2" :index 1}
                                "sample3" {:label "sample3" :index 2}}
                      :align        "HORIZONTAL"
                      :required     false
                      :type         "CHECK_BOX"}
   "date"            {:code            "date"
                      :defaultNowValue true
                      :defaultValue    ""
                      :label           "Date"
                      :noLabel         false
                      :required        true
                      :type            "DATE"
                      :unique          false}
   "datetime"        {:code            "datetime"
                      :defaultNowValue false
                      :defaultValue    "2012-07-19T00:00:00.000Z"
                      :label           "Datetime"
                      :noLabel         false
                      :type            "DATETIME"
                      :unique          false}
   "attachment_file" {:code          "attachment_file"
                      :label         "Attachment File"
                      :noLabel       true
                      :required      false
                      :type          "FILE"
                      :thumbnailSize 150}
   "link"            {:code        "link"
                      :defaultValu "https://kintoneapp.com"
                      :maxLengt    64
                      :minLength   0
                      :label       "Link"
                      :noLabel     true
                      :protocol    "WEB"
                      :required    false
                      :type        "LINK"
                      :unique      false}
   "table"           {:code   "table"
                      :type   "SUBTABLE"
                      :fields {:single_text_in_table
                               {:code           "single_text_in_table"
                                :defaultValue   ""
                                :expression     ""
                                :hideExpression false
                                :maxLength      64
                                :minLength      0
                                :label          "Single text in table"
                                :noLabel        false
                                :required       true
                                :type           "SINGLE_LINE_TEXT"
                                :unique         false}}}})

(def app-layout
  [{:type   "ROW"
    :fields [{:code "single_text"
              :type "SINGLE_LINE_TEXT"
              :size {:width 200}}
             {:code "number"
              :type "NUMBER"
              :size {:width 100}}
             {:code "radio_button"
              :type "RADIO_BUTTON"
              :size {:width 200}}
             {:code "checkbox"
              :type "CHECK_BOX"
              :size {:width 200}}
             {:code "date"
              :type "DATE"
              :size {:width 100}}
             {:code "datetime"
              :type "DATETIME"
              :size {:width 200}}
             {:code "attachment_file"
              :type "FILE"
              :size {:width 100}}
             {:code "link"
              :type "LINK"
              :size {:width 200}}]}
   {:type   "SUBTABLE"
    :code   "table"
    :fields [{:code "single_text_in_table"
              :type "SINGLE_LINE_TEXT"
              :size {:width 500}}]}])

(def app-views
  {"test index"       {:type       "LIST"
                       :name       "test index"
                       :fields     ["single_text" "number" "date" "datetime"]
                       :filterCond "datetime < NOW()"
                       :sort       "single_text asc"
                       :index      0}
   "test calendar"    {:type       "CALENDAR"
                       :name       "test calendar"
                       :date       "date"
                       :title      "single_text"
                       :filterCond ""
                       :sort       "number asc"
                       :index      1}
   "test custom view" {:type       "CUSTOM"
                       :name       "test custom view"
                       :html       "<p>test custom view html code</p>"
                       :filterCond ""
                       :sort       "date asc"
                       :index      2}})

(def app-acl
  [{:entity           {:code nil :type "CREATOR"}
    :includeSubs      false
    :appEditable      true
    :recordViewable   true
    :recordAddable    true
    :recordEditable   true
    :recordDeletable  true
    :recordImportable false
    :recordExportable false}
   {:entity           {:type "GROUP"
                       :code "Administrators"}
    :includeSubs      false
    :appEditable      true
    :recordViewable   true
    :recordAddable    true
    :recordEditable   false
    :recordDeletable  false
    :recordImportable false
    :recordExportable false}])

(def record-acl
  [{:filterCond ""
    :entities   [{:entity      {:type "GROUP" :code "Administrators"}
                  :viewable    true
                  :editable    false
                  :deletable   false
                  :includeSubs false}]}])

(def field-acl
  [{:code "number"
    :entities [{:entity {:type "GROUP" :code "Administrators"}
                :accessibility "READ"
                :includeSubs false}]}
   {:code "date"
    :entities [{:entity {:type "GROUP" :code "Administrators"}
                :accessibility "WRITE"
                :includeSubs false}]}])

(def record
  {"single_text"     {:value "single_text value"}
   "number"          {:value 10}
   "radio_button"    {:value "sample1"}
   "checkbox"        {:value ["sample1" "sample2"]}
   "date"            {:value "2017-06-07"}
   "datetime"        {:value "2017-06-07T00:00:00Z"}
   "attachment_file" {:value []}
   "link"            {:value "https://kintoneapp.com"}
   "table"           {:value [{:value {"single_text_in_table" {:value "single_text_in_table value1"}}}
                              {:value {"single_text_in_table" {:value "single_text_in_table value2"}}}]}})


(defn create-test-space
  ([auth] (create-test-space auth false))
  ([auth guest?]
   (let [template-id (get-in (read-config-file) [:space :template-id])
         members [{:entity {:type "USER" :code (:login-name auth)}
                   :isAdmin true}]]
     (space/post auth template-id "cybozu-http test space" members))))

(defn create-test-app [auth space-id thread-id]
  (let [res (->> {:space-id space-id :thread-id thread-id}
                 (preview-app/create auth "cybozu-http test app"))
        app-id (:app res)]
    (preview-app/put-settings auth app-id {:nmae "cybozu-http test app"
                                           :description "cybozu-http test app"
                                           :icon {:type "PRESET" :key "APP42"}
                                           :theme "RED"})
    (preview-app/post-fields auth app-id app-fields)
    (preview-app/put-layout auth app-id app-layout)
    (preview-app/put-views auth app-id app-views)
    (preview-app/deploy auth app-id)
    (let [continue? (atom true)]
      (while @continue?
        (if-not (= (:status (preview-app/get-deploy-status auth app-id)) "PROCESSING")
          (reset! continue? false))))
    app-id))

(defn- setup [db]
  (let [auth (:login-info (read-config-file))
        space-id (create-test-space auth)
        thread-id (:defaultThread (space/get auth space-id))
        app-id (create-test-app auth space-id thread-id)]
    (swap! db assoc :auth auth :space-id space-id :thread-id thread-id :app-id app-id)))

(defn- teardown [db]
  (space/delete (:auth @db) (:space-id @db))
  (reset! db {}))

(defn wrap-setup [db]
  (fn [f]
    (setup db)
    (f)
    (teardown db)))
