# TODO

* MVC layers
* Client
  * Multithreaded user interface
  * Informative user interface, display current state etc.
* Server
  * Respond with:
    * Numbers of letters in the word
    * Correctly guessed letters
    * Number of remaining attempts
    * Total score
  * Handle multiple clients
* Blocking TCP communication (???)

# API

## From client

### Guess char
`type=guess_char;char=a;`

### Guess word
`type=guess_word;word=hangman;`

### Start
`type=start_game;`

## From server

### State
`type=state;letters=_a__ma_;remaining_attempts=5;score=0`


## Example

```
client: type=start_game;
server: type=state;letters=_____;remaining_attempts=5;score=0;

client: type=guess_char;char=a;
server: type=state;letters=_____;remaining_attempts=4;score=0;

client: type=guess_char;char=o;
server: type=state;letters=____o;remaining_attempts=4;score=0;

client: type=guess_char;char=l;
server: type=state;letters=__llo;remaining_attempts=4;score=0;

client: type=guess_char;char=y;
server: type=state;letters=__llo;remaining_attempts=3;score=0;

client: type=guess_char;char=h;
server: type=state;letters=h_llo;remaining_attempts=3;score=0;

client: type=guess_char;char=e;
server: type=state;letters=hello;remaining_attempts=3;score=1;

client: type=start_game;
server: type=state;letters=___;remaining_attempts=3;score=1;

client: type=guess_char;char=e;
server: type=state;letters=___;remaining_attempts=2;score=1;

client: type=guess_char;char=a;
server: type=state;letters=___;remaining_attempts=1;score=1;

client: type=guess_char;char=x;
server: type=state;letters=___;remaining_attempts=0;score=1;
```