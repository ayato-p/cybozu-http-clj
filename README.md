# cybozu-http-clj [![CircleCI](https://circleci.com/gh/ayato-p/cybozu-http-clj.svg?style=svg)](https://circleci.com/gh/ayato-p/cybozu-http-clj)

Cybozu HTTP client for Clojure

Latest version is

[![Clojars Project](https://img.shields.io/clojars/v/ayato_p/cybozu-http.svg)](https://clojars.org/ayato_p/cybozu-http)

## Usage

```clojure
(require [cybozu-http.api.kintone :as k])

;; Authenticating via api Token
(k/record$get
 {:subdomain "mycompanysubdomain"
  :api-token "xxxxxx..."}
 1
 1)

```
