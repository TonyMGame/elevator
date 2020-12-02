#!/bin/sh

rm -rf /etc/localtime
ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
DATE=$(date +%Y%m%d-%H%M%S)
TARGET_LOG_DIR=/babel/logs/$SERVICENAME/${DATE}-${HOSTNAME}

LOCAL_LOG_DIR=/data/logs/elevator_content_server
mkdir -p /data/logs

mkdir -p $TARGET_LOG_DIR
ln -s $TARGET_LOG_DIR $LOCAL_LOG_DIR

JVM_OPTS=" -Dfile.encoding=UTF8 -Duser.timezone=GMT+08"
JVM_OPTS="$JVM_OPTS -Xmx2048M -Xms2048M"
JVM_OPTS="$JVM_OPTS -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:/data/logs/elevator_content_server/gc.log -verbose:gc"
JVM_OPTS="$JVM_OPTS -XX:SoftRefLRUPolicyMSPerMB=0"
JVM_OPTS="$JVM_OPTS -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/data/logs/elevator_content_server/"
JVM_OPTS="$JVM_OPTS -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"

java $JVM_OPTS -jar /opt/sai/elevator_content_server/cms.jar