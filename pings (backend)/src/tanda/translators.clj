(ns tanda.translators
  (:require
    [clojure.tools.logging :as log]

    [tanda.persist :as persist]
    [tanda.helpers :as h]
    )
  (:gen-class)
  )

(defn trans-ping
  "Takes and stores a device-epoch record"
  [device epoch]
  (log/trace "trans-ping:" device epoch)
  (if-let [t (h/parse-int epoch)]
    (let [status (persist/ping-insert device epoch)]
      (if (:success status)
        "Success"
        (str "Failed: " (:message status))
        )
      )
    "Invalid epoch format, must be an integer" ;; Failed to parse epoch
    )
  )
