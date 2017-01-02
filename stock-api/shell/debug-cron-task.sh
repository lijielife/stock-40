#!/bin/bash
BIN_PATH=`dirname "$0"`
INSTANCE_NAME=$2
MODE=cron-task

source $BIN_PATH/config.sh

JVMOPTS="-server -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=$3"
JVMARGS="-Dmode=$MODE"

source $BIN_PATH/common.sh