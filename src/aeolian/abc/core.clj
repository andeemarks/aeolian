(ns aeolian.abc.core)

(defn inline [command]
  (str "[" command "]"))

(defn measure [chord notes]
  (str "| " chord (apply str notes) " |\n"))

(defn lyrics-for [lyric]
  (str "\nw: " lyric "\n"))

(defn note [note length instrument lyrics tempo]
  (let [note-components (list note instrument lyrics tempo)]
    (apply str (interpose " " (remove nil? note-components)))))
