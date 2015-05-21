(ns smio-fb.routes.api
  (:require [compojure.core :refer [defroutes GET]]
            [clojure.java.io :as io]))

(defroutes api-routes
  (GET "/" request (keys request)))
