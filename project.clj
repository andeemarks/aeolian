(defproject tutorial "0.1.0-SNAPSHOT"
  :description "AEOLIAN - Audio-ising code quality"
  :url "https://github.com/andeemarks/aeolian"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main aeolian.core
  :jvm-opts ^:replace []
  :dependencies [	[org.clojure/clojure "1.8.0"]]
  :profiles 
  	{:dev 
  		{:dependencies 
  			[
          [com.jakemccrary/lein-test-refresh "0.19.0"]
          [me.raynes/fs "1.4.6"]
          [com.taoensso/timbre "4.10.0"]
  				[midje "1.8.3" :exclusions [org.clojure/clojure]]]}})
