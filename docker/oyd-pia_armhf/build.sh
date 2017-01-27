#!/bin/bash

APP="oyd-pia_armhf"
APP_NAME="datentresor"

# read commandline options
REFRESH=false
BUILD_CLEAN=false
DEBUG_MODE=false
DOCKER_UPDATE=false
RUN_LOCAL=false
RUN_DEMO=false
while [ $# -gt 0 ]; do
    case "$1" in
        --clean*)
            BUILD_CLEAN=true
            ;;
        --debug*)
            DEBUG_MODE=true
            ;;
        --dockerhub*)
            DOCKER_UPDATE=true
            ;;
        --name=*)
            APP_NAME="${1#*=}"
            ;;
        --refresh*)
            REFRESH=true
            ;;
        --run-demo*)
            RUN_DEMO=true
            ;;
        --run*)
            RUN_LOCAL=true
            ;;
        --help*)
            echo "Verwendung: [source] ./build.sh  --options"
            echo "erzeugt und startet OwnYourData Komponenten"
            echo " "
            echo "Optionale Argumente:"
            echo "  --clean      baut neues Docker-Image (--no-cache, alle neu kompilieren)"
            echo "  --debug      Debug-Messages der Java-Applikation werden ausgegeben"
            echo "  --dockerhub  pushed Docker-Image auf hub.docker.com"
            echo "  --help       zeigt diese Hilfe an"
            echo "  --name=TEXT  Name für Docker-Image"
            echo "  --refresh    aktualisiert docker Verzeichnis von Github"
            echo "               (Achtung: löscht alle vorhandenen Zwischenschritte)"
            echo "  --run        startet Docker Container"
            echo "  --run-demo   startet Docker Container mit Demodaten"
            if [ "${BASH_SOURCE[0]}" != "${0}" ]; then
                return 1
            else
                exit 1
            fi
            ;;
        *)
            printf "unbekannte Option(en)\n"
            if [ "${BASH_SOURCE[0]}" != "${0}" ]; then
                return 1
            else
                exit 1
            fi
    esac
    shift
done

if $REFRESH; then
    if [ "${BASH_SOURCE[0]}" != "${0}" ]; then
        cd ~/docker
        rm -rf $APP
        svn checkout https://github.com/OwnYourData/oyd-pia/trunk/docker/$APP
        echo "refreshed"
        cd ~/docker/$APP
        return
    else
        echo "you need to source the script for refresh"
        exit
    fi
fi

if $BUILD_CLEAN; then
    # clone current PIA
    rm -rf oyd-pia
    git clone https://github.com/OwnYourData/oyd-pia.git
    cd oyd-pia

    # demo specifc adoptions
    if $RUN_DEMO; then
        cp src/main/webapp/i18n/de/global.json.demo src/main/webapp/i18n/de/global.json
        cp src/main/webapp/i18n/en/global.json.demo src/main/webapp/i18n/en/global.json
    fi

    # change secrests in ymls
    # use correct sed
    if [ "$(uname)" == "Darwin" ]; then
        SED_COMMAND=gsed
    else # elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
        SED_COMMAND=sed
    fi

    # secret
    JHI_SECRET=$(cat /dev/urandom | LC_CTYPE=C tr -dc 'a-zA-Z0-9' | fold -w 21 | head -n 1)
    $SED_COMMAND -ri 's/^(\s*)(secret:\s*.*\s*$)/\1secret: '"$JHI_SECRET"'/' src/main/resources/config/application.yml
    $SED_COMMAND -i "s/return 'mySecretOAuthSecret';/return '"$JHI_SECRET"';/" src/main/webapp/scripts/components/auth/provider/auth.oauth2.service.js

    # key
    cat /dev/urandom | LC_CTYPE=C tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1 | { read random; $SED_COMMAND -ri 's/^(\s*)(key:\s*.*\s*$)/\1key: '"$random"'/' src/main/resources/config/application.yml; }

    # PostgreSQL
    PG_PWD=$(cat /dev/urandom | LC_CTYPE=C tr -dc 'a-zA-Z0-9' | fold -w 21 | head -n 1)
    $SED_COMMAND -ri 's/^(\s+)(name:\s*.*\s*$)/\1name: pia/' src/main/resources/config/application-prod.yml
    $SED_COMMAND -ri 's/^(\s+)(username:\s*.*\s*$)/\1username: postgres/' src/main/resources/config/application-prod.yml
    $SED_COMMAND -ri '0,/RE/s/^(\s+)(password:\s*.*\s*$)/\1password: '"$PG_PWD"'/' src/main/resources/config/application-prod.yml

    # build war file---
    npm install
    mvn -DskipTests -Pprod clean package
    cd ..
    if $RUN_DEMO; then
        APP_SUFFIX="_demo"
        APP_DEMO="$APP$APP_SUFFIX"
        docker build --no-cache -t oydeu/$APP_DEMO --build-arg PG_PWD=$PG_PWD --build-arg DEBUG_MODE=$DEBUG_MODE .
    else
        docker build --no-cache -t oydeu/$APP --build-arg PG_PWD=$PG_PWD --build-arg DEBUG_MODE=$DEBUG_MODE .
    fi
