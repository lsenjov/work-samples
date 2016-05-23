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
  (try (-> (java.text.SimpleDateFormat. "yyyy-MM-dd Z")
           (.parse (str d " +0000")) ;; Specify timezone as GMT, otherwise uses a local timezone
           (.getTime)
           (/ 1000)
           (long)
           )
       (catch Exception e
         (do
           (log/trace "date-to-epoch, could not parse:" d "exception:" e)
           nil))))

(defn to-epoch
  "Parses either an integer OR a YYYY-MM-DD to epoch seconds. Returns nil both ways fail"
  [^String t]
  (log/trace "to-epoch:" t)
  (if-let [ep (parse-int t)]
    ep
    (if-let [d (date-to-epoch t)]
      d
      nil ;; Both have failed, return nil
      )))

(defn pretty-print-sequence
  "Takes a sequence, adds sep between each value, adds the starter and end chars front and back."
  [^String sep ^String start ^String end arr]
  (str start \newline
       (apply str (interpose sep arr))
       \newline end))

(defn pretty-print-array
  "Takes a sequence, adds sep between each value, and adds square brackets front and back. Returns a string."
  [^String sep arr]
  (pretty-print-sequence sep "[" "]" arr))

(defn pretty-print-map
  "Takes an array, adds sep between each value, and adds map brackets front and back. Returns a string."
  [^String sep arr]
  (pretty-print-sequence sep "{" "}" arr))

(defn pretty-print-record
  "Takes a record and separator, pretty-prints the array and adds the value beforehand"
  ([^String sep [k v]]
   (pretty-print-record sep k v))
  ([^String sep k v]
   (str \"
        k
        \"
        ": "
        (pretty-print-array sep v))))
