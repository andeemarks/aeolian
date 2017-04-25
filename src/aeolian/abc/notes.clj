(ns aeolian.abc.notes)

(def rest-note "z")
(def octave-1 ["C," "E," "G," "B,"])
(def octave-2 ["C" "E" "G" "B"])
(def octave-3 ["c" "e" "g" "b"])
(def octave-4 ["c'" "e'" "g'" "b'"])
(def octave-5 ["c''" "e''" "g''" "b''"])
(def or-chords ["\"C\"" "\"F\"" "\"G\"" "\"Am\""])
(def or-root "\"Am\"")

(defn pick-chord-for-method-length [method-length]
	(if (nil? method-length)
		"C"
		(cond
		   (<= 1 method-length 5) "C"
		   (<= 6 method-length 10) "D"
		   (<= 11 method-length 15) "E"
		   (<= 16 method-length 20) "F"
		   (<= 21 method-length 29) "G"
		   (<= 30 method-length 39) "A"
		   (<= 40 method-length) "B"
		   :else "C")))

(defn pick-octave-for-line-length [line-length]
	(cond
	   (<= 10 line-length 39) octave-1
	   (<= 40 line-length 79) octave-2
	   (<= 80 line-length 99) octave-3
	   (<= 100 line-length 119) octave-4
	   (<= 120 line-length) octave-5))