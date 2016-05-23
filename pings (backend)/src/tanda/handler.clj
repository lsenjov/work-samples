(ns tanda.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [taoensso.timbre :as log]

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
  (POST "/user/:device/:epoch"
        {{device :device epoch :epoch} :params}
        (tr/trans-ping device epoch)) ; TODO
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes api-defaults))

(defn init
  "Initialises anything required for startup"
  []
  ;; TODO Database setup
  nil
  )
