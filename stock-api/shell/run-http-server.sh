#!/bin/bash
BIN_PATH=`dirname "$0"`
INSTANCE_NAME=$2
HTTP_PORT=$3
MODE=http-server

source $BIN_PATH/config.sh

JVMOPTS="-server -Xms1g -Xmx2g -XX:NewSize=512m -Xss512k -XX:+UseConcMarkSweepGC -XX:+UseParNewGC"
JVMARGS="-Dmode=$MODE -Dserver.port=$HTTP_PORT"

source $BIN_PATH/common.sh