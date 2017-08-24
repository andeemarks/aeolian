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
                           (c/metric-to-note "Foo.java#1 LL=0" k/major) => (contains n/rest-note))

              (tabular
               (fact "longer lines are mapped to actual notes with longer lines at higher octaves"
                     ?expected-octave => (contains (c/build-note ?line-length ?composition-key)))
               ?expected-octave    ?line-length  ?composition-key
               n/major-octave-1    1             k/major
               n/major-octave-1    9             k/major
               n/major-octave-2    10            k/major
               n/major-octave-2    39            k/major
               n/major-octave-3    40            k/major
               n/major-octave-3    79            k/major
               n/major-octave-4    80            k/major
               n/major-octave-4    99            k/major
               n/major-octave-5    100           k/major
               n/major-octave-5    200           k/major
               n/major-octave-5    2000          k/major
               n/minor-octave-1    1             k/minor
               n/minor-octave-1    9             k/minor
               n/minor-octave-2    10            k/minor
               n/minor-octave-2    39            k/minor
               n/minor-octave-3    40            k/minor
               n/minor-octave-3    79            k/minor
               n/minor-octave-4    80            k/minor
               n/minor-octave-4    99            k/minor
               n/minor-octave-5    100           k/minor
               n/minor-octave-5    200           k/minor
               n/minor-octave-5    2000          k/minor))

       (fact "complexity > 1 is mapped to tempo"
             (c/metric-to-note "Foo.java#1 LL=30 CC=1" k/major) =not=> (contains t/prefix)
             (c/metric-to-note "Foo.java#1 LL=30 CC=10" k/major) => (contains t/prefix)
             (c/metric-to-note "Foo.java#1 LL=30 CC=5" k/major) => (contains t/prefix)
             (c/metric-to-note "Foo.java#1 LL=30 CC=3" k/major) => (contains t/prefix))

       (future-fact "method-length is mapped to accompanying chord"
                    (str/index-of (c/metrics-to-measure "Foo.java#1 LL=30 ML=1") t/prefix) => falsey
                    (str/index-of (c/metrics-to-measure "Foo.java#1 LL=30 ML=10") t/prefix) => truthy
                    (str/index-of (c/metrics-to-measure "Foo.java#1 LL=30 ML=5") t/prefix) => truthy
                    (str/index-of (c/metrics-to-measure "Foo.java#1 LL=30 ML=3") t/prefix) => truthy))

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