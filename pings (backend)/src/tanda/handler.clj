(ns tanda.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [clojure.tools.logging :as log]

            [tanda.translators :as tr]
            ))

(defroutes app-routes
  (GET "/" [] "Hello World")

  (GET "/:deviceId/:date"
       {{device :deviceId date :date} :params}
       "Not implemented") ; TODO
  (GET "/:deviceId/:from/:to"
       {{device :deviceId dateFrom :from :dateTo :to} :params}
       "Not implemented") ; TODO

  ;; Actual Logging of Pings
  (POST "/:device/:epoch"
        {{device :device epoch :epoch} :params}
        (tr/trans-ping device epoch)) ; TODO
  (route/not-found "Not Found - Bad endpoint"))

(def app
  (wrap-defaults app-routes api-defaults))

(defn init
  "Initialises anything required for startup"
  []
  (log/trace "Initialisation start")
  ;; TODO Database setup
  nil

  (log/info "Initialisation complete")
  )
