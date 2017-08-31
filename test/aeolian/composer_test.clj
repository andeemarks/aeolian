(ns aeolian.composer-test
  (:use midje.sweet)
  (:require [aeolian.composer :as c]
            [clojure.string :as str]
            [aeolian.tempo :as t]
            [aeolian.abc.notes :as n]
            [aeolian.abc.key :as k]))

(facts "when processing metrics"
       (facts "line length is mapped to note"
              (fact "empty lines are mapped to rests"
                           (:note (c/metric-to-note "Foo.java#1 LL=0" k/major anything anything anything)) => (contains n/rest-note))

              (tabular
               (fact "longer lines are mapped to actual notes with longer lines at higher octaves"
                     ?expected-octave => (contains (c/build-note ?line-length ?composition-key)))
               ?expected-octave    ?line-length  ?composition-key
               n/major-octave-1    9             k/major
               n/major-octave-2    39            k/major
               n/major-octave-3    79            k/major
               n/major-octave-4    99            k/major
               n/major-octave-5    2000          k/major
               n/minor-octave-1    9             k/minor
               n/minor-octave-2    39            k/minor
               n/minor-octave-3    79            k/minor
               n/minor-octave-4    99            k/minor
               n/minor-octave-5    2000          k/minor))

       (future-fact "changes to git author produces a change in instrument")
       (future-fact "changes to source file name produces a change in lyrics")

       (fact "complexity > 1 is mapped to tempo"
             (:note (c/metric-to-note "Foo.java#1 LL=30 CC=1" k/major anything anything anything)) =not=> (contains t/prefix)
             (:note (c/metric-to-note "Foo.java#1 LL=30 CC=10" k/major anything anything anything)) => (contains t/prefix)
             (:note (c/metric-to-note "Foo.java#1 LL=30 CC=5" k/major anything anything anything)) => (contains t/prefix)
             (:note (c/metric-to-note "Foo.java#1 LL=30 CC=3" k/major anything anything anything)) => (contains t/prefix))

       (fact "method-length is mapped to accompanying chord"
             (str/index-of (c/metrics-to-measure ["Foo.java#1 LL=30 ML=1"] k/major) "\"C\"") => truthy
             (str/index-of (c/metrics-to-measure ["Foo.java#1 LL=30 ML=10"] k/major) "\"Dm\"") => truthy
             (str/index-of (c/metrics-to-measure ["Foo.java#1 LL=30 ML=5"] k/minor) "\"Cm\"") => truthy
             (str/index-of (c/metrics-to-measure ["Foo.java#1 LL=30 ML=11"] k/minor) "\"_E\"") => truthy))

(facts "when opening metrics files"
       (fact "all lines are used in composition"
             (c/compose ["/home/amarks/Code/aeolian/resources/Notification.java#1 LL=3"
                         "/home/amarks/Code/aeolian/resources/Notification.java#10 LL=70"
                         "/home/amarks/Code/aeolian/resources/Notification.java#100 LL=99"] k/minor) => truthy)

       (fact "file names are persisted"
             (c/compose ["/home/amarks/Code/aeolian/resources/Notification.java#1 LL=3"] k/major)
             (c/get-source-file) => "Notification.java")

       (fact "no metrics means no composition"
             (c/compose [] k/major) => nil))
