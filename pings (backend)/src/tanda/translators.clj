(ns tanda.translators
  (:require
    [taoensso.timbre :as log]

    [tanda.persist :as persist]
    )
  (:gen-class)
  )

(defn trans-ping
  "Takes and stores a device-epoch record"
  [device epoch]
  (log/trace "trans-ping:" device epoch)
  (let [status (persist/ping-insert device epoch)]
    (if (:success status)
      "Success"
      "Failed"
      )
    )
  )
