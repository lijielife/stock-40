#!/bin/bash
BIN_PATH=`dirname "$0"`
INSTANCE_NAME=$2
MODE=cron-task

source $BIN_PATH/config.sh

JVMARGS="-Dmode=$MODE"

source $BIN_PATH/common.sh