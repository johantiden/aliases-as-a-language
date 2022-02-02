#!/usr/bin/env bash

# ==================================================
# EACH > DIRECTORY AND FILE
# ==================================================

export LOOKLET_TOOLS_EACH_DIR="${HOME}/.e"
export LOOKLET_TOOLS_EACH_FILE="${LOOKLET_TOOLS_EACH_DIR}/e.conf"

if [[ ! -f "${LOOKLET_TOOLS_EACH_FILE}" ]]; then
    echo "# You may comment out lines like this." >> "${LOOKLET_TOOLS_EACH_FILE}"
    echo "# Add one repo per line as seen below." >> "${LOOKLET_TOOLS_EACH_FILE}"
    echo "" >> "${LOOKLET_TOOLS_EACH_FILE}"
    echo "lib-fx-util" >> "${LOOKLET_TOOLS_EACH_FILE}"
    echo "look-creator" >> "${LOOKLET_TOOLS_EACH_FILE}"
    echo "qualify" >> "${LOOKLET_TOOLS_EACH_FILE}"
    echo "retouch-assistant" >> "${LOOKLET_TOOLS_EACH_FILE}"
fi

# ==================================================
# EACH > EDIT/SAVE/LOAD
# ==================================================

function __looklet_each_edit {
    if [[ ! -t 0 ]]; then
        cat > "${LOOKLET_TOOLS_EACH_FILE}"
    else
        if __looklet_command_exists "subl"; then
            subl "${LOOKLET_TOOLS_EACH_FILE}"
        else
            open "${LOOKLET_TOOLS_EACH_FILE}"
        fi
    fi
}


# ==================================================
# EACH > ITSELF
# ==================================================

function __looklet_each_pipe {
    local tempfilename="$(mktemp)"
    cat > "${tempfilename}"
    EACH_FILE="${tempfilename}" __looklet_each "${@}"
}

function __looklet_each {
    local arguments="${@:2}"
    if [[ "${BASH_VERSION}" != "" && "${OUTER_COMMAND}" != "" ]]; then
        local command_and_arguments="${OUTER_COMMAND#* }"
        local arguments="${command_and_arguments}"
        local arguments="${arguments#* }"
        local arguments="${arguments#* }"
        if [[ "${arguments}" == "${command_and_arguments}" ]]; then
            local arguments=""
        fi
    fi

    local EACH_DEPTH="${EACH_DEPTH}"
    if [[ "${EACH_DEPTH}" == "" ]]; then
        local EACH_DEPTH="0"
    fi
    local EACH_DEPTH=$((EACH_DEPTH + 1))
    
    if [[ "${EACH_FILE}" == "" ]]; then
        local EACH_FILE="${LOOKLET_TOOLS_EACH_FILE}"
    fi

    local origin_directory="${PWD}"

    local CR='\033[91m' # red
    local CL='\033[92m' # lime
    local CP='\033[95m' # pink
    local CA='\033[96m' # aqua
    local CD='\033[39m' # default

    local repos

    readarray -t repos < "${EACH_FILE}"

    for element in "${repos[@]}"; do
        if [[ "${element}" == "#"* || "${element}" == "//"* || "${element}" == "" ]]; then
            continue
        fi
        
        local indentation=""    
        for ((n=0;n<${EACH_DEPTH};n++)); do
            local indentation="${indentation}========"
        done

        >&2 echo -e "${CL}${indentation}>${CA} ${element}${CD}"

        local directory=$(__looklet_guess_absolute_path "${element}")
        cd "${directory}"
        EACH_FILE="${EACH_FILE}" EACH_DEPTH="${EACH_DEPTH}" eval "${arguments}"
    done

    cd "${origin_directory}"
}

# ==================================================
# EACH > ALIASES AND HELP
# ==================================================

# We make use of a trick to retain quotes and avoid shell expansion before command execution.
# https://web.archive.org/web/20150910160505/https://simpletondigest.wordpress.com/2012/01/06/building-a-better-macro/

# Interested in how the black magic works?
# Try running this in your terminal:
# OUTER_COMMAND="${BASH_COMMAND}" eval 'echo $OUTER_COMMAND'
# Wow! A command that echoes itself!
# The eval is to avoid premature shell expansion. With the callback commands eval is not needed.
# This trick sadly only works on BASH.

alias e="OUTER_COMMAND=\"\${BASH_COMMAND}\" __looklet_each"

alias ee='__looklet_each_edit'

alias ea="__looklet_list_repos | __looklet_xargs -n1 basename | OUTER_COMMAND=\"\${BASH_COMMAND}\" __looklet_each_pipe"

alias em="__looklet_list_maven_repos | __looklet_xargs -n1 basename | OUTER_COMMAND=\"\${BASH_COMMAND}\" __looklet_each_pipe"

alias en="__looklet_list_node_repos | __looklet_xargs -n1 basename | OUTER_COMMAND=\"\${BASH_COMMAND}\" __looklet_each_pipe"

alias ed="__looklet_list_sub_directories | __looklet_xargs realpath | OUTER_COMMAND=\"\${BASH_COMMAND}\" __looklet_each_pipe"


# ==================================================
# EACH > TAB COMPLETION
# ==================================================

if [[ -n "${BASH_VERSION}" ]]; then
    if __looklet_command_exists "complete"; then
        # https://gist.github.com/baopham/2270270
        complete -o nospace -F _command e
        complete -o nospace -F _command er
    fi
fi

if [[ -n "${ZSH_VERSION}" ]]; then
    # TODO: Tab completion for ZSH not yet implemented.
    # TODO: I did not manage to figure it out in reasonable time.
    # TODO: Feel free to help out and open a PR!
    :
fi
