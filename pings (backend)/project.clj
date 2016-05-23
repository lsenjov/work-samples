(defproject tanda "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [compojure "1.4.0"]
                 [ring/ring-defaults "0.1.5"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler tanda.handler/app}
  :profiles
  {:dev {:dependencies [
                        ;; Compojure deps
                        [javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]
                        
                        ;; Logging Deps
                        [org.clojure/tools.logging "0.3.1"]
                        [org.slf4j/slf4j-log4j12 "1.7.1"]
                        [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                           javax.jms/jms
                                                           com.sun.jmdk/jmxtools
                                                           com.sun.jmx/jmxri]]
                        [com.taoensso/timbre "4.3.1"]
                        ]}})
