#!/bin/bash

# Stop on error
#set -e

#start postgreSQL
response=`psql -U postgres -t  -c "select now()" postgres`
if [ $? == 0 ]; then
    echo "postgreSQL already running";
else
    echo "starting postgreSQL ... "
    su postgres -c "postgres -D /var/lib/postgresql/data &"
    sleep 5
    echo "started postgreSQL"
fi

if su postgres -c "psql -lqt" | cut -d \| -f 1 | grep -q pia; then
    echo "PIA DB already exists"
else
    echo "creating PIA DB"
    su postgres -c "psql --command \"ALTER USER postgres WITH SUPERUSER PASSWORD '"$PG_PWD"';\""
    su postgres -c "createdb pia"
fi

echo "starting PIA"
java -jar /oyd-pia/*.war --spring.profiles.active=prod #>/dev/null 2>&1

# keep the stdin
/bin/bash
