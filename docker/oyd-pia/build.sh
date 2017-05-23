#!/bin/bash

APP="oyd-pia"
APP_NAME="datentresor"
IMAGE="oydeu/oyd-pia"
LOGIN_PASSWORD=""
DATA=""

# read commandline options
REFRESH=false
BUILD_CLEAN=false
DEBUG_MODE=false
DOCKER_UPDATE=false
LOAD_DATA=false
LOAD_IMAGE=false
SET_PASSWORD=false
RUN_LOCAL=false
RUN_DEMO=false
VAULT_UPDATE=false
VAULT_DEMO=false
VAULT_PERSONAL=false
while [ $# -gt 0 ]; do
    case "$1" in
        --clean*)
            BUILD_CLEAN=true
            ;;
        --data=*)
            LOAD_DATA=true
            DATA="${1#*=}"
            ;;
        --debug*)
            DEBUG_MODE=true
            ;;
        --dockerhub*)
            DOCKER_UPDATE=true
            ;;
        --load-image=*)
            LOAD_IMAGE=true
            IMAGE="${1#*=}"
            ;;
        --name=*)
            APP_NAME="${1#*=}"
            ;;
        --password=*)
            SET_PASSWORD=true
            LOGIN_PASSWORD="${1#*=}"
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
        --vault-demo*)
            VAULT_DEMO=true
            ;;
        --vault-personal*)
            VAULT_PERSONAL=true
            ;;
        --vault*)
            VAULT_UPDATE=true
            ;;
        --help*)
            echo "Verwendung: [source] ./build.sh  --options"
            echo "erzeugt und startet OwnYourData Komponenten"
            echo " "
            echo "Optionale Argumente:"
            echo "  --clean           baut neues Docker-Image (--no-cache, alles neu kompilieren)"
            echo "  --data=file.zip   lädt die im ZIP-file angegebenen Daten"
            echo "  --debug           Debug-Messages der Java-Applikation werden ausgegeben"
            echo "  --dockerhub       pusht Docker-Image auf hub.docker.com"
            echo "  --help            zeigt diese Hilfe an"
            echo "  --load-image=TEXT verwendet angegebenes image anstatt docker build auszuführen"
            echo "  --name=TEXT       Name für Docker Container und bei --vault für Subdomain"
            echo "  --password=TEXT   setzt das angegebene Passwort (als Hash)"
            echo "  --refresh         aktualisiert docker Verzeichnis von Github"
            echo "                    (Achtung: löscht alle vorhandenen Zwischenschritte)"
            echo "  --run             startet Docker Container"
            echo "  --run-demo        startet Docker Container mit Demodaten"
            echo "  --vault           startet Docker Container auf datentresor.org"
            echo "  --vault-demo      startet Docker Container auf datentresor.org mit Demodaten"
            echo "  --vault-personal  startet Docker Container auf datentresor.org mit"
            echo "                    Daten für individuelle Nutzung vorkonfiguriert"
            echo " "
            echo "Beispiele:"
            echo " ./build.sh --clean --dockerhub"
            echo " ./build.sh --debug --vault-personal --name=test"
            echo " ./build.sh --clean --name=demo --vault-demo"
            echo " ./build.sh --load-image=oydeu/oyd-pia --vault-personal --name=funny-kant_101"
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
    if $RUN_DEMO || $VAULT_DEMO; then
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
    if $RUN_DEMO || $VAULT_DEMO; then
        APP_SUFFIX="_demo"
        APP_DEMO="$APP$APP_SUFFIX"
        IMAGE="oydeu/$APP_DEMO"
    fi
    docker build --no-cache -t $IMAGE --build-arg PG_PWD=$PG_PWD --build-arg DEBUG_MODE=$DEBUG_MODE .
