(ns tanda.persist
  (:require
    [taoensso.timbre :as log]
    )
  (:gen-class)
  )

(defn ping-insert
  "Inserts a ping into the database. Returns a map with :success flag and :message string"
  [device epoch]
  (log/trace "ping-insert:" device epoch)
  {:success false :message "Not initialised"}
  )
