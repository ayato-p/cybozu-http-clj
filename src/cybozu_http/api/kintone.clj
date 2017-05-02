(ns cybozu-http.api.kintone
  (:require [clojure.string :as str]))

(defmacro import-ns
  ([ns-sym]
   `(import-ns ns-sym nil))
  ([ns-sym prefix]
   (require ns-sym)
   (let [accumulator (fn [acc [sym vr]]
                       (let [sym (symbol (cond->> sym prefix (str prefix "$")))]
                         (assoc acc sym vr)))
         ps (->> (ns-publics ns-sym)
                 (reduce accumulator {}))]
     `(do
        ~@(for [[sym# vr#] ps]
            `(def ~sym# ~vr#))))))

(import-ns cybozu-http.api.kintone.record record)
