# cybozu-http-clj [![wercker status](https://app.wercker.com/status/740d77c301264b2775cce0212e18fe21/s/master "wercker status")](https://app.wercker.com/project/byKey/740d77c301264b2775cce0212e18fe21)

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
