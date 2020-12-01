#/usr/bin/env bash


# This file is intended to implement the language. 
# Use this file to hide ugly implementation details. 
# Try to avoid using any of the language here. 




function __impl_merge() {
	git merge --no-edit 
}


function __impl_open() {
	xdg-open "${@}"
}

function __impl_set_version() {
	# TODO: not only for looklet
	# TODO: support for more languages, for now only maven 
	__looklet_maven_version
}

function __impl_get_all_branches() {
    git branch --all | sed 's/[ \\*]*//g' | sed 's/remotes\/origin\///g' | grep -v HEAD | sort | uniq
}

function __impl_choose_branch() {
	BRANCHES=`__impl_get_all_branches`
	echo "$BRANCHES" | fzf
}

function clipS() { xclip -selection c; }
function clipL() { xclip -selection c -o | cat; }


function __johan_b {
    if [[ ! -z "${1}" ]] ; then
      git checkout "${1}"
    else
      if OUTPUT=`__impl_choose_branch`; then
        git checkout $OUTPUT;
      fi
    fi
}

# TODO: Depends on t_preview
function __impl_t() {
    OFFSET="${1}"
    if [[ -z "${OFFSET}" ]] ; then
        OFFSET = 0
    fi

    FILE_AND_LINE=$(ag --nobreak --noheading . | fzf --preview='t_preview {}')
    FILE=$(sed 's/:.*//g' <<< $FILE_AND_LINE)
    LINE=$(sed -e 's/.*:\(.*\):.*/\1/' <<< $FILE_AND_LINE)
    LINE=$LINE+$OFFSET
    if [[ ! -z "${FILE}" ]] ; then
        #vim +$LINE $FILE
        vim "+normal $(LINE)G|" $FILE
    fi
}

# TODO: De-duplicate from __impl_t
function __impl_t_exact() {
    OFFSET="${1}"
    if [[ -z "${OFFSET}" ]] ; then
        OFFSET = 0
    fi

    FILE_AND_LINE=$(ag --nobreak --noheading . | fzf -e --preview='t_preview {}')
    FILE=$(sed 's/:.*//g' <<< $FILE_AND_LINE)
    LINE=$(sed -e 's/.*:\(.*\):.*/\1/' <<< $FILE_AND_LINE)
    LINE=$LINE+$OFFSET
    if [[ ! -z "${FILE}" ]] ; then
        #vim +$LINE $FILE
        vim "+normal $(LINE)G|ww|dit|" $FILE
    fi
}


function __impl_get_version() {
	# TODO: More than maven versions
	__impl_get_maven_version
}

function __impl_get_maven_version() {
    local pom_path="${pwd}/pom.xml"
    if [[ -f "${pom_path}" ]] ; then
        local version=$(/usr/bin/xmllint --format "${pom_path}" 2> /dev/null | /usr/bin/sed '2 s/xmlns=".*"//g' | /usr/bin/xmllint --xpath '//project/version/text()' - 2> /dev/null)
        if [[ -z "${version}" ]] ; then
            version=$(/usr/bin/xmllint --format "${pom_path}" 2> /dev/null | /usr/bin/sed '2 s/xmlns=".*"//g' | /usr/bin/xmllint --xpath '//project/parent/version/text()' - 2> /dev/null)'*'
        fi
        if [[ -z "${version}" ]] ; then
            return 0
        fi
        if [[ "${directory_original}" != "${pwd}" ]] ; then
            version="${version}^"
        fi
        echo "${version##\$}"
        return 0
    else
	    >&2 echo "File not found $pom_path"
    fi
}



#function __impl_private_choose_one_file_from() {
#	find "$1" -print | fzf "${@:2}"
#}

function __impl_choose_one_file_from_home() {
	find ~ -print | fzf
}

function __impl_choose_one_file_from_home_exact() {
	find ~ -print | fzf -e
}

function __impl_choose_one_file_from_root() {
	local temp=$(sudo find / -print) # TODO: Terminating the pipe is very slow but sudo find is piped before password is written
	echo $temp | fzf
}

function __impl_choose_one_file_from_root_exact() {
	local temp=$(sudo find / -print) # TODO: Terminating the pipe is very slow but sudo find is piped before password is written
	echo $temp | fzf -e
}
