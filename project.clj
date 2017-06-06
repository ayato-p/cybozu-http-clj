(defproject ayato_p/cybozu-http "0.1.0-SNAPSHOT"
  :description "Cybozu HTTP client for Clojure"
  :url "https://github.com/ayato-p/cybozu-http-clj"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}

  :deploy-repositories [["snapshots" {:url      "https://clojars.org/repo/"
                                      :username [:gpg :env]
                                      :password [:gpg :env]}]
                        ["releases" {:url   "https://clojars.org/repo/"
                                     :creds :gpg}]]

  :dependencies [[clj-http "3.5.0"]
                 [cheshire "5.7.1"]]
  :profiles
  {:dev {:dependencies [[org.clojure/clojure "1.8.0"]
                        [spootnik/unilog "0.7.20"]]
         :injections [(require '[unilog.config :as uni])
                      (uni/start-logging! {:level :debug :console true})]}
   :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}
   :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
   :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}
   :1.9 {:dependencies [[org.clojure/clojure "1.9.0-alpha16"]]}})
