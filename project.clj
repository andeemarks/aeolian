(defproject aeolian "0.1.1-SNAPSHOT"
  :description "AEOLIAN - Audio-ising code quality"
  :url "https://github.com/andeemarks/aeolian"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main aeolian.core
  :aot  [aeolian.core]
  :jvm-opts ^:replace []
  :codox {:output-path "docs"
          :metadata {:doc/format :markdown}
          :source-paths ["src"]
          :source-uri "https://github.com/andeemarks/aeolian/blob/master/{filepath}#L{line}"}
  :dependencies [
                 [org.clojure/clojure "1.8.0"]
                 [proto-repl "0.3.1"]
                 [clansi "1.0.0"]
                 [prismatic/schema "1.1.6"]
                 [com.taoensso/timbre "4.10.0"]
                 [org.clojure/tools.namespace "0.2.11"]]
  :plugins
    [[lein-cljfmt "0.5.6"]]
  :uberjar-name "aeolian-standalone.jar"
  :profiles
    {:dev
       {:source-paths ["dev" "src" "test"]
        :plugins [[lein-midje "3.2.1"]]
        :dependencies [
                       [com.jakemccrary/lein-test-refresh "0.21.1"]
                       [me.raynes/fs "1.4.6"]
                       [lein-midje "3.2.1"]
                       [midje "1.8.3" :exclusions [org.clojure/clojure]]]}}
  :lein-release {:build-uberjar true :deploy-via :lein-install}
  :assemble 
    {:filesets {"" [["go.sh"]["go-gh.sh"]["gen-abc-prod.sh" :as "gen-abc.sh"]["README.md"]["LICENSE"]]
                "resources" [["resources/*"]]}
     :replacements {:uberjarname ""}
     :jar {:dest "" :uberjar true}
     :archive {:format :tgz}}   
)
             
