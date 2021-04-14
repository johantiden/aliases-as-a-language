#!/usr/bin/env bash


# This file is intended to implement the language.
# Use this file to hide ugly implementation details.
# Try to avoid using any of the language here.


#TODO Use error output or other. Can't pipe this to other commands.
function __debugecho {
  RED='\033[0;34m'
  NC='\033[0m' # No Color

	#echo -e ${RED}DEBUG: "${@}"${NC}
}


function __johan_aliases_merge {
    git merge --no-edit "${@}"
}


function __johan_aliases_open {
    xdg-open "${@}" &> /dev/null
}

function __johan_aliases_set_version {
    # TODO: not only for looklet
    # TODO: support for more languages, for now only maven
    __looklet_maven_version "${@}"
}

function __johan_aliases_get_all_branches {
    git branch --all | sed 's/[ \\*]*//g' | sed 's/remotes\/origin\///g' | grep -v HEAD | sort | uniq
}

function __johan_aliases_choose_branch {
    BRANCHES=`__johan_aliases_get_all_branches`
    echo "$BRANCHES" | fzf
}

function clipS { xclip -selection c; }
function clipL {
	xclip -selection c -o \
	    | xargs -0 echo
}

#https://gist.github.com/davejamesmiller/1965569#file-ask-sh
ask() {
    local prompt default reply

    if [[ ${2:-} = 'Y' ]]; then
        prompt='Y/n'
        default='Y'
    elif [[ ${2:-} = 'N' ]]; then
        prompt='y/N'
        default='N'
    else
        prompt='y/n'
        default=''
    fi

    while true; do

        # Ask the question (not using "read -p" as it uses stderr not stdout)
        echo -n "$1 [$prompt] "

        # Read the answer (use /dev/tty in case stdin is redirected from somewhere else)
        read -r reply </dev/tty

        # Default?
        if [[ -z $reply ]]; then
            reply=$default
        fi

        # Check if the reply is valid
        case "$reply" in
            Y*|y*) return 0 ;;
            N*|n*) return 1 ;;
        esac

    done
}

# Simple git checkout if argument applied, otherwise ask for branch.
function __johan_aliases_git_checkout_contextual {
    if [[ ! -z "${1}" ]] ; then
      git checkout "${1}"
    else
      if OUTPUT=`__johan_aliases_choose_branch`; then
        git checkout $OUTPUT;
      fi
    fi
}

# TODO: Depends on t_preview
function __johan_aliases_t_fuzzy {
    FILE_AND_LINE=$(ag --nobreak --noheading . ~ | fzf --preview='t_preview {}')
    if [[ -n "${FILE_AND_LINE}" ]]; then
        FILE=$(sed 's/:.*//g' <<< $FILE_AND_LINE)
        let LINE=$(sed -e 's/.*:\(.*\):.*/\1/' <<< $FILE_AND_LINE)

        if [[ ! -z "${FILE}" ]] ; then
            #vim +$LINE $FILE
            #vim "+normal $(LINE)G|" $FILE
            echo $FILE $LINE
        fi
    else
        echo abort
    fi
}

# TODO: De-duplicate from __johan_aliases_t_fuzzy
function __johan_aliases_t_exact {
    FILE_AND_LINE=$(ag --nobreak --noheading . ~ | fzf -e --preview='t_preview {}')
    if [[ -n "${FILE_AND_LINE}" ]]; then
        FILE=$(sed 's/:.*//g' <<< $FILE_AND_LINE)
        let LINE=$(sed -e 's/.*:\(.*\):.*/\1/' <<< $FILE_AND_LINE)

        if [[ ! -z "${FILE}" ]] ; then
            #vim +$LINE $FILE
            #vim "+normal $(LINE)G|" $FILE
            echo $FILE $LINE
        fi
    else
        echo abort
    fi
}


function __johan_aliases_get_version {
    # TODO: More than maven versions
    __johan_aliases_get_maven_version
}

function __johan_aliases_get_maven_version {
  # TODO: copy paste looklet solution to make this standalone
    __looklet_xpath_pom_version
}

function __johan_aliases_second {
    __johan_aliases_column 2
}

function __johan_aliases_column {
    cut -d' ' -f"$1"
}

#function __johan_aliases_private_choose_one_file_from {
#    find "$1" -print | fzf "${@:2}"
#}

function __johan_aliases_one {
    cat | fzf
}

function __johan_aliases_one_exact {
    cat | fzf -e
}

function __johan_aliases_list_all_files_from_home {
    find ~ -print
}

function __johan_aliases_choose_one_file_from_home {
    find ~ -print | fzf
}

function __johan_aliases_choose_one_file_from_home_exact {
    find ~ -print | fzf -e
}

function __johan_aliases_choose_one_file_from_root {
    local temp=$(sudo find / -print) # TODO: Terminating the pipe is very slow but sudo find is piped before password is written
    echo $temp | fzf
}

function __johan_aliases_choose_one_file_from_root_exact {
    local temp=$(sudo find / -print) # TODO: Terminating the pipe is very slow but sudo find is piped before password is written
    echo $temp | fzf -e
}

function __johan_aliases_git_commit {
    __looklet_git_commit "${@}"
}

function __johan_aliases_open_github {
    local repo="$2"
    if [[ -z "${repo}" ]]; then 
        repo=`pwd`
    fi
    local branch="$1"

    pushd "${repo}"
    local base_url=`__looklet_github_repository_url`
    popd
    
    local url="${base_url}/tree/${branch}"
    if [[ -z "${branch}" ]]; then
        url="${base_url}"
    fi

    __johan_aliases_open "${url}"
}

function __johan_aliases_z1t {
    __johan_aliases_t_fuzzy | sed -e "s/ /:/g" | xargs echo | xargs subl
}

function __johan_aliases_z1et {
    __johan_aliases_t_exact | sed -e "s/ /:/g" | xargs echo | xargs subl
}

function __johan_aliases_all_repos() {
  # TODO: ~/git is hardcoded. '~' would be nice as root but a lot of false positives pop up, and it's slow.
  find ~/git -type d -exec test -e '{}/.git' ';' -print -prune
	#find ~/git/looklet -maxdepth 1 -mindepth 1 -type d -printf '%f\n'
}

function _r() { #TODO: Rename
    __johan_aliases_all_repos | fzf
}
