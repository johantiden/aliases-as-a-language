#!/usr/bin/env bash

thisDirectory=$(dirname $0)
source $thisDirectory/impl.sh

export PATH=$thisDirectory/bin:$PATH
export PATH=$thisDirectory/generated:$PATH

# Verbs
#########################
alias b='__aaal_git_checkout_contextual '
alias c='__aaal_git_commit '
alias p='ep'
alias y='__aaal_clip_save'
alias v='__aaal_set_version '

# Nouns
#########################
alias 1d='cd `e1d`'
alias ed='cd `eed`'
alias 1D='cd `e1D`'
alias d1f='cd `ed1f`'
alias d1F='cd `ed1F`'
alias f='1f'
alias 1f='e1f'
alias ef='eef'
alias 1F='e1F'
alias eF='eeF'
alias 1t='e1t'
alias et='eet'


# Special
#########################
alias 1='fzf '
alias x1='dmenu '


# Assumptions from context
#########################
alias 1b='g1b'
alias t='e1t'
alias et='eet'
alias ev='e.v'

# Sentences
#########################
### Branch
alias bd='b develop'							# branch develop
alias bm='b `__looklet_git_main_branch`'		# branch main/master


alias bp='b $(p)'								# branch pasting
alias b1b='b `e1b`'								# branch one branch

### Commit
alias gcp='c $(p)'								# commit pasting (as message)

# echo one file from /
alias 1F='__aaal_choose_one_file_from_root'

# echo one exact file from /
alias eF='__aaal_choose_one_file_from_root_exact'

alias 1d='find ~ -type d | fzf'				# echo dir from ~
alias ed='find ~ -type d | fzf -e'			# echo dir from ~, exact
alias e1D='sudo find / -type d | fzf'			# echo dir from /
alias e1t='__aaal_t_fuzzy'						# echo file and row
alias et='__aaal_t_exact'						# echo file and row, exact

alias ed1f='dirname `e1f`'						# echo dir of one file from ~
alias ed1F='dirname `e1F`'						# echo dir of one file from /
alias e.d='pwd'
alias ep='__aaal_clip_load'								# echo pasting
alias e.v='__aaal_get_version ' 	    		# echo this version
alias .v='e.v ' 	    		# echo this version
alias e.r='__looklet_github_repository_name'
alias .r='e.r'


### Get
alias g1d='cd `e1d`' 		#get one dir
alias gd='bd' 				#get develop
alias gg='git pull' 		#get git
alias gm='bm' 				#get master
alias g1b='b `e1b`' 		#get one branch (checkout)
alias gb='git co -'	 		#get "back" 

### Merge
alias m='merge '
alias mb='m1b'							#merge branch
alias md='m develop'					#merge develop
alias m1b='git merge --no-edit `e1b`'	#merge one branch
alias mp='git merge --no-edit `p`' 		#merge pasting

### New
alias np='n $(p)'  			#new-branch pasting

### Open
alias o.b='__aaal_open_github `e.b`' #open this branch (on github)
alias ob='o.b' 								  #open this branch (on github)
alias o.r='o.b'                               #open this repo (on github)
alias or='o.r'                                #open this repo (on github)
alias o1b='__aaal_open_github `e1b`' #open one branch (on github)
alias o1r='__aaal_open_github "" `1r`' #open one branch (on github)

alias o1d='o `e1d`' 			#open one dir from ~
alias o.d='o .'				    #open this dir
alias o.='o .'				    #open this dir
alias oD='o `e1D`'				#open one dir from /
alias o1f='o `e1f`'				#open one file from ~
alias oef='o `eef`'			#open one file from ~, exact
alias o1F='o `e1F`'				#open one file from /
alias oeF='o `eeF`'			#open one file from /, exact
alias of='o1f'					#open one file from ~
alias og='pr'					#open git

# conflict with one password :(
#alias op='o $(p)'				#open pasting

### "See"

# See pasting
alias c1f='cccat `e1f`'

### Yank
alias yb='y.b' 										# yank branch
alias y.b='e.b | y' 								# yank this branch
alias y1b='e1b | y' 								# yank this branch
alias y1d='e1d | y' 								# yank one dir
alias y.d='e.d | y' 								# yank this dir
alias y1f='1f | y' 									# yank file
alias y.v='e.v | y' 								# yank this version
alias yv='y.v' 										# yank this version
alias y.c='e.c | y' 								# yank this commit message
alias y.r='e.r | y' 								# yank this repo name
alias yr='y.r'

### Sublime
alias z1f='z `e1f`' # sublime one file from ~
alias z1F='z `e1F`' # sublime one file from /
alias Z1f='Z `e1f`' # sudo sublime one file from ~
alias Z1F='Z `e1F`' # sudo sublime one file from /

alias zef='z `eef`' # sublime one file from ~
alias zeF='z `eeF`' # sublime one file from /
alias Zef='Z `eef`' # sudo sublime one file from ~
alias ZeF='Z `eeF`' # sudo sublime one file from /

alias z1t='__aaal_z1t'
alias zet='__aaal_zet'

alias zp='z `p`' # sublime pasting
alias Zp='Z `p`' # sudo sublime pasting

alias x='z -nw'


## TODO

alias r='. __aaal_r_sourceme'
alias dt='git difftool'
alias s='git status'
alias d='git --no-pager diff'
alias .d='pwd'
alias mt='git mt '
alias hard='git reset --hard'
alias hardo='git reset --hard origin/`e.b`'


alias dp='depend `p`' # Depend on pasting, hopefully a repo name
alias bb='git co -' # branch back
alias gp='git push -u' # git push

alias dto='dt origin/`.b`'
alias dod='d origin/develop'
alias dtod='dt origin/develop'
alias gs='git status'


alias rp='r $(p)'					#repo pasting
