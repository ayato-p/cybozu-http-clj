(ns cybozu-http.test-helper
  (:require [baum.core :as b]
            [clojure.core.async :as async]
            [clojure.java.io :as io]
            [com.stuartsierra.component :as c]
            [cybozu-http.kintone.api.app :as app]
            [cybozu-http.kintone.api.preview-app :as preview-app]
            [cybozu-http.kintone.api.space :as space]))

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


(defn wait-deploying [auth app-ids]
  (let [continue? (atom true)]
    (while @continue?
      (when (->> (preview-app/get-deploy-statuses auth app-ids)
                 (map :status)
                 (every? #(not= % "PROCESSING")))
        (reset! continue? false)))))

(defn ^:deprecated create-test-space
  ([auth] (create-test-space auth false))
  ([auth guest?]
   (let [template-id (get-in (read-config-file) [:space :template-id])
         members [{:entity {:type "USER" :code (:login-name auth)}
                   :isAdmin true}]]
     (space/post auth template-id "cybozu-http test space" members))))

(defn ^:deprecated create-test-app [auth space-id thread-id]
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
    (wait-deploying auth [app-id])
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

(defrecord KintoneTestSpace [auth space]
  c/Lifecycle
  (start [this]
    (if-not (:space-id this)
      (let [template-id (:template-id space 1)
            members [{:entity {:type "USER" :code (:login-name auth)}
                      :isAdmin true}]]
        (let [space-id (space/post auth template-id "cybozu-http test space" members)
              space (space/get auth space-id)]
          (assoc this :space-id space-id :detail space)))
      this))
  (stop [this]
    (when (:space-id this)
      (space/delete auth (:space-id this)))
    (dissoc this :space-id)))

(defn new-kintone-test-space [auth space]
  (map->KintoneTestSpace {:auth auth :space space}))

(defrecord KintoneTestApp [auth space]
  c/Lifecycle
  (start [this]
    (if-not (:app-id this)
      (let [{:keys [space-id detail]} space
            {app-id :app} (->> {:space-id space-id :thread-id (:defaultThread detail)}
                               (preview-app/create auth "cybozu-http test app"))]
        (preview-app/put-settings auth app-id {:name "cybozu-http test app"
                                               :description "cybozu-http test app"
                                               :icon {:type "PRESET" :key "APP42"}
                                               :theme "RED"})
        (preview-app/post-fields auth app-id app-fields)
        (preview-app/put-layout auth app-id app-layout)
        (preview-app/put-views auth app-id app-views)
        (preview-app/deploy auth app-id)
        (wait-deploying auth [app-id])
        (assoc this :app-id app-id))
      this))
  (stop [this]
    ;; Can't delete app by API
    this))

(defn new-kintone-test-app [auth]
  (map->KintoneTestApp {:auth auth}))

(defn new-kintone-system []
  (let [{:keys [login-info space]} (read-config-file)]
    (-> (c/system-map
         :space (new-kintone-test-space login-info space)
         :app (new-kintone-test-app login-info))
        (c/system-using {:app [:space]}))))

(defonce system-request-channel (async/chan))

(defonce system-deliver-channel (async/chan))
(defonce system-deliver-publication
  (async/pub system-deliver-channel :uuid))

;;; system create requests
(defonce +requests+
  (let [systems (atom {})]
    (letfn [(watcher [_ _ _ new-val]
              (println new-val))]
      (add-watch systems :+requests+ watcher))
    {:systems systems
     :many-to-many-ch
     (async/go-loop []
       (let [{:keys [op uuid]} (async/<! system-request-channel)]
         (case op
           :create
           (let [system (try
                          (c/start-system (new-kintone-system))
                          (catch Exception e))]
             (swap! systems assoc uuid system)
             (->> {:uuid uuid :system system}
                  (async/>! system-deliver-channel)))
           :destroy
           (try
             (when-let [system (get @systems uuid)]
               (c/stop-system system)
               (swap! systems dissoc uuid))
             (catch Exception e)))
         (recur)))}))

(defmacro with-kintone-test-space [sym & body]
  `(let [uuid# (java.util.UUID/randomUUID)]
     (try
       (let [chan# (async/chan)
             sub# (async/sub system-deliver-publication uuid# chan#)
             _# (async/put! system-request-channel {:uuid uuid# :op :create})
             ~sym (:system (async/<!! chan#))]
         ~@body)
       (finally
         (async/put! system-request-channel {:uuid uuid# :op :destroy})))))
