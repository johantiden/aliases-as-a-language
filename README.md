# aliases-as-a-language

This is an attempt to create a language for common (for me) terminal aliases. This is inspired by the language used in VIM. The main idea is to have a simple VERB-NOUN grammar for most actions, this allows the user to dynamically compose commands and helps create completeness to the alias suite. It's simpler to learn the aliases once you have leared the available words. Composing sentences should come naturally after a while.

THIS IS VERY MUCH A WORK IN PROGRESS!!!!
HELP WANTED!


##########################
# Language
##########################


##########################
# Verbs:
##########################
# b		(go)branch
# c     commit
# e		echo
# g		go (contextual)
# m     git-merge
# o     open
# r     remove (contextual)
# v 	version
# y     yank (as in vim)
# z     sublime
# Z     sudo sublime
# .n    git new


##########################
# Nouns
#########################
# 1b	one branch
# .b	this branch

# .d	this dir
# 1d	one dir (from home)
# 1D	one dir (from /)
# 1.d	one dir (from .d)
# d1f	dir of one file
# d1F	sudo dir of one file

# 1f 	one file
# 1F 	sudo one file

# p     pasting

# d    develop
# m    master
# .v    this version
# .c    this commit (message)

# Special
#########################
# 1     one
alias 1='fzf '
alias x1='dmenu '

# .     this
	# eg. y.d "yank this dir"

# Adjectives
#########################
# j 	jira as an adjective
	# oj.v     "open jira, this-version"
	# njp      "new branch, jira-pasting"




#########################
# sentences
#########################
# Grammar:
# Verb alone expects an argument eg "e hej"

# Behavior from noun alone depends on context. 
	# `f` will will echo the file. 
	# `.d` will change branch to develop.

# VERB-NOUN sentence should give the noun as an argument to the verb. 
	# `of` open file
# VERB-ADJECTIVE-NOUN should act as VERB-NOUN, the ADJECTIVE should modify the NOUN

# NOTE: SOME VERBS ARE ALSO NOUNS! 
	# e.g. `cp` will commit with the clipboard contents as message. 
	# `yc` will yank the latest commit message. 
