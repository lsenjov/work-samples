(ns tanda.translators
  (:require
    [clojure.tools.logging :as log]

    [tanda.persist :as persist]
    [tanda.helpers :as h]
    )
  (:gen-class)
  )

(def secondsInDay 86400)

(defn trans-ping
  "Takes and stores a device-epoch record"
  [device epoch]
  (log/trace "trans-ping:" device epoch)
  (if-let [t (h/parse-int epoch)]
    (let [status (persist/ping-insert! device epoch)]
      (if (:success status)
        "Success"
        (str "Failed: " (:message status))
        )
      )
    "Invalid epoch format, must be an integer" ;; Failed to parse epoch
    )
  )

(defn get-pings
  "Gets pings by device in a time range"
  [device ^Integer start ^Integer end]
  {device (persist/get-by-time device start end)})

(defn get-records
  "Gets pings, assuming start and finish are both integers"
  [device ^Integer start ^Integer end]
  (if (= device "all")
    (h/pretty-print-map
      ",\n"
      (map (partial h/pretty-print-record ",\n")
           (remove
             (fn [x] (= 0 (count (second x)))) ; Remove devices with no entries in this time period
             (map persist/get-record-by-time ; Get the records of each device for printing
                  (persist/get-unique-devices)
                  (repeat start)
                  (repeat end)))))
    (h/pretty-print-array ",\n" (persist/get-by-time device start end))))


(defn trans-get-pings
  "Gets all the pings from a single device in a time period"
  ([device date] ;; This form MUST be YYYY-MM-DD, not epoch time
   (if-let [start (h/date-to-epoch date)] ;; Must make sure this is good first
     (get-records device start (+ start secondsInDay))
     {:status 400 :message "Invalid date."}
     ))
  ([device start end]
   (let [sEpoch (h/to-epoch start)
         eEpoch (h/to-epoch end)]
     (if (and sEpoch eEpoch)
       (get-records device sEpoch eEpoch)
       {:status 400
        :message (str "Invalid time" (if sEpoch end start))}
     )))
  )

(defn trans-devices
  "Gets a list of all devices"
  []
  (h/pretty-print-array
    ",\n"
    (map (fn [s] (str \" s \"))
         (persist/get-unique-devices))))

(defn trans-clear
  "Clears all data from the table"
  []
  (persist/clear-data!))
