FROM anapsix/alpine-java:8_server-jre
ARG PG_PWD

RUN echo "@edge http://nl.alpinelinux.org/alpine/edge/main" >> /etc/apk/repositories && \
    apk update && \
    apk add curl "libpq@edge<9.7" "postgresql-client@edge<9.7" "postgresql@edge<9.7" "postgresql-contrib@edge<9.7" && \
    mkdir /docker-entrypoint-initdb.d && \
    curl -o /usr/local/bin/gosu -sSL "https://github.com/tianon/gosu/releases/download/1.2/gosu-amd64" && \
    chmod +x /usr/local/bin/gosu && \
    apk del curl && \
    rm -rf /var/cache/apk/*

ENV LANG en_US.utf8
RUN su postgres -c "initdb -D /var/lib/postgresql/data" \
        && mkdir /oyd-pia

ADD oyd-pia/target/*.war /oyd-pia
ADD script/run.sh /bin/run.sh
RUN chmod +x /bin/run.sh

EXPOSE 8080 5432

CMD ["/bin/run.sh"]