else
    PG_PWD=$(grep 'password:' oyd-pia/src/main/resources/config/application-prod.yml | head -n1); PG_PWD=${PG_PWD//*password: /};
    if $RUN_DEMO; then
        APP_SUFFIX="_demo"
        APP_DEMO="$APP$APP_SUFFIX"
        docker build -t oydeu/$APP_DEMO --build-arg PG_PWD=$PG_PWD --build-arg DEBUG_MODE=$DEBUG_MODE .
    else
        docker build -t oydeu/$APP --build-arg PG_PWD=$PG_PWD --build-arg DEBUG_MODE=$DEBUG_MODE .
    fi
fi

if $DOCKER_UPDATE; then
    docker push oydeu/$APP
fi

if $RUN_LOCAL; then
    docker stop $APP_NAME
    docker rm $(docker ps -q -f status=exited)
    docker rm $(docker ps -q -f status=created)
    docker run -d -p 8080:8080 --name $APP_NAME oydeu/$APP
fi

if $RUN_DEMO; then
    # restart demo
    docker stop $APP_NAME
    docker rm $(docker ps -q -f status=exited)
    docker rm $(docker ps -q -f status=created)
    APP_SUFFIX="_demo"
    APP_DEMO="$APP$APP_SUFFIX"
    DEMO_ID=$(docker run -d --name $APP_NAME -p 8080:8080 oydeu/$APP_DEMO)
    sleep 10

    # wait until demo pia is up and running
    until $(curl --output /dev/null --silent --head --fail http://localhost:8080); do
        printf '.'
        sleep 5
    done

    # copy and execute SQL scripts to create demo data set
    docker cp script/demo_credentials.sql $DEMO_ID:/oyd-pia/script/
    docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -a -f /oyd-pia/script/demo_credentials.sql"
    docker cp script/store.sql $DEMO_ID:/oyd-pia/script/
    docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -a -f /oyd-pia/script/store.sql"
    docker cp script/apps.sql $DEMO_ID:/oyd-pia/script/
    docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -a -f /oyd-pia/script/apps.sql"
    docker cp script/apps2.sql $DEMO_ID:/oyd-pia/script/
    docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -a -f /oyd-pia/script/apps2.sql"
    docker cp script/permissions.sql $DEMO_ID:/oyd-pia/script/
    docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -a -f /oyd-pia/script/permissions.sql"
    docker cp script/client_details.sql $DEMO_ID:/oyd-pia/script/
    docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -a -f /oyd-pia/script/client_details.sql"
    docker cp script/repo.sql $DEMO_ID:/oyd-pia/script/
    docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -a -f /oyd-pia/script/repo.sql"
    docker cp script/item.sql $DEMO_ID:/oyd-pia/script/
    docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -a -f /oyd-pia/script/item.sql"

    # setup scheduler
    SCHEDULER_SECRET=$(cat /dev/urandom | LC_CTYPE=C tr -dc 'a-zA-Z0-9' | fold -w 20 | head -n 1)
    docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -c \"INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('eu.ownyourdata.service_scheduler', '', '$SCHEDULER_SECRET', 'eu.ownyourdata.scheduler:read,eu.ownyourdata.scheduler.status:read,eu.ownyourdata.scheduler.email_config:read', 'client_credentials', NULL, '', 3600, 3600, '{}', '');\""
    docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -c \"INSERT INTO repo (id, description, identifier) VALUES (1, 'Scheduler Status', 'eu.ownyourdata.scheduler.status');\""
    docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -c \"SELECT pg_catalog.setval('repo_id_seq', 2, false);\""
    docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -c \"INSERT INTO item (id, value, belongs_id) VALUES (1, '{\\\"active\\\": true}', 1);\""
    docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -c \"SELECT pg_catalog.setval('item_id_seq', 2, false);\""
    docker exec -d $DEMO_ID bash -c "cd /service-scheduler; rails runner \"ApplicationController.helpers.execute\" -s $SCHEDULER_SECRET"
fi
