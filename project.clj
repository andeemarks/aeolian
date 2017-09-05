(defproject tutorial "0.1.0-SNAPSHOT"
  :description "AEOLIAN - Audio-ising code quality"
  :url "https://github.com/andeemarks/aeolian"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main aeolian.core
  :jvm-opts ^:replace []
  :codox {:output-path "docs"
          :metadata {:doc/format :markdown}
          :source-paths ["src"]
          :source-uri "https://github.com/andeemarks/aeolian/blob/master/{filepath}#L{line}"}
  :dependencies [
                 [org.clojure/clojure "1.8.0"]
                 [proto-repl "0.3.1"]
                 [prismatic/schema "1.1.6"]
                 [org.clojure/tools.namespace "0.2.11"]]
  :plugins
    [
     [lein-cljfmt "0.5.6"]]
  :profiles
    {:dev
       {:source-paths ["dev" "src" "test"]
        :dependencies
        [
             [com.jakemccrary/lein-test-refresh "0.20.0"]
             [me.raynes/fs "1.4.6"]
             [lein-midje "3.2.1"]
             [clansi "1.0.0"]
             [com.taoensso/timbre "4.10.0"]
             [midje "1.8.3" :exclusions [org.clojure/clojure]]]}})
