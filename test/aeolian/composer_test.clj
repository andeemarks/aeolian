(ns aeolian.composer-test
  (:use midje.sweet)
  (:require [aeolian.composer :as c]
            [clojure.string :as str]
            [aeolian.abc.tempo :as t]
            [aeolian.abc.notes :as n]
            [aeolian.abc.key :as k]))

(facts "when processing metrics"
       (facts "line length is mapped to note"
              (fact "empty lines are mapped to rests"
                (str/index-of (:notes (c/build-measure ["Foo.java#1 LL=0"] k/major 1)) n/rest-note) => truthy)

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

       (defn- notes-in-measure [metrics]  (first (:notes (c/build-measure [metrics] k/major 1))))

       (fact "complexity > 1 is mapped to tempo"
             (notes-in-measure "Foo.java#1 LL=30 CC=1") =not=> (contains t/prefix)
             (notes-in-measure "Foo.java#1 LL=30 CC=10") => (contains t/prefix)
             (notes-in-measure "Foo.java#1 LL=30 CC=5") => (contains t/prefix)
             (notes-in-measure "Foo.java#1 LL=30 CC=3") => (contains t/prefix))

       (defn- measure-for [metrics key] (c/metrics-to-measure [metrics] key 1))

       (fact "method-length is mapped to accompanying chord"
             (measure-for "Foo.java#1 LL=30 ML=1" k/major) => (contains "\"C\"")
             (measure-for "Foo.java#1 LL=30 ML=10" k/major) => (contains "\"Dm\"")
             (measure-for "Foo.java#1 LL=30 ML=5" k/minor) => (contains "\"Cm\"")
             (measure-for "Foo.java#1 LL=30 ML=11" k/minor) => (contains "\"_E\"")))

(facts "when opening metrics files"
       (fact "all lines are used in composition"
             (c/compose ["/home/amarks/Code/aeolian/resources/Notification.java#1 LL=3"
                         "/home/amarks/Code/aeolian/resources/Notification.java#10 LL=70"
                         "/home/amarks/Code/aeolian/resources/Notification.java#100 LL=99"] k/minor) => truthy)

       (fact "no metrics means no composition"
             (c/compose [] k/major) => nil))
