#!/usr/bin/env bash

source "$(dirname $0)/../impl.sh"
__debugecho ": yank this repo"

.r | y
