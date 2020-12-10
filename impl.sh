#/usr/bin/env bash


# This file is intended to implement the language.
# Use this file to hide ugly implementation details.
# Try to avoid using any of the language here.


function __impl_merge {
    git merge --no-edit "${@}"
}


function __impl_open {
    xdg-open "${@}"
}

function __impl_set_version {
    # TODO: not only for looklet
    # TODO: support for more languages, for now only maven
    __looklet_maven_version
}

function __impl_get_all_branches {
    git branch --all | sed 's/[ \\*]*//g' | sed 's/remotes\/origin\///g' | grep -v HEAD | sort | uniq
}

function __impl_choose_branch {
    BRANCHES=`__impl_get_all_branches`
    echo "$BRANCHES" | fzf
}

function clipS { xclip -selection c; }
function clipL { xclip -selection c -o | cat; }


# Simple git checkout if argument applied, otherwise ask for branch.
function __impl_git_checkout_contextual {
    if [[ ! -z "${1}" ]] ; then
      git checkout "${1}"
    else
      if OUTPUT=`__impl_choose_branch`; then
        git checkout $OUTPUT;
      fi
    fi
}

# TODO: Depends on t_preview
function __impl_t_fuzzy {
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
        #echo abort
    fi
}

# TODO: De-duplicate from __impl_t_fuzzy
function __impl_t_exact {
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
        #echo abort
    fi
}


function __impl_get_version {
    # TODO: More than maven versions
    __impl_get_maven_version
}

function __impl_get_maven_version {
    __looklet_xpath_pom_version
}

#function __impl_private_choose_one_file_from {
#    find "$1" -print | fzf "${@:2}"
#}

function __impl_one {
    cat | fzf
}

function __impl_one_exact {
    cat | fzf -e
}

function __impl_list_all_files_from_home {
    find ~ -print
}

function __impl_choose_one_file_from_home {
    find ~ -print | fzf
}

function __impl_choose_one_file_from_home_exact {
    find ~ -print | fzf -e
}

function __impl_choose_one_file_from_root {
    local temp=$(sudo find / -print) # TODO: Terminating the pipe is very slow but sudo find is piped before password is written
    echo $temp | fzf
}

function __impl_choose_one_file_from_root_exact {
    local temp=$(sudo find / -print) # TODO: Terminating the pipe is very slow but sudo find is piped before password is written
    echo $temp | fzf -e
}

function __impl_git_commit {
    __looklet_git_commit "${@}"
}

function __impl_open_github {
    __looklet_open_pr
}

function __impl_z1t {
    __impl_t_fuzzy | sed -e "s/ /:/g" | xargs echo | xargs subl
}

function __impl_z1et {
    __impl_t_exact | sed -e "s/ /:/g" | xargs echo | xargs subl
}
