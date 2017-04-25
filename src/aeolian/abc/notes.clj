(ns aeolian.abc.notes)

(def rest-note "z")
(def octave-1 ["C," "E," "G," "B,"])
(def octave-2 ["C" "E" "G" "B"])
(def octave-3 ["c" "e" "g" "b"])
(def octave-4 ["c'" "e'" "g'" "b'"])
(def octave-5 ["c''" "e''" "g''" "b''"])
(def major-chords ["\"C\"" "\"F\"" "\"G\"" "\"Am\""])
(def major-root "\"Am\"")

(defn pick-chord-for-method-length [method-length]
	(cond
	   (<= 1 method-length 5) "Cmaj"
	   (<= 6 method-length 10) "Dmaj"
	   (<= 11 method-length 15) "Emaj"
	   (<= 16 method-length 20) "Fmaj"
	   (<= 21 method-length 29) "Gmaj"
	   (<= 30 method-length 39) "Amaj"
	   (<= 40 method-length) "Bmaj")
	)

(defn pick-octave-for-line-length [line-length]
	(cond
	   (<= 10 line-length 39) octave-1
	   (<= 40 line-length 79) octave-2
	   (<= 80 line-length 99) octave-3
	   (<= 100 line-length 119) octave-4
	   (<= 120 line-length) octave-5))