(ns aeolian.abc.notes)

(def rest-note "z")
(def major-raw-notes ["C" "E" "G" "B"])
(def major-octave-1 (map #(str %1 ",") major-raw-notes))
(def major-octave-2 major-raw-notes)
(def major-octave-3 (map clojure.string/lower-case major-raw-notes))
(def major-octave-4 (map #(str %1 "'") major-octave-3))
(def major-octave-5 (map #(str %1 "'") major-octave-4))

(defn pick-chord-for-method-length [method-length composition-key]
	(cond
		(nil? method-length) "\"C\""
	   	(<= 1 method-length 5) "\"C\""
	   	(<= 6 method-length 10) "\"D\""
	   	(<= 11 method-length 15) "\"E\""
	   	(<= 16 method-length 20) "\"F\""
	   	(<= 21 method-length 29) "\"G\""
	   	(<= 30 method-length 39) "\"A\""
	   	(<= 40 method-length) "\"B\""
	   	:else "\"C\""))

(defn- pick-note-from-octave [octave line-length]
	(nth octave (mod line-length (count octave))))

(defn pick-note-for-line-length [line-length]
	(cond
		(< line-length 10) (pick-note-from-octave major-octave-1 line-length)
	   	(<= 10 line-length 39) (pick-note-from-octave major-octave-2 line-length) 
	   	(<= 40 line-length 79) (pick-note-from-octave major-octave-3 line-length)
	   	(<= 80 line-length 99) (pick-note-from-octave major-octave-4 line-length)
	   	(<= 100 line-length) (pick-note-from-octave major-octave-5 line-length)))