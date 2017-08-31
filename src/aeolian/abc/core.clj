(ns aeolian.abc.core)

(defn inline [command]
  (str "[" command "]"))

(defn measure [chord notes]
  (str "| " chord (apply str notes) " |\n"))

(defn lyrics-for [lyric]
  (str "\nw: " lyric "\n"))

(defn note [note instrument lyrics tempo]
  (let [note-components (conj '() note instrument lyrics tempo)]
    (apply str (interpose " " (filter #(not (nil? %)) note-components)))))
