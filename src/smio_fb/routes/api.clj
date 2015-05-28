(ns smio-fb.routes.api
  (:require [compojure.core :refer [defroutes GET]]
            [clojure.java.io :as io]
            [taoensso.timbre :as timbre]))

(defn api-v1
  "Responds to requests to version one of the semant.io facebook REST API"
  [params]
  (timbre/info params))

(defroutes api-routes
  (GET "/" request (keys request))
  (GET "/api/v1" {params :params} (api-v1 params)))
