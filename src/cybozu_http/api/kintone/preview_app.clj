(ns cybozu-http.api.kintone.preview-app
  (:require [cybozu-http.api.kintone.bare :refer [defapi]]))

;;; create app
;;; doc https://developer.cybozu.io/hc/ja/articles/204529834

(defapi create :post "/preview/app.json"
  [name :- name]
  [space  :- space-id
   thread :- thread-id])

;;; deploy app
;;; doc https://developer.cybozu.io/hc/ja/articles/204699420
;;;     https://developer.cybozu.io/hc/ja/articles/210100886

(def deploy-url "/preview/app/deploy.json")

(defapi deploy-apps :post deploy-url
  [apps :- apps]
  [revert :- revert])

(defn deploy [auth app-id & {:keys [revert] :as opts}]
  (deploy-apps auth [{:app app-id}] :revert (or revert false)))

(defapi get-deploy-statuses :get deploy-url
  [apps :- app-ids])

(defn get-deploy-status [auth app-id]
  (get-deploy-statuses auth [app-id]))

;;; general configurations
;;; doc https://developer.cybozu.io/hc/ja/articles/204694170
;;;     https://developer.cybozu.io/hc/ja/articles/204730520

(def settings-url "/preview/app/settings.json")

(defapi get-settings :get settings-url
  [app :- app-id]
  [lang :- language])

(defapi put-settings :put settings-url
  [app :- app-id]
  [name        :- name
   description :- description
   icon        :- icon
   theme       :- theme
   revision    :- revision])

;;; form configurations
;;; doc: https://developer.cybozu.io/hc/ja/articles/204783170
;;;      https://developer.cybozu.io/hc/ja/articles/204529724

(def fields-url "/preview/app/form/fields.json")

(defapi get-fields :get fields-url
  [app :- app-id]
  [lang :- language])

(defapi post-fields :post fields-url
  [app        :- app-id
   properties :- fields]
  [revision :- revision])

(defapi put-fields :put fields-url
  [app        :- app-id
   properties :- fields]
  [revision :- revision])

(defapi delete-fields :delete fields-url
  [app    :- app-id
   fields :- field-codes]
  [revision :- revision])

(def layout-url "/preview/app/form/layout.json")

(defapi get-layout :get layout-url
  [app    :- app-id])

(defapi put-layout :put layout-url
  [app    :- app-id
   layout :- layout]
  [revision :- revision])

;;; view configurations
;;; doc https://developer.cybozu.io/hc/ja/articles/204529784
;;;     https://developer.cybozu.io/hc/ja/articles/204529794

(def views-url "/preview/app/views.json")

(defapi get-views :get views-url
  [app :- app-id]
  [lang :- language])

(defapi put-views :put views-url
  [app   :- app-id
   views :- views]
  [revision :- revision])

;;; access control list configurations
;;; doc https://developer.cybozu.io/hc/ja/articles/204529754
;;;     https://developer.cybozu.io/hc/ja/articles/201941854

(def acl-url "/preview/app/acl.json")

(defapi get-acl :get acl-url
  [app :- app-id])

(defapi put-acl :put acl-url
  [app    :- app-id
   rights :- rights]
  [revision :- revision])

;;; record access control list configurations
;;; doc https://developer.cybozu.io/hc/ja/articles/204791510
;;;     https://developer.cybozu.io/hc/ja/articles/201941854

(def record-acl-url "/preview/record/acl.json")

(defapi get-record-acl :get record-acl-url
  [app :- app-id]
  [lang :- language])

(defapi put-record-acl :put record-acl-url
  [app    :- app-id
   rights :- rights]
  [revision :- revision])

;;; field access control list configurations
;;; doc https://developer.cybozu.io/hc/ja/articles/204791520
;;;     https://developer.cybozu.io/hc/ja/articles/201941864

(def field-acl-url "/preview/field/acl.json")

(defapi get-field-acl :get field-acl-url
  [app :- app-id])

(defapi put-field-acl :put field-acl-url
  [app    :- app-id
   rights :- rights]
  [revision :- revision])


;;; customize configurations
;;; doc https://developer.cybozu.io/hc/ja/articles/204529824
;;;     https://developer.cybozu.io/hc/ja/articles/204529834

(def customize-url "/preview/app/customize.json")

(defapi get-customize :get customize-url
  [app :- app-id])

(defapi put-customize :put customize-url
  [app :- app-id]
  [scope    :- scope
   desktop  :- desctop-customize
   mobile   :- mobile-customize
   revision :- revision])

;;; process management configurations
;;; doc https://developer.cybozu.io/hc/ja/articles/216972946
;;;

(def status-url "/preview/app/status.json")

(defapi get-status :get status-url
  [app :- app-id]
  [lang :- language])

(defapi put-status :put status-url
  [app :- app-id]
  [enable :- enable?
   status   :- status
   actions  :- actions
   revision :- revision])
