(ns tanda.persist
  (:require
    [clojure.tools.logging :as log]
    [clojure.java.jdbc :as jdb]
    [clojure.edn :as edn]
    )
  (:gen-class)
  )

(def default-db
  {:subprotocol "mysql"
   :subname "//127.0.0.1:3306/tanda_ping"
   :user "pingUser"})

(def db
  (try 
    (let [d (edn/read-string (slurp "dbConfig.edn"))]
      (if (and (:subprotocol d)
               (:subname d)
               (:user d))
        (do
          (log/info "Loading from dbConfig.edn")
          d)
        (do
          (log/info "dbConfig.edn not complete, reverting to defaults")
          default-db)))
    (catch java.io.FileNotFoundException e
      (do
        (log/info "Could not find dbConfig.edn, reverting to defaults")
        default-db))))

(defn- query
  "Performs the query against the defined database, and logs the query"
  [& quer]
  (log/trace "Performing Query: " quer)
  (jdb/query db quer))

(defn get-all
  "Gets ALL timestamps"
  []
  (log/trace "get-all.")
  (query "SELECT pings.device, pings.epoch FROM pings;"))

(defn get-unique-devices
  "Gets a list of all devices, sorted alphabetically"
  []
  (log/trace "get-unique-devices.")
  (sort (map :device (query "SELECT DISTINCT pings.device FROM pings;"))))

(defn get-by-device
  "Gets all timestamps from the device"
  [device]
  (log/trace "get-by-device:" device)
  (mapv :epoch (query "SELECT pings.epoch FROM pings WHERE device = ?;" device)))

(defn get-by-time
  "Gets all timestamps from a device between start inclusive and end exclusive"
  [device startEpoch endEpoch]
  (log/trace "get-by-time:" device startEpoch endEpoch)
  (mapv :epoch
        (query "SELECT pings.epoch FROM pings
               WHERE device = ?
               AND pings.epoch >= ?
               AND pings.epoch < ?;"
               device
               startEpoch
               endEpoch)))

(defn get-record-by-time
  "As get-by-time, but returns as a vector instead with the key as record"
  [device ^Integer startEpoch ^Integer endEpoch]
  (log/trace "get-record-by-time:" device startEpoch endEpoch)
  [device (get-by-time device startEpoch endEpoch)])

(defn ping-insert!
  "Inserts a ping into the database. Returns a map with :success flag and :message string"
  [device ^Integer epoch]
  (log/trace "ping-insert!:" device epoch)
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

(defn clear-data!
  "Clears all data from the database table."
  []
  (log/trace "clear-data!.")
  (try (jdb/delete! db :pings ["idpings >= 0"])
       {:status 200}
       (catch Exception e {:status 500}) ;; This *should* always succeed
       ))
