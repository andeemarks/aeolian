(defproject tutorial "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main tutorial.core
  :dependencies [[org.clojure/clojure "1.6.0"]
  				 [beatr.beatr "0.1.0-SNAPSHOT"]
  				 [quil "1.6.0" :exclusions [org.clojure/clojure]]
                 [overtone "0.9.1"]])
