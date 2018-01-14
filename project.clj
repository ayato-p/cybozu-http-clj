(defproject ayato_p/cybozu-http "0.1.1-SNAPSHOT"
  :description "Cybozu HTTP client for Clojure"
  :url "https://github.com/ayato-p/cybozu-http-clj"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}

  :deploy-repositories [["snapshots" {:url      "https://clojars.org/repo/"
                                      :username [:gpg :env]
                                      :password [:gpg :env]}]
                        ["releases" {:url   "https://clojars.org/repo/"
                                     :creds :gpg}]]

  :dependencies [[clj-http "3.7.0"]
                 [cheshire "5.8.0"]]

  :aliases {"all" ["with-profiles" "+1.8:+1.9"]}

  :profiles
  {:dev {:resource-paths ["env/dev/resources"]
         :dependencies [[rkworks/baum "0.4.0"]
                        [com.stuartsierra/component "0.3.2"]
                        [org.clojure/core.async "0.4.474"]]}
   :provided {:dependencies [[org.clojure/clojure "1.9.0"]]}
   :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}
   :1.9 {:dependencies [[org.clojure/clojure "1.9.0"]]}})