else
    if ! $LOAD_IMAGE; then
        PG_PWD=$(grep 'password:' oyd-pia/src/main/resources/config/application-prod.yml | head -n1); PG_PWD=${PG_PWD//*password: /};
        if $RUN_DEMO || $VAULT_DEMO; then
            APP_SUFFIX="_demo"
            APP_DEMO="$APP$APP_SUFFIX"
            IMAGE="oydeu/$APP_DEMO"
        fi
        docker build -t $IMAGE --build-arg PG_PWD=$PG_PWD --build-arg DEBUG_MODE=$DEBUG_MODE .
    fi
fi

if $DOCKER_UPDATE; then
    docker push $IMAGE
fi

if $RUN_LOCAL; then
    docker stop $APP_NAME
    docker rm $(docker ps -q -f status=exited)
    docker rm $(docker ps -q -f status=created)
    docker run -d -p 8080:8080 --name $APP_NAME $IMAGE
fi

if $RUN_DEMO || $VAULT_DEMO; then
    # restart demo
    docker stop $APP_NAME
    docker rm $(docker ps -q -f status=exited)
    docker rm $(docker ps -q -f status=created)
    if $RUN_DEMO; then
        DEMO_ID=$(docker run -d --name $APP_NAME -p 8080:8080 $IMAGE)
    fi
    if $VAULT_DEMO; then
        DEMO_ID=$(docker run -d --name $APP_NAME --expose 8080 -e VIRTUAL_HOST=$APP_NAME.datentresor.org -e VIRTUAL_PORT=8080 $IMAGE)
    fi
    sleep 10

    # wait until demo pia is up and running
    if $RUN_DEMO; then
        until $(curl --output /dev/null --silent --head --fail http://localhost:8080); do
            printf '.'
            sleep 5
        done
    fi
    if $VAULT_DEMO; then
        until $(curl --output /dev/null --silent --head --fail https://$APP_NAME.datentresor.org); do
            printf '.'
            sleep 5
        done
    fi

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

    # setup scheduler service
    SCHEDULER_SECRET=$(cat /dev/urandom | LC_CTYPE=C tr -dc 'a-zA-Z0-9' | fold -w 20 | head -n 1)
    docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -c \"INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('eu.ownyourdata.service_scheduler', '', '$SCHEDULER_SECRET', 'eu.ownyourdata.scheduler*:read,eu.ownyourdata.scheduler*:write,eu.ownyourdata.scheduler*:update,eu.ownyourdata.scheduler*:delete', 'client_credentials', NULL, '', 3600, 3600, '{}', '');\""
    docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -c \"INSERT INTO repo (id, description, identifier) VALUES (50, 'Scheduler Status', 'eu.ownyourdata.scheduler.status');\""
    docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -c \"SELECT pg_catalog.setval('repo_id_seq', 1000, false);\""
    docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -c \"INSERT INTO item (id, value, belongs_id) VALUES (1, '{\\\"active\\\": true}', 50);\""
    docker exec $DEMO_ID su postgres -c "psql -U postgres -d pia -c \"SELECT pg_catalog.setval('item_id_seq', 100000, false);\""
    docker exec -d $DEMO_ID bash -c "cd /service-scheduler; rake db:create; rake db:migrate; MAILER_PASSWORD_DEFAULT=$MAILER_PASSWORD_DEFAULT rails runner \"ApplicationController.helpers.execute\" -s $SCHEDULER_SECRET"
fi

if $VAULT_PERSONAL; then
    CONTAINER_ID=$(docker run -d --name $APP_NAME --expose 8080 -e VIRTUAL_HOST=$APP_NAME.datentresor.org -e VIRTUAL_PORT=8080 $IMAGE)
    sleep 60
    docker logs $CONTAINER_ID | grep 'Local:\s*http://127.0.0.1:8080' &> /dev/null
    until [ $? == 0 ]; do  # $(curl --output /dev/null --silent --head --fail https://$APP_NAME.datentresor.org); do
        printf '.'
        sleep 10
        docker logs $CONTAINER_ID | grep 'Local:\s*http://127.0.0.1:8080' &> /dev/null
    done
    
    # copy and execute SQL scripts to create initial data set for personal use
    docker exec $CONTAINER_ID su postgres -c "psql -U postgres -d pia -a -f /oyd-pia/script/store.sql"
    SERVICE_SCHEDULER_SECRET=$(cat /dev/urandom | LC_CTYPE=C tr -dc 'a-zA-Z0-9' | fold -w 20 | head -n 1)
    SERVICE_BACKUP_SECRET=$(cat /dev/urandom | LC_CTYPE=C tr -dc 'a-zA-Z0-9' | fold -w 20 | head -n 1)
    docker exec $CONTAINER_ID su postgres -c "psql -U postgres -d pia -c \"INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('eu.ownyourdata.service_scheduler', '', '$SERVICE_SCHEDULER_SECRET', 'eu.ownyourdata.scheduler*:read,eu.ownyourdata.scheduler*:write,eu.ownyourdata.scheduler*:update,eu.ownyourdata.scheduler*:delete', 'client_credentials', NULL, '', 3600, 3600, '{}', '');\""
    docker exec $CONTAINER_ID su postgres -c "psql -U postgres -d pia -c \"INSERT INTO repo (id, description, identifier) VALUES (50, 'Scheduler Status', 'eu.ownyourdata.scheduler.status');\""
    docker exec $CONTAINER_ID su postgres -c "psql -U postgres -d pia -c \"INSERT INTO item (id, value, belongs_id) VALUES (1, '{\\\"active\\\": true}', 50);\""
    docker exec -d $CONTAINER_ID bash -c "cd /service-scheduler; rake db:create; rake db:migrate; MAILER_PASSWORD_DEFAULT='$MAILER_PASSWORD_DEFAULT' rails runner \"ApplicationController.helpers.execute\" -s $SERVICE_SCHEDULER_SECRET"
    docker exec $CONTAINER_ID su postgres -c "psql -U postgres -d pia -a -f /oyd-pia/script/update.sql"
    docker exec $CONTAINER_ID su postgres -c "psql -U postgres -d pia -c \"UPDATE jhi_user SET lang_key='de', login='data' WHERE id=3;\""
    if $SET_PASSWORD; then
        docker exec $CONTAINER_ID su postgres -c "psql -U postgres -d pia -c \"UPDATE jhi_user SET password_hash='$LOGIN_PASSWORD' WHERE id=3;\""
    fi

    # setup Backup Service
    docker exec $CONTAINER_ID su postgres -c "psql -U postgres -d pia -c \"INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('eu.ownyourdata.service_backup', '', '$SERVICE_BACKUP_SECRET', 'eu.ownyourdata.backup*:read,eu.ownyourdata.backup*:write,eu.ownyourdata.backup*:update,eu.ownyourdata.backup*:delete', 'client_credentials', NULL, '', 3600, 3600, '{}', '');\""
    docker exec $CONTAINER_ID su postgres -c "psql -U postgres -d pia -c \"INSERT INTO repo (id, description, identifier) VALUES (51, 'Backup Status', 'eu.ownyourdata.backup.status');\""
    docker exec $CONTAINER_ID su postgres -c "psql -U postgres -d pia -c \"INSERT INTO item (id, value, belongs_id) VALUES (2, '{\\\"active\\\": true}', 51);\""
    docker exec $CONTAINER_ID bash -c "echo \"4 2 * * * Rscript --vanilla /oyd-pia/script/backup.R https://$APP_NAME.datentresor.org $SERVICE_BACKUP_SECRET\" > /oyd-pia/script/cronfile"
    docker exec $CONTAINER_ID crontab /oyd-pia/script/cronfile
    docker exec $CONTAINER_ID crond

    docker exec $CONTAINER_ID su postgres -c "psql -U postgres -d pia -c \"SELECT pg_catalog.setval('repo_id_seq', 1000, false);\""
    docker exec $CONTAINER_ID su postgres -c "psql -U postgres -d pia -c \"SELECT pg_catalog.setval('item_id_seq', 1000, false);\""

    echo "$APP_NAME" >> /home/user/oyd/service-backup/pia_list.txt
fi

if $LOAD_DATA; then
    mkdir tmp_data
    unzip -d tmp_data $DATA
    docker cp tmp_data/store.sql $CONTAINER_ID:import
    docker exec $CONTAINER_ID su postgres -c "psql -U postgres -d pia -a -f /import/store.sql"

    rm -rf tmp_data
fi
