# aliases-as-a-language


This is an attempt to create a language for common (for me) terminal aliases. This is inspired by the language system used in VIM. The main idea is to have a simple VERB-NOUN grammar for most actions, this allows the user to dynamically compose commands and helps create completeness to the alias suite. It's simpler to learn the aliases once you have leared the available words. Composing sentences should come naturally after a while.

THIS IS VERY MUCH A WORK IN PROGRESS!!!!

HELP WANTED!

A cool name is needed to make this go viral! Maybe vish - vi-like shell?

# Installation

- Clone this repository
- source the `aliases-as-a-language.sh` file into you .bashrc or similar.

e.g.
`[ -f ~/git/aliases-as-a-language/aliases-as-a-language.sh ] && source ~/git/aliases-as-a-language/aliases-as-a-language.sh
`

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
* `z`     open editor
* `x`     open editor (gui)
* `n`    git new

## Nouns
* `b` branch
* `c` commit (message)
* `d` directory
* `d` develop (only contextual)
* `f` file (search under ~)
* `m` main branch (only contextual)
* `p` pasting (like vim)
* `r` (git) repo
* `t` text row (search under ~)
* `v` version

## Special
* `1` one - One should be selected. Where to choose from depends on context, especially the NOUN. `1f` one file chooses files from all sub folders of the user home folder. `1F` chooses a file from all files on the computer. Note that I chose not to use the local directory because the use for that is diminished since you can probably find the file easily anyway.
    * e.g. `e1f` "echo one file"

* `.` local - Determines which item from context in contrast to `1` which needs user 
    * `y.d` "yank this dir"
    * `.t` text row (search in this directory .)
    * `.r` "this repo"

* Combining `1.` search here. 
    * `1.f` one local file (search under .) 

* `d1f`   dir of one file        (kind of breaks grammar)
* `d1F`   sudo dir of one file   (kind of breaks grammar)

## Context
Context can be given at the beginning of the sentence or before a noun. Usage before a 

* `j`     jira
    * `jo.v`     "jira: open this version"
    * `njp`      "new branch, jira-pasting"


* `g`     git
    * `go1v`        "git: open a version" - open to mean open github. git can usually be derived from context, so it may not be needed. e.v" - echo this version can be safely assumed to be a git version, so it should behave the same as "eg.v"


* `e` exact. this instructs searches to use exact matching instead of fuzzy search. fuzzy is default
    * `o1ef` "open one exact file"


* `s` sudo. This will typically change the context from "home" to "everywhere".
    * `se1f`  "sudo: echo one file (from `/`)" - This is to distinguish from the default `e1f` which means "echo one file from `~`" 


# Grammar
* Verb alone expects an argument eg "e hej"

* Behavior from noun alone depends on context. 
    *`f` will echo the file. 
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

# Contributing

You can check out the code and build it yourself. The build system is a little java program designed to generate a bunch of commands that will be put on the PATH.

## Prerequisistes
- Java 17
- Maven

Run the generator by executing the java class predefined in the pom file:
```
mvn clean compile exec:java
```
This will replace all the files in the `./generated/` folder. Both the code changes and the newly generated commands should be committed to git.

This repo is meant to be heavily changed to personal taste using forks but if you think your changes are of value here, please make a Pull Request!
