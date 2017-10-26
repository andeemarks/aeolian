(ns aeolian.midi.instrument)

(def ^:const hi-hat "42")
(def ^:const clap "39")
(def ^:const stick "37")
(def ^:const beat stick)

(def ^:const four-beat-metro (str "%%MIDI drum dzdzdzdz " (apply str (repeat 4 (str beat " "))) "\n"))
(def ^:const no-accents "%%MIDI nobeataccents\n")
(def ^:const first-beat-emphasis "%%MIDI beatstring pppp\n")
(def ^:const header "%%MIDI gchord c\n")

(def ^:const piano 4)
(def ^:const marimba 13)
(def ^:const bass 35)
(def ^:const flute 73)
(def ^:const tuba 58)
(def ^:const trumpet 56)
(def ^:const cello 42)
(def ^:const guitar 26)
(def ^:const accordian 21)

(def instruments [piano marimba bass flute tuba trumpet cello guitar accordian])

(defn instrument-for [filename]
  (nth instruments
       (mod
        (apply + (map int filename))
        (count instruments))))

(defn instrument-command-for [filename]
  (str "[I: MIDI program " (instrument-for filename) "]"))

(defn volume-boost []
  (str "[I: MIDI volinc +50]"))
