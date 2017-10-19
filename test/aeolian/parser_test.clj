(ns aeolian.parser-test
  (:use midje.sweet)
  (:require [aeolian.parser :as parser]))

(facts "When parsing a metric line"
       (fact "the commit author can be found"
             (:author (parser/parse "{:source-file \"/ExecNpmOfflineMojo.java\" :line 1 :author \"<adam.dubiel@allegro.pl>\" :line-length \"3\" :timestamp 1396276249}")) => "<adam.dubiel@allegro.pl>")

       (fact "the commit timestamp can be found"
             (:timestamp (parser/parse "{:source-file \"/ExecNpmOfflineMojo.java\" :line 1 :line-length \"33\" :author \"<adam.dubiel@allegro.pl>\" :timestamp 1396276249}")) => 1396276249)

       (facts "the line length"
              (fact "can be found"
                    (:line-length (parser/parse "{:source-file \"/Notification.java\" :line 190 :line-length \"13\"}")) => 13)

              (fact "can include non-numeric chars"
                    (:line-length (parser/parse "{:source-file \"/Notification.java\" :line 190 :line-length \"1,371\"}")) => 1371)

              (fact "defaults to 0"
                    (:line-length (parser/parse "{:source-file \"/Notification.java\" :line 190 }")) => 0))

       (facts "indentation errors"
              (fact "can be found"
                    (:indentation-violation? (parser/parse "{:source-file \"/Notification.java\" :line 190 :line-length \"23\" :indentation-violation? true}")) => true)

              (fact "defaults to false"
                    (:indentation-violation? (parser/parse "{:source-file \"/Notification.java\" :line 190 :line-length \"23\"}")) => false))

       (fact "the source file can be found"
             (:source-file (parser/parse "{:source-file \"/Notification.java\" :line 190 :line-length \"10\"}")) => "/Notification.java")

       (facts "the complexity"
              (fact "can be found"
                    (:complexity (parser/parse "{:source-file \"/Notification.java\" :line 190 :line-length \"8\" :complexity \"9\"}")) => 9)

              (fact "defaults to 0"
                    (:complexity (parser/parse "{:source-file \"/Notification.java\" :line 190 :line-length \"13\"}")) => 0))

       (facts "the method-count"
              (fact "can be found"
                    (:method-count (parser/parse "{:source-file \"/Notification.java\" :line 190 :line-length \"8\" :method-count \"9\"}")) => 9)

              (fact "defaults to 0"
                    (:method-count (parser/parse "{:source-file \"/Notification.java\" :line 190 :line-length \"13\"}")) => 0))

       (facts "the file-length"
              (fact "can be found"
                    (:file-length (parser/parse "{:source-file \"/Notification.java\" :line 190 :line-length \"8\" :file-length \"97\"}")) => 97)

              (fact "defaults to 0"
                    (:file-length (parser/parse "{:source-file \"/Notification.java\" :line 190 :line-length \"13\"}")) => 0))

       (facts "the complexity"
              (fact "can be found"
                    (:complexity (parser/parse "{:source-file \"/Notification.java\" :line 190 :line-length \"8\" :complexity \"9\"}")) => 9)

              (fact "defaults to 0"
                    (:complexity (parser/parse "{:source-file \"/Notification.java\" :line 190 :line-length \"13\"}")) => 0))

       (facts "the metric-type"
              (fact "defaults to :regular"
                    (:type (parser/parse "{:source-file \"/Notification.java\" :line 190 :line-length \"13\"}")) => :regular)

              (fact "becomes :method when a method length metric is found"
                    (:type (parser/parse "{:source-file \"/Notification.java\" :line 190 :line-length \"13\" :method-length \"16\"}")) => :method)

              (fact "becomes :class when a class declaration line is found"
                    (:type (parser/parse "{:source-file \"/Notification.java\" :line 190 :line-length \"13\" :method-count \"9\"}")) => :class)

              (fact "becomes :source-file when a file declaration line is found"
                    (:type (parser/parse "{:source-file \"/Notification.java\" :line 190 :line-length \"13\" :file-length \"169\"}")) => :file))

       (facts "the method length"
              (fact "can be found"
                    (:method-length (parser/parse "{:source-file \"/Notification.java\" :line 190 :method-length \"16\" :line-length \"98\"}")) => 16)

              (fact "defaults to 0"
                    (:method-length (parser/parse "{:source-file \"/Notification.java\" :line 190 :line-length \"13\"}")) => 0
                    (:method-length (parser/parse "{:source-file \"/Notification.java\" :line 190 :line-length \"13\" :complexity \"9\"}")) => 0)))
