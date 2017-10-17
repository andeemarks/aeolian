(ns aeolian.parser-test
  (:use midje.sweet)
  (:require [aeolian.parser :as parser]))

(facts "When parsing a metric line"
       (fact "the commit author can be found"
             (:author (parser/parse "/ExecNpmOfflineMojo.java#1 AU=<adam.dubiel@allegro.pl> LL=3 TS=1396276249")) => "<adam.dubiel@allegro.pl>")

       (fact "the commit timestamp can be found"
             (:timestamp (parser/parse "/ExecNpmOfflineMojo.java#1 LL=33 AU=<adam.dubiel@allegro.pl> TS=1396276249")) => 1396276249)

       (facts "the line length"
              (fact "can be found"
                    (:line-length (parser/parse "/Notification.java#190 LL=13")) => 13)

              (fact "must be numeric"
                    (:line-length (parser/parse "/Notification.java#190 LL=abc")) => (throws Exception)))

       (fact "indentation errors can be found"
             (:indentation? (parser/parse "/Notification.java#190 LL=23 IND")) => truthy)

       (fact "the source file can be found"
             (:source-file (parser/parse "/Notification.java#190 IND LL=10")) => "Notification.java")

       (facts "the complexity"
              (fact "can be found"
                    (:complexity (parser/parse "/Notification.java#190 LL=8 CC=9")) => 9)

              (fact "must be numeric"
                    (:complexity (parser/parse "/Notification.java#190 LL=135 CC=abc")) => (throws Exception))

              (fact "defaults to 0"
                    (:complexity (parser/parse "/Notification.java#190 LL=13")) => 0))

       (facts "the metric-type"
        (fact "defaults to :regular"
          (:type (parser/parse "/Notification.java#190 LL=13")) => :regular))
       
       (facts "the method length"
              (fact "can be found"
                    (:method-length (parser/parse "/Notification.java#190 ML=16 LL=98")) => 16)

              (fact "must be numeric"
                    (:method-length (parser/parse "/Notification.java#190 ML=abc LL=435")) => (throws Exception))

              (fact "defaults to nil"
                    (:method-length (parser/parse "/Notification.java#190 LL=13")) => nil)
              (:method-length (parser/parse "/Notification.java#190 LL=13 CC=9")) => nil))
