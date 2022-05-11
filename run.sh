#!/bin/sh
d_id=$(docker ps -a | grep 'im-' | awk '{print $1}')
# shellcheck disable=SC2034
val=$1
# shellcheck disable=SC2039
if [ "$val" == '--s' ]; then
  echo 'abc'
else
  if [ "$d_id" != '' ]; then
     docker-compose down --rmi all
  fi
  docker-compose up -d --build
fi