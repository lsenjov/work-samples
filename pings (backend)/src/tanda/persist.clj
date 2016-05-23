(ns tanda.persist
  (:require
    [clojure.tools.logging :as log]
    [clojure.java.jdbc :as jdb]
    )
  (:gen-class)
  )

(def db {:subprotocol "mysql"
         :subname "//127.0.0.1:3306/tanda_ping"
         :user "pingUser"})

(defn- query
  "Performs the query against the defined database, and logs the query"
  [& quer]
  (log/trace "Performing Query: " quer)
  (jdb/query db quer))

(defn get-all
  "Gets ALL timestamps"
  []
  (query "SELECT pings.device, pings.epoch FROM pings;"))

(defn get-unique-devices
  "Gets a list of all devices, sorted alphabetically"
  []
  (sort (map :device (query "SELECT DISTINCT pings.device FROM pings;"))))

(defn get-by-device
  "Gets all timestamps from the device"
  [device]
  (map :epoch (query "SELECT pings.epoch FROM pings WHERE device = ?;" device)))

(defn get-by-time
  "Gets all timestamps from a device between start inclusive and end exclusive"
  [device startEpoch endEpoch]
  (query "SELECT pings.epoch FROM pings
         WHERE device = ?
         AND pings.epoch >= ?
         AND pings.epoch < ?;"
         device
         startEpoch
         endEpoch))

(defn ping-insert
  "Inserts a ping into the database. Returns a map with :success flag and :message string"
  [device epoch]
  (log/trace "ping-insert:" device epoch)
  (try
    (let [result (jdb/insert! db :pings {:device device :epoch epoch})]
      (log/trace "ping-insert, result: " result)
      {:success true :message "Key inserted"}
      )
    (catch Exception e
      (do
        (log/trace "ping-insert, caught exception: " e)
        {:success false :message "Failed to insert key into database"}))
    )
  )
