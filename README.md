# Aeolian

A Clojure console application that injests (currently) a limited set of source code metrics and creates a musical representation of these metrics using [ABC Notation][1].

The name Aeolian (apart from being a Greek musical mode) refers to a leading company in the production of [player pianos][2], which takes punched paper as input and drives a mechanical piano.

## Metrics

Currently, Aeolian only cares about two code metrics; one is used to determine which __notes__ to play in the song and the second is used to determine __tempo__ at various stages during the song.  During development, these metrics were mapped to _line length_ (for note choice) and _method complexity_ (for tempo), although you could arbitrarily map any other metrics that make sense to you.

[1]: http://abcnotation.com/
[2]: https://en.wikipedia.org/wiki/Player_piano

Because there is one note played for each line of code processed, a larger source file will produce a longer song.  Likewise, more complex code will result in faster passages in the song, whilst less complex code will result in slower passages.

Note: there are still lots of dimensions to code quality and song composition and structure which haven't been covered by Aeolian.  I give this to you as a starting point to build out more complex audio representations of code quality for your own edification.

## Usage

1. _Generate the metrics._

	How the metrics are generated are less important than what format they are generated in.  For development purposes, I used [Checkstyle][5] plus some bash scripty-magic to create sample input files from a Java source file.  

	```
	# Generate some complexity metrics from Checkstyle
	java -jar /path/to/checkstyle.jar -c /path/to/checkstyle.xml Foo.java | grep "Cyclomatic Complexity" | awk '{print $1 " " $5}' | awk -F: '{print $2 " " $4}' > complexity.txt
	# Generate some line length metrics from awk
	cat Foo.java | awk '{print NR " " length($0)}' > line-lengths.txt
	# Merge the two datasets
	join -a1 <(sort line-lengths.txt) <(sort complexity.txt) > combined-metrics.txt
	```

	A sample Checkstyle to generate the complexity metrics shown about can be found [here](resources/checkstyle.xml).

	A sample Java source file (courtesy of [RXJava][6]) can be found [here](resources/Notification.java).

2. _Generate the ABC Notation._

	Aeolian takes a metric file as input (foo.txt) and generates an ABC Notation file from the input file (foo.abc) for use in the next stage of the pipeline.

	```
	lein run /path/to/combined-metrics.txt
	```

3. _Generate a playable version of the ABC Notation._

	Aeolian was developed using [abcmidi][3] (available via Homebrew on OSX).  abcmidi takes an ABC Notation file as input (foo.abc) and generates a MIDI file from the input file (foo1.mid) for use in the next stage of the pipeline.

	```
	abc2midi aeolian.abc
	```

4. _Play the music._  

	Aeolian was developed using [timidity][4] (available via Homebrew on OSX).

	```
	timidity aeolian1.mid
	```

	Assuming all steps of the pipeline have been successful, you should now be listening to the quality of your code playing.

## License

Copyright © 2015 ThoughtWorks

Distributed under the Eclipse Public License, the same as Clojure.

[3]: http://ifdo.pugmarks.com/~seymour/runabc/top.html
[4]: http://timidity.sourceforge.net/
[5]: http://checkstyle.sourceforge.net/
[6]: https://github.com/ReactiveX/RxJava
