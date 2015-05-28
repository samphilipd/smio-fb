(ns smio-fb.db.core
  (:require
    [yesql.core :refer [defqueries]]))

(def db-spec
  {:subprotocol "postgresql"
   :subname "//localhost/smio_fb_dev"
   :user "sam"
   :password ""})

(defqueries "sql/queries.sql" {:connection db-spec})

(defn format-as-array
  "Converts a results hash into an array suitable for injecting into a Flot chart"
  [results]
  (taoensso.timbre/info results)
  (map
    #(identity [(:timestamp %) (:n_likes %)])
    results))

(defn evolution-chart
  "Queries the DB and returns a data structure suitable for injecting into the
   evolution chart of the semant.io frontend"
  [metric, page_id, start_time, end_time]
  (taoensso.timbre/info (str start_time end_time))
  (condp = metric
    "likes" (format-as-array (get-page-deltas {:page_id page_id :start_time start_time :end_time end_time}))))

;; change
