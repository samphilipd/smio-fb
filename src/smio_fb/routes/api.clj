(ns smio-fb.routes.api
  (:require [compojure.core :refer [defroutes GET]]
            [clojure.java.io :as io]
            [taoensso.timbre :as timbre]
            [clojure.data.json :as json]
            [smio-fb.db.core :as db]
            [bouncer.core :as b]
            [bouncer.validators :as v :refer [defvalidator]]
            [clj-time.format :as f]
            [clj-time.coerce :as c]))

(def valid-metrics ["likes"])
(def datetime-formatter (:date-time-no-ms f/formatters))
(defvalidator metric-validator
  {:default-message-format (str "%s must be one of " (clojure.string/join ", "valid-metrics))}
  [metric]
  (some #{metric} valid-metrics))

(defn to-db-time
  "Converts an ISO formatted time string into a SQL timestamp"
  [iso-time-string]
  (c/to-sql-time (f/parse datetime-formatter iso-time-string)))

(defn validate
  "Can we do something useful with the supplied parameters?"
  [params]
  (b/validate params
    :page_id v/required
    :metric metric-validator
    :start_time [[v/required] [v/datetime datetime-formatter]]
    :end_time [[v/required] [v/datetime datetime-formatter]]))

(defn api-v1
  "Responds to requests to version one of the semant.io facebook REST API"
  [params]
  ; (timbre/info (str params))
  (let [{:keys [metric page_id start_time end_time]} params
        errors (validate params)]
    (if (seq? errors)
      {:status 400 :body {:errors (first errors)}}
      {:status 200 :body (db/evolution-chart
        (str metric)
        (bigdec page_id)
        (to-db-time start_time)
        (to-db-time end_time))})))

(defroutes api-routes
  (GET "/" request (keys request))
  (GET "/api/v1" {params :params} (api-v1 params)))
