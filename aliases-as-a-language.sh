#/usr/bin/env bash

thisDirectory=$(dirname $0)
source $thisDirectory/impl.sh

export PATH=$thisDirectory/bin:$PATH

# Verbs
#########################
alias b='__johan_aliases_git_checkout_contextual '
alias c='__johan_aliases_git_commit '
alias p='ep'
alias y='clipS'
alias v='__johan_aliases_set_version '

# Nouns
#########################
alias 1d='cd `e1d`'
alias 1ed='cd `e1ed`'
alias 1D='cd `e1D`'
alias d1f='cd `ed1f`'
alias d1F='cd `ed1F`'
alias f='1f'
alias 1f='e1f'
alias 1ef='e1ef'
alias 1F='e1F'
alias 1eF='e1eF'
alias 1t='e1t'
alias 1et='e1et'


# Special
#########################
alias 1='fzf '
alias x1='dmenu '


# Assumptions from context
#########################
alias 1b='g1b'
alias t='e1t'
alias et='e1et'
alias ev='e.v'

# Sentences
#########################
### Branch
alias bd='b develop'							# branch develop
alias bm='b master'								# branch master
alias bp='b $(p)'								# branch pasting
alias b1b='b `e1b`'								# branch one branch

### Commit
alias gcp='c $(p)'								# commit pasting (as message)

### Echo
alias e1b='__johan_aliases_choose_branch'				# echo branch
alias e.b='git branch --show-current '  		# echo this branch
alias eb='e.b'				# echo branch
alias .b='e.b'  								# echo this branch
alias e.c='git --no-pager log -n1 --pretty=format:"%s"'	# echo this commit message

alias e1f='__johan_aliases_choose_one_file_from_home '	# echo one file from ~
alias ef='e1f'

# echo one exact file from ~
alias e1ef='__johan_aliases_choose_one_file_from_home_exact'

# echo one file from /
alias e1F='__johan_aliases_choose_one_file_from_root'

# echo one exact file from /
alias e1eF='__johan_aliases_choose_one_file_from_root_exact'

alias e1d='find ~ -type d | fzf'				# echo dir from ~
alias e1ed='find ~ -type d | fzf -e'			# echo dir from ~, exact
alias e1D='sudo find / -type d | fzf'			# echo dir from /
alias e1t='__johan_aliases_t_fuzzy'						# echo file and row
alias e1et='__johan_aliases_t_exact'						# echo file and row, exact

alias ed1f='dirname `e1f`'						# echo dir of one file from ~
alias ed1F='dirname `e1F`'						# echo dir of one file from /
alias e.d='pwd'
alias ep='clipL'								# echo pasting
alias e.v='__johan_aliases_get_version ' 	    		# echo this version
alias e.r='__looklet_github_repository_name'
alias .r='e.r'


### Get
alias g='gg'				#get
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
alias o.b='__johan_aliases_open_github `e.b`' #open this branch (on github)
alias ob='o.b' 								  #open this branch (on github)
alias o.r='o.b'                               #open this repo (on github)
alias or='o.r'                                #open this repo (on github)
alias o1b='__johan_aliases_open_github `e1b`' #open one branch (on github)
alias o1r='__johan_aliases_open_github "" `1r`' #open one branch (on github)

alias o1d='o `e1d`' 			#open one dir from ~
alias o.d='o .'				    #open this dir
alias o.='o .'				    #open this dir
alias oD='o `e1D`'				#open one dir from /
alias o1f='o `e1f`'				#open one file from ~
alias o1ef='o `e1ef`'			#open one file from ~, exact
alias o1F='o `e1F`'				#open one file from /
alias o1eF='o `e1eF`'			#open one file from /, exact
alias of='o1f'					#open one file from ~
alias og='pr'					#open git
alias op='o $(p)'				#open pasting

### "See"

# See pasting
alias catp='johan_view_pasting'
alias c1f='cccat `e1f`'


### Remove
alias rp='brm $(p)'					#remove-branch pasting
alias r1b='brm $(e1b)' 	#remove one branch
alias r.b='echo not implemented' 	#remove this branch
alias r.d='echo not implemented'	#remove this dir
alias r1d='echo not implemented'	#remove one dir
alias r1D='echo not implemented' 	#remove one sudo dir
alias r1.d='echo not implemented'	#remove this dir
alias rd1f='echo not implemented'	#remove dir of one file
alias rd1F='echo not implemented'	#remove dir of one sudo file
alias r1F='rm $(e1F)'				#remove one sudo file

alias rd='echo remove develop not allowed'
alias r.v='echo remove this version does not make sense'
alias r.c='echo not implemented. hmm maybe' # Maybe "git reset HEAD^" ?


### Up
alias u='git push'					#up
alias ug='git push'					#up git


### Version
alias vp='v $(p)' # version pasting


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

alias zef='z1ef'      # sublime one file from ~
alias z1ef='z `e1ef`' # sublime one file from ~
alias z1eF='z `e1eF`' # sublime one file from /
alias Z1ef='Z `e1ef`' # sudo sublime one file from ~
alias Z1eF='Z `e1eF`' # sudo sublime one file from /

alias z1t='__johan_aliases_z1t'
alias zet='z1et'
alias z1et='__johan_aliases_z1et'

alias zp='z `p`' # sublime pasting
alias Zp='Z `p`' # sudo sublime pasting



## TODO

alias dt='git difftool'
alias s='git status'
alias st='git status'
alias d='git --no-pager diff'
alias mt='git mt '
alias hard='git reset --hard'
alias hardo='git reset --hard origin/`e.b`'


alias dp='depend `p`' # Depend on pasting, hopefully a repo name
alias bb='git co -' # branch back
alias gp='git push -u' # git push

alias dto='dt origin/`e.b`'
alias gs='git status'