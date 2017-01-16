#!/bin/bash
BIN_PATH=`dirname "$0"`
INSTANCE_NAME=$2
MODE=cron-task

source $BIN_PATH/config.sh

#JVMOPTS="-server -Xms1g -Xmx2g -XX:NewSize=512m -Xss512k -XX:+UseConcMarkSweepGC -XX:+UseParNewGC"
JVMOPTS="-server -Xms256m -Xmx1g -XX:NewSize=128m -Xss256k -XX:+UseConcMarkSweepGC -XX:+UseParNewGC"
JVMARGS="-Dmode=$MODE"

source $BIN_PATH/common.sh
