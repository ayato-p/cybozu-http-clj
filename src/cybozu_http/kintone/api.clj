(ns cybozu-http.kintone.api)

(defrecord Boundary
    [domain subdomain
     login-name password api-token
     basic-login-name basic-password])
