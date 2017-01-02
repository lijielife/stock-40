#!/bin/bash
BIN_PATH=`dirname "$0"`
INSTANCE_NAME=$2
HTTP_PORT=$3
MODE=http-server

source $BIN_PATH/config.sh

JVMARGS="-Dmode=$MODE -Dserver.port=$HTTP_PORT"

source $BIN_PATH/common.sh