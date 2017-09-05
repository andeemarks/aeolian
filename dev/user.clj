(ns user
  (:require [clojure.tools.namespace.repl :as tnr]
            [clojure.repl]
            [schema.core :as s]
            [proto-repl.saved-values]))

(defn start []
  (println "Start completed"))

(defn reset []
  (tnr/refresh :after 'user/start))

(println "proto-repl-demo dev/user.clj loaded.")
(s/set-fn-validation! true)
