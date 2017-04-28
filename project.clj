(defproject ayato_p/cybozu-http "0.1.0-SNAPSHOT"
  :description "Cybozu HTTP client for Clojure"
  :url "https://github.com/ayato-p/cybozu-http-clj"
  :license {:name "MIT"}
  :dependencies [[clj-http "3.5.0"]
                 [cheshire "5.7.1"]
                 [camel-snake-kebab "0.4.0"]]
  :profiles
  {:dev {:dependencies [[org.clojure/clojure "1.8.0"]]}})
