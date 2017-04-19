(ns aeolian.abc.header)

(def reference "X:1\n")
(def title "T:AEOLIAN\n")
(def composer "C:Andy Marks\n")
(def meter "M:4/4\n")
(def key-signature "K:C\n")
(def note-length "L:1/4\n")
(def header (str reference title composer meter note-length key-signature))
