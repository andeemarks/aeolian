(ns aeolian.abc.notes)

(def rest-note "z")
(def octave-1 ["C," "E," "G," "B,"])
(def octave-2 ["C" "E" "G" "B"])
(def octave-3 ["c" "e" "g" "b"])
(def octave-4 ["c'" "e'" "g'" "b'"])
(def octave-5 ["c''" "e''" "g''" "b''"])

(defn pick-chord-for-method-length [method-length]
	(cond
		(nil? method-length) "C"
	   	(<= 1 method-length 5) "C"
	   	(<= 6 method-length 10) "D"
	   	(<= 11 method-length 15) "E"
	   	(<= 16 method-length 20) "F"
	   	(<= 21 method-length 29) "G"
	   	(<= 30 method-length 39) "A"
	   	(<= 40 method-length) "B"
	   	:else "C"))

(defn pick-note-for-line-length [line-length]
	(cond
		(< line-length 10) rest-note
	   	(<= 10 line-length 39) (nth octave-1 (mod line-length (count octave-1))) 
	   	(<= 40 line-length 79) (nth octave-2 (mod line-length (count octave-2)))
	   	(<= 80 line-length 99) (nth octave-3 (mod line-length (count octave-3)))
	   	(<= 100 line-length 119) (nth octave-4 (mod line-length (count octave-4)))
	   	(<= 120 line-length) (nth octave-5 (mod line-length (count octave-5)))))