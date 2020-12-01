# aliases-as-a-language

This is an attempt to create a language for common (for me) terminal aliases. This is inspired by the language used in VIM. The main idea is to have a simple VERB-NOUN grammar for most actions, this allows the user to dynamically compose commands and helps create completeness to the alias suite. It's simpler to learn the aliases once you have leared the available words. Composing sentences should come naturally after a while.

THIS IS VERY MUCH A WORK IN PROGRESS!!!!
HELP WANTED!


# Usage
Clone this repository and source the `aliases-as-a-language` file into you .bashrc or similar.


# Language
## Verbs:
* `b`     (go)branch
* `c`     commit
* `e`     echo
* `g`     go (contextual)
* `m`     git-merge
* `o`     open
* `r`     remove (contextual)
* `v`     version
* `y`     yank (as in vim)
* `z`     sublime
* `Z`     sudo sublime
* `.n`    git new

## Nouns
* `1b` one branch
* `.b`    this branch

* `.d`    this dir
* `1d`    one dir (from home)
* `1D`    one dir (from /)
* `1.d`    one dir (from .d)
* `d1f`    dir of one file        (kind of breaks grammar)
* `d1F`    sudo dir of one file   (kind of breaks grammar)

* `1f`     one file
* `1F`     sudo one file

* `p`     pasting

* `d`    develop
* `m`    master
* `.v`    this version
* `.c`    this commit (message)
* `1t`	one text row (search)

## Special
* `1`     one - One should be selected. Where to choose from depends on context. `1f` one file chooses files from all sub folders of the user home folder. `1F` chooses a file from all files on the computer. Note that I chose not to use the local directory because the use for that is diminished since you can probably find the file easily anyway.
    * e.g. `e1f` "echo one file"

* `.`     this - Determines which item from context in contrast to `1` which needs user 
    * e.g. `y.d` "yank this dir"

## Adjectives
* `j`     jira as an adjective
    * `oj.v`     "open jira, this-version"
    * `njp`      "new branch, jira-pasting"
* `g`     git as an adjective
    * `og1v`        "open a git version" - open here, to mean open github. git can usually be derived from context so it may now be needed. "e.v" - echo this version can be safely assumed to be a git version, so it should behave the same as "eg.v"
* `e` 	exact. this instructs searches to use exact matching isntead of fuzzy search, which is defautl
	* e.g. `o1ef` "open one exact file"

# Grammar
* Verb alone expects an argument eg "e hej"

* Behavior from noun alone depends on context. 
    *`f` will will echo the file. 
    *`1d` will cd to a directory.

* Some verbs are contextual.
    * e.g. `r`. `r.b` remove this branch

## Building sentences
*VERB-NOUN sentence should give the noun as an argument to the verb. Note that the verb always comes before the noun.
    * e.g. `of` open file

*VERB-ADJECTIVE-NOUN should act as VERB-NOUN, the ADJECTIVE give some context to the sentence.
    * e.g. `og1f` open one git file - This could be interpreted as to open the chosen file on github.

*NOTE: SOME VERBS ARE ALSO NOUNS! 
    *`cp` will commit with the clipboard contents as message. 
    *`yc` will yank the latest commit message. 
