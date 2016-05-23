(ns tanda.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [clojure.tools.logging :as log]

            [tanda.translators :as tr]
            ))

(defroutes app-routes
  (GET "/" [] "Hello World")

  (GET "/devices"
       []
       (tr/trans-devices))
  (GET "/:deviceId/:date"
       {{device :deviceId date :date} :params}
       (tr/trans-get-pings device date))
  (GET "/:deviceId/:from/:to"
       {{device :deviceId dateFrom :from dateTo :to} :params}
       (tr/trans-get-pings device dateFrom dateTo))

  ;; Actual Logging of Pings
  (POST "/:device/:epoch"
        {{device :device epoch :epoch} :params}
        (tr/trans-ping device epoch))
  (POST "/clear_data"
        []
        (tr/trans-clear))
  (route/not-found "Not Found - Bad endpoint"))

(def app
  (wrap-defaults app-routes api-defaults))
