#!/bin/bash
AppName=$2
PORT=$3
# nohup java -jar smtp-server-1.0.jar > smtp-server.log &
# shellcheck disable=SC2034
APP_HOME=$(pwd)


# shellcheck disable=SC2112
function start() {
  # shellcheck disable=SC2009
  PID=$(ps -ef | grep java | grep "$AppName" | grep -v grep | awk '{print $2}')
  if [ x"$PID" != x"" ]; then
    echo "$AppName is running..."
  else
    nohup java -jar "$AppName" "$PORT" > "$AppName".log &
    echo "Start $AppName success..."
  fi
}

# shellcheck disable=SC2112
function stop() {
  echo "Stop $AppName"
  PID=""
  query() {
    # shellcheck disable=SC2009
    PID=$(ps -ef | grep java | grep "$AppName" | grep -v grep | awk '{print $2}')
  }

  query
  if [ x"$PID" != x"" ]; then
    kill -TERM "$PID"
    echo "$AppName (pid:$PID) exiting..."
    while [ x"$PID" != x"" ]; do
      sleep 1
      query
    done
    echo "$AppName exited."
  else
    echo "$AppName already stopped."
  fi
}

# shellcheck disable=SC2112
function restart() {
  stop
  sleep 2
  start
}

# shellcheck disable=SC2112
function status() {
  # shellcheck disable=SC2126
  # shellcheck disable=SC2009
  PID=$(ps -ef | grep java | grep "$AppName" | grep -v grep | wc -l)
  if [ "$PID" != 0 ]; then
    echo "$AppName is running..."
  else
    echo "$AppName is not running..."
  fi
}

case $1 in # 根据参数选择分支case运行
start)
  start
  ;;
stop)
  stop
  ;;
restart)
  restart
  ;;
status)
  status
  ;;
*) ;;

esac