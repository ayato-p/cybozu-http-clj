(ns cybozu-http.kintone.api.preview-app
  (:require [cybozu-http.kintone.api.internal.preview-app :as internal]))

(defprotocol PreviewAppAPI
  (create
    [auth name]
    [auth name opts])
  (get-customize
    [auth app-id]
    [auth app-id opts])
  (put-customize
    [auth app-id]
    [auth app-id opts])
  (get-status
    [auth app-id]
    [auth app-id opts])
  (put-status
    [auth app-id]
    [auth app-id opts]))

(extend-protocol PreviewAppAPI
  clojure.lang.IPersistentMap
  (create
    ([auth name]
     (internal/create auth name))
    ([auth name opts]
     (internal/create auth name opts)))
  (get-customize
    ([auth app-id]
     (internal/get-customize auth app-id))
    ([auth app-id opts]
     (internal/get-customize auth app-id opts)))
  (put-customize
    ([auth app-id]
     (internal/put-customize auth app-id))
    ([auth app-id opts]
     (internal/put-customize auth app-id opts)))
  (get-status
    ([auth app-id]
     (internal/get-status auth app-id))
    ([auth app-id opts]
     (internal/get-status auth app-id opts)))
  (put-status
    ([auth app-id]
     (internal/put-status auth app-id))
    ([auth app-id opts]
     (internal/put-status auth app-id opts))))

(defprotocol DeployAPI
  (deploy-apps
    [auth apps]
    [auth apps opts])
  (deploy
    [auth app-id]
    [auth app-id opts])
  (get-deploy-statuses
    [auth app-ids]
    [auth app-ids opts])
  (get-deploy-status
    [auth app-id]))

(extend-protocol DeployAPI
  clojure.lang.IPersistentMap
  (create
    ([auth name]
     (internal/create auth name))
    ([auth name opts]
     (internal/create auth name opts)))
  (deploy-apps
    ([auth apps]
     (internal/deploy-apps auth apps))
    ([auth apps opts]
     (internal/deploy-apps auth apps opts)))
  (deploy
    ([auth app-id]
     (internal/deploy auth app-id))
    ([auth app-id opts]
     (->> (reduce into [] opts)
          (apply internal/deploy auth app-id))))
  (get-deploy-statuses
    ([auth app-ids]
     (internal/get-deploy-statuses auth app-ids))
    ([auth app-ids opts]
     (internal/get-deploy-statuses auth app-ids opts)))
  (get-deploy-status
    ([auth app-id]
     (internal/get-deploy-status auth app-id))))

(defprotocol SettingAPI
  (get-settings [auth app-id] [auth app-id opts])
  (put-settings [auth app-id] [auth app-id opts]))

(extend-protocol SettingAPI
  clojure.lang.IPersistentMap
  (get-settings
    ([auth app-id]
     (internal/get-settings auth app-id))
    ([auth app-id opts]
     (internal/get-settings auth app-id opts)))
  (put-settings
    ([auth app-id]
     (internal/put-settings auth app-id))
    ([auth app-id opts]
     (internal/put-settings auth app-id opts))))

(defprotocol FieldAPI
  (get-fields
    [auth app-id]
    [auth app-id opts])
  (post-fields
    [auth app-id fields]
    [auth app-id fields opts])
  (put-fields
    [auth app-id fields]
    [auth app-id fields opts])
  (delete-fields
    [auth app-id field-codes]
    [auth app-id field-codes opts]))

(extend-protocol FieldAPI
  clojure.lang.IPersistentMap
  (get-fields
    ([auth app-id]
     (internal/get-fields auth app-id))
    ([auth app-id opts]
     (internal/get-fields auth app-id opts)))
  (post-fields
    ([auth app-id fields]
     (internal/post-fields auth app-id fields))
    ([auth app-id fields opts]
     (internal/post-fields auth app-id fields opts)))
  (put-fields
    ([auth app-id fields]
     (internal/put-fields auth app-id fields))
    ([auth app-id fields opts]
     (internal/put-fields auth app-id fields opts)))
  (delete-fields
    ([auth app-id field-codes]
     (internal/delete-fields auth app-id field-codes))
    ([auth app-id field-codes opts]
     (internal/delete-fields auth app-id field-codes opts))))

(defprotocol LayoutAPI
  (get-layout [auth app-id] [auth app-id opts])
  (put-layout [auth app-id layout] [auth app-id layout opts]))

(extend-protocol LayoutAPI
  clojure.lang.IPersistentMap
  (get-layout
    ([auth app-id]
     (internal/get-layout auth app-id))
    ([auth app-id opts]
     (internal/get-layout auth app-id opts)))
  (put-layout
    ([auth app-id layout]
     (internal/put-layout auth app-id layout))
    ([auth app-id layout opts]
     (internal/put-layout auth app-id layout opts))))

(defprotocol ViewAPI
  (get-views [auth app-id] [auth app-id opts])
  (put-views [auth app-id views] [auth app-id views opts]))

(extend-protocol ViewAPI
  clojure.lang.IPersistentMap
  (get-views
    ([auth app-id]
     (internal/get-views auth app-id))
    ([auth app-id opts]
     (internal/get-views auth app-id opts)))
  (put-views
    ([auth app-id views]
     (internal/put-views auth app-id views))
    ([auth app-id views opts]
     (internal/put-views auth app-id views opts))))

(defprotocol AclAPI
  (get-acl [auth app-id] [auth app-id opts])
  (put-acl [auth app-id rights] [auth app-id rights opts])
  (get-record-acl [auth app-id] [auth app-id opts])
  (put-record-acl [auth app-id rights] [auth app-id rights opts])
  (get-field-acl [auth app-id] [auth app-id opts])
  (put-field-acl [auth app-id rights] [auth app-id rights opts]))

(extend-protocol AclAPI
  clojure.lang.IPersistentMap
  (get-acl
    ([auth app-id]
     (internal/get-acl auth app-id))
    ([auth app-id opts]
     (internal/get-acl auth app-id opts)))
  (put-acl
    ([auth app-id rights]
     (internal/put-acl auth app-id rights))
    ([auth app-id rights opts]
     (internal/put-acl auth app-id rights opts)))
  (get-record-acl
    ([auth app-id]
     (internal/get-record-acl auth app-id))
    ([auth app-id opts]
     (internal/get-record-acl auth app-id opts)))
  (put-record-acl
    ([auth app-id rights]
     (internal/put-record-acl auth app-id rights))
    ([auth app-id rights opts]
     (internal/put-record-acl auth app-id rights opts)))
  (get-field-acl
    ([auth app-id]
     (internal/get-field-acl auth app-id))
    ([auth app-id opts]
     (internal/get-field-acl auth app-id opts)))
  (put-field-acl
    ([auth app-id rights]
     (internal/put-field-acl auth app-id rights))
    ([auth app-id rights opts]
     (internal/put-field-acl auth app-id rights opts))))
