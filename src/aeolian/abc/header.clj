(ns aeolian.abc.header)

(def reference "X:1\n")
(def title-prefix "T:")
(def composer "C:AEOLIAN\n")
(def meter "M:4/4\n")
(def key-signature "K:C\n")
(def note-length "L:1/4\n")

(defn build-header [title]
	(str reference title-prefix title "\n" composer meter note-length key-signature))
