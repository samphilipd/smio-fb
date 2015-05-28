(ns smio-fb.db.core
  (:require
    [yesql.core :refer [defqueries]]))

(def db-spec
  {:subprotocol "postgresql"
   :subname "//localhost/smio_fb_dev"
   :user "sam"
   :password ""})

(defqueries "sql/queries.sql" {:connection db-spec})
