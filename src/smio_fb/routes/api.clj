(ns smio-fb.routes.api
  (:require [compojure.core :refer [defroutes GET]]
            [clojure.java.io :as io]
            [taoensso.timbre :as timbre]
            [clojure.data.json :as json]
            [smio-fb.db.core :as db]))

(defn api-v1
  "Responds to requests to version one of the semant.io facebook REST API"
  [params]
  (let [page_id (:page_id params)]
    (let [response (db/get-page-deltas {:page_id (new BigDecimal page_id)})]
      (timbre/info response)
      response)))

(defroutes api-routes
  (GET "/" request (keys request))
  (GET "/api/v1" {params :params} (api-v1 params)))
