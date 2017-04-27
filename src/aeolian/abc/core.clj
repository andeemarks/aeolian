(ns aeolian.abc.core)

(defn inline [command]
	(str "[" command "]"))

(defn measure [command]
	(str "| " command " |\n"))