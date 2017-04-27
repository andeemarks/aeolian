(ns aeolian.midi.core)

(def hi-hat "42")
(def clap "39")
(def stick "37")
(def beat stick)

(def four-beat-metro (str "%%MIDI drum dzdzdzdz " beat " " beat " " beat " " beat " ""\n"))
(def no-accents "%%MIDI nobeataccents\n")
(def first-beat-emphasis "%%MIDI beatstring pmmm\n")
(def starting-instrument "%%MIDI program 32\n")
(def header (str starting-instrument first-beat-emphasis four-beat-metro no-accents "%%MIDI drumon\n%%MIDI gchord c\n"))

(def flute 73)
(def voice 53)
(def tuba 58)
(def trumpet 56)
(def cello 42)
(def guitar 26)
(def accordian 21)

(def instruments [flute voice tuba trumpet cello guitar accordian])

(defn instrument-for [filename]
	(nth instruments 
		(mod 
			(apply + (map int filename)) 
			(count instruments))))

(defn instrument-command-for [filename]
	(str "[I: MIDI program " (instrument-for filename) "]"))

(defn volume-for [method-length]
	"%%MIDI control 7 50")
