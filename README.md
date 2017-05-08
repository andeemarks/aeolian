# Aeolian

A (mostly) Clojure console application that injests processes Java source code through [Checkstyle][10] to produce source code quality metrics and creates a musical representation of these metrics using [ABC Notation][1] and ultimately [MIDI][3].

The name Aeolian (apart from being a Greek musical mode) refers to a leading company in the production of [player pianos][2], which takes punched paper as input and drives a mechanical piano.

Aeolian was presented to the [YOW West conference in May 2017][12].  The slides can be found [here][11].

[1]: http://abcnotation.com/
[2]: https://en.wikipedia.org/wiki/Player_piano
[3]: https://en.wikipedia.org/wiki/MIDI
[4]: http://checkstyle.sourceforge.net/config_metrics.html#CyclomaticComplexity
[5]: http://checkstyle.sourceforge.net/config_sizes.html#LineLength
[6]: http://www.harukizaemon.com/simian/installation.html#checkstyle
[7]: https://git-scm.com/docs/git-blame
[8]: http://ifdo.pugmarks.com/~seymour/runabc/top.html
[9]: http://timidity.sourceforge.net/
[10]: http://checkstyle.sourceforge.net/
[11]: https://docs.google.com/a/thoughtworks.com/presentation/d/1k8yWYMxy8dPU8AoxXZbIp3o77fbvcfC9naT7Yk6fOW8/edit?usp=sharing
[12]: http://west.yowconference.com.au/speakers/andy-marks-4/

## Metrics

Currently, Aeolian maps the following source code metrics to aspects of the generated music:

Metric 						| Affects... 										| By... 
---------------------------	| ------------------------------------------------- | --- 
[Line length][5] 			| Note and octave selection 						| Longer lines -> higher pitched notes 
[Cyclomatic Complexity][4]	| Tempo 											| Higher complexity -> faster tempo 
[Duplication][6] 			| Key (Major or Minor) 								| Duplication >= 10% -> minor key 
[Git commit author][7] 		| MIDI instrument (from 7 available instruments) 	| New instrument for each author change 
Source file name 			| Karaoke lyrics (if MIDI file is played through a player that supports the KAR format) | New filename lyric for each source file change 

Because there is one note played for each line of code processed, a larger source file will produce a longer song.  Likewise, more complex code will result in faster passages in the song, whilst less complex code will result in slower passages.

All generated songs use a default tempo of 140bpm and all notes played are eighth notes. 

Note: there are still lots of dimensions to code quality and song composition and structure which haven't been covered by Aeolian.  I give this to you as a starting point to build out more complex audio representations of code quality for your own edification. 

## Usage

Assuming you have a Github repo you want to point Aeolian at, then start with this:

```script
./go-gh.sh <github user name> <github repo name>
```

This script will perform a number of actions:

1. _Clone the repo locally._

	The repo will appear in a temporary directory underneath the Aeolian install dir (hereafter known as the $WORK_DIR).  For example:

	```
	$  ./go-gh.sh allegro grunt-maven-plugin
	Checking GitHub for repo 'grunt-maven-plugin' for user 'allegro'...
	Cloning repo to /home/amarks/Code/aeolian/tmp.YtpN0tR5yG...
	```

1. _Find the commit history for all the source files._

	Sample terminal output:

	```
	Find commit history for each Java file...
	```

	The output from this stage is a file called ```blames.txt``` in the $WORK_DIR.  The fields in this file are:

	Field 					| Position 	| Prefix
	----------------------- | ---------	| ---
	Source file 			| 1 		| None
	Line number 			| 2 		| #
	Git commit author 		| 3 		| AU=
	Git commit timestamp 	| 4 		| TS=

