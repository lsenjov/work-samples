(ns tanda.helpers
  (:require [clojure.tools.logging :as log]
            )
  (:gen-class)
  )

(defn parse-int
  "Parses an integer, returning nil if failed"
  [^String i]
  (log/trace "parse-int:" i)
  (try (Integer/parseInt i)
       (catch Exception e nil)))

(defn date-to-epoch
  "Parses a date YYYY-MM-DD to an epoch. Returns nil on error"
  [^String d]
  (log/trace "date-to-epoch:" d)
  (try (-> (java.text.SimpleDateFormat. "yyyy-MM-DD Z")
           (.parse (str d " +0000")) ;; Specify timezone as GMT, otherwise uses a local timezone
           (.getTime)
           (/ 1000)
           (int)
           )
       (catch Exception e (log/trace "date-to-epoch, could not parse:" d))))

(defn to-epoch
  "Parses either an integer OR a YYYY-MM-DD to epoch seconds. Returns nil both ways fail"
  [^String t]
  (if-let [ep (parse-int t)]
    ep
    (if-let [d (date-to-epoch t)]
      d
      nil ;; Both have failed, return nil
      )))
