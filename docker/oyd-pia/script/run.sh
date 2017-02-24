#!/bin/bash

# Stop on error
set -e

# install gems
# loop according to http://www.zhuwu.me/blog/posts/solve-gem-installation-timeout-when-building-docker-image
N=0
until [ ${N} -ge 5 ]
do
  bundle install  && break
  echo 'Try bundle again ...'
  N=$[${N}+1]
  sleep 1
done

#start postgreSQL
echo "starting postgreSQL ... "
su postgres -c "postgres -D /var/lib/postgresql/data &"
sleep 5
echo "started postgreSQL"

if su postgres -c "psql -lqt" | cut -d \| -f 1 | grep -q pia; then
    echo "PIA DB already exists"
else
    echo "creating PIA DB"
    su postgres -c "psql --command \"ALTER USER postgres WITH SUPERUSER PASSWORD '"$PG_PWD"';\""
    su postgres -c "createdb pia"
fi

echo "starting PIA"
if $DEBUG_MODE; then
    java -jar /oyd-pia/*.war --spring.profiles.active=prod
else
    java -jar /oyd-pia/*.war --spring.profiles.active=prod >/dev/null 2>&1
fi

# keep the stdin
/bin/bash
