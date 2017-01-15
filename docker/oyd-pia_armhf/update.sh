#!/bin/bash

if [ $# -eq 0 ]
  then
    PG_PWD=$(grep 'password:' oyd-pia/src/main/resources/config/application-prod.yml | head -n1); PG_PWD=${PG_PWD//*password: /};
    docker build -t oydeu/oyd-pia_armhf --build-arg PG_PWD=$PG_PWD .
  else
    # clone current PIA
    rm -rf oyd-pia
    git clone https://github.com/OwnYourData/oyd-pia.git
    cd oyd-pia

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
    docker build --no-cache -t oydeu/oyd-pia_armhf --build-arg PG_PWD=$PG_PWD .
fi
docker push oydeu/oyd-pia_armhf