1. _Generate the metrics._

	Sample terminal output:

	```
	Generating Checkstyle duplication metrics...
	Running metrics on all Java files...
	Processing ExecNpmOfflineMojo...
	Generating Checkstyle cyclomatic complexity metrics...
	Generating Checkstyle line length metrics...
	Generating Checkstyle method length metrics...
	Generating Checkstyle indentation metrics...
	Processing AbstractExecutableMojo...
	Generating Checkstyle cyclomatic complexity metrics...
	Generating Checkstyle line length metrics...
	Generating Checkstyle method length metrics...
	Generating Checkstyle indentation metrics...
	Processing ExecNpmMojo...
	...
	Building uber metrics file...
	Joining metrics file with Git commit history...
	```

	The Checkstyle configuration files to generate the complexity metrics can be found [here](resources/) and the script in charge of invoking Checkstyle for each source file can be found [here](go.sh).

	The output from this stage is a file called ```$WORK_DIR\duplication.txt``` which contains the summary of the duplication checking on the repo.  Additional output is a number of temporary metrics files in $WORK_DIR for each source file processed.  Each of these files is named to correspond to the source file and suffixed by a short pseudo-random UID to prevent clashes of source file names across packages.

	All metrics for each source file are eventually combined into a file with a ```.metrics.all``` extension.  The fields in this file are:

	Field 					| Position 	| Prefix
	----------------------- | -------- 	| ---
	Source file 			| 1 		| None
	Line number 			| 2 		| # 
	Line length 			| N/A 		| LL=
	Cyclomatic Complexity 	| N/A 		| CC=
	Method length 			| N/A 		| ML=

	The final step in this stage is to join the Git blame and Checkstyle metrics files, producing a file with a ```.history``` extension.

1. _Generate the ABC Notation._

	Sample terminal output:

	```
	Generating ABC notation...
	Generated /home/amarks/Code/aeolian/tmp.YtpN0tR5yG/grunt-maven-plugin.metrics.history.abc
	```

	The generated ABC notation file will appear in $WORK_DIR with the extension ```.metrics.history.abc```.

1. _Generate a playable version of the ABC Notation._

	Aeolian was developed using [abcmidi][3] (available on OSX and Ubuntu).  abcmidi takes an ABC Notation file as input (foo.abc) and generates a MIDI file from the input file (foo1.mid) for use in the next stage of the pipeline.

	Sample terminal output:

	```
	Generating MIDI...
	3.90 September 25 2016 abc2midi
	writing MIDI file /home/amarks/Code/aeolian/tmp.YtpN0tR5yG/grunt-maven-plugin.metrics.mid
	```

	The generated MIDI file will appear in $WORK_DIR with the extension ```.metrics.mid```.

1. _Archive the generated ABC and MIDI files._

	Sample terminal output:

	```
	Archiving generated files..
	```

	Both the ABC and MIDI files will be copied to the [archive directory](archive/).

1. _Play the music._  

	Aeolian was developed using [timidity][4] (available on OSX and Ubuntu).

	Sample terminal output from Timidity.

	```
	Playing MIDI...
	Requested buffer size 32768, fragment size 8192
	ALSA pcm 'default' set buffer size 32768, period size 8192 bytes
	Playing /home/amarks/Code/aeolian/tmp.YtpN0tR5yG/grunt-maven-plugin.metrics.mid
	MIDI file: /home/amarks/Code/aeolian/tmp.YtpN0tR5yG/grunt-maven-plugin.metrics.mid
	Format: 1  Tracks: 3  Divisions: 480
	Text: @KMIDI KARAOKE FILE
	Sequence: /home/amarks/Code/aeolian/tmp.YtpN0tR5yG/grunt-maven-plugin.metrics.history
	Text: @I/home/amarks/Code/aeolian/tmp.YtpN0tR5yG/grunt-maven-plugin.metrics.history
	Text: @IAEOLIAN
	...	
	Playing time: ~160 seconds
	Notes cut: 0
	Notes lost totally: 0
	```

	Assuming all steps of the pipeline have been successful, you should now be listening to the quality of the specified code playing.

## License

Copyright © 2015-2017 ThoughtWorks

Distributed under the Eclipse Public License, the same as Clojure.
