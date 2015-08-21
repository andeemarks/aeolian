# Aeolian

A Clojure console application that injests (currently) a limited set of source code metrics and creates a musical representation of these metrics using [ABC Notation][1].

The name, Aeolian, apart from being a Greek musical mode, refers to a leading company in the production of [player pianos][2], which takes punched paper as input and drives a mechanical piano.

[1]: http://abcnotation.com/
[2]: https://en.wikipedia.org/wiki/Player_piano

## Usage

1. Generate the metrics



2. Generate the ABC Notation

	lein run

3. Generate a playable version of the ABC Notation.  Aeolian was developed using [abcmidi][3] (available via Homebrew on OSX).

	abc2midi aeolian.abc

4. Play the music.  Aeolian was developed using [timidity][4] (available via Homebrew on OSX).

	timidity aeolian1.mid

## License

Copyright © 2015 ThoughtWorks

Distributed under the Eclipse Public License, the same as Clojure.

[3]: http://ifdo.pugmarks.com/~seymour/runabc/top.html
[4]: http://timidity.sourceforge.net/
