#!/bin/bash

# Stop on error
#set -e

if [ $# -eq 0 ]
  then
    PG_PWD=$(grep 'password:' oyd-pia/src/main/resources/config/application-prod.yml | head -n1); PG_PWD=${PG_PWD//*password: /};
  else
    # clone current PIA
    rm -rf oyd-pia
    git clone https://github.com/OwnYourData/oyd-pia.git
    cd oyd-pia

    # changes for demo-setup
    cp src/main/webapp/i18n/de/global.json.demo src/main/webapp/i18n/de/global.json
    cp src/main/webapp/i18n/en/global.json.demo src/main/webapp/i18n/en/global.json

    # change values in ymls---
    # secret
    JHI_SECRET=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 21 | head -n 1)
    sed -ri 's/^(\s*)(secret:\s*.*\s*$)/\1secret: '"$JHI_SECRET"'/' src/main/resources/config/application.yml
    sed -i "s/return 'mySecretOAuthSecret';/return '"$JHI_SECRET"';/" src/main/webapp/scripts/components/auth/provider/auth.oauth2.service.js

    # key
    cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1 | { read random; sed -ri 's/^(\s*)(key:\s*.*\s*$)/\1key: '"$random"'/' src/main/resources/config/application.yml; }

    # PostgreSQL
    PG_PWD=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 21 | head -n 1)
    sed -ri 's/^(\s+)(name:\s*.*\s*$)/\1name: pia/' src/main/resources/config/application-prod.yml
    sed -ri 's/^(\s+)(username:\s*.*\s*$)/\1username: postgres/' src/main/resources/config/application-prod.yml
    sed -ri '0,/RE/s/^(\s+)(password:\s*.*\s*$)/\1password: '"$PG_PWD"'/' src/main/resources/config/application-prod.yml

    # build war file---
    npm install
    mvn -DskipTests -Pprod clean package
    cd ..
fi

# build Docker image
docker build --no-cache -t oydeu/oyd-pia_demo2 --build-arg PG_PWD=$PG_PWD --build-arg SCHEDULER_SECRET=$SCHEDULER_SECRET .

# restart demo pia
docker stop demo2-pia
docker rm $(docker ps -q -f status=exited)
DEMO_ID=$(docker run -d --name demo2-pia --expose 8080 -e VIRTUAL_HOST=demo2.datentresor.org -e VIRTUAL_PORT=8080 -e MAILER_PASSWORD_DEFAULT='FG%K3!jWkXvBfqq' oydeu/oyd-pia_demo2)
sleep 10

# wait until demo pia is up and running
until $(curl --output /dev/null --silent --head --fail https://demo2.datentresor.org); do
    printf '.'
    sleep 5
done
docker cp script/demo_credentials.sql $DEMO_ID:/
docker cp script/store.sql $DEMO_ID:/
docker cp script/apps.sql $DEMO_ID:/
docker cp script/apps2.sql $DEMO_ID:/
docker cp script/permissions.sql $DEMO_ID:/
docker cp script/client_details.sql $DEMO_ID:/
docker cp script/repo.sql $DEMO_ID:/
docker cp script/item.sql $DEMO_ID:/

docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -a -f demo_credentials.sql"
docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -a -f store.sql"
docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -a -f apps.sql"
docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -a -f apps2.sql"
docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -a -f permissions.sql"
docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -a -f client_details.sql"
docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -a -f repo.sql"
docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -a -f item.sql"

SCHEDULER_SECRET=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 20 | head -n 1)
docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -c \"UPDATE external_plugin SET mobileurl = 'https://scheduler.datentresor.org', url = 'https://scheduler.datentresor.org' where id=1003;\""
docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -c \"UPDATE oauth_client_details SET client_secret = '$SCHEDULER_SECRET' WHERE client_id = 'eu.ownyourdata.scheduler';\""

docker exec -d $DEMO_ID bash -c "cd /service-scheduler; rails runner \"ApplicationController.helpers.execute\" -s $SCHEDULER_SECRET > output.txt"

