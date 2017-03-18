FROM oydeu/oyd-pia_base:latest
MAINTAINER "Christoph Fabianek" christoph@ownyourdata.eu

ARG PG_PWD
ARG DEBUG_MODE=false

RUN git clone https://github.com/OwnYourData/service-scheduler.git \
        && cd service-scheduler \
        && N=0 \
        && until [ ${N} -ge 5 ]; do bundle install  && break; echo 'Try bundle again ...'; N=$[${N}+1]; sleep 1; done \
        && R -e "source('init.r')" \
        && apk update \
        && apk add zip

RUN su postgres -c "initdb -D /var/lib/postgresql/data" \
        && mkdir -p /oyd-pia/script \
        && mkdir -p /archive \
        && mkdir -p /import \
        && touch /stores.sql \
        && touch /items.sql \
        && touch /repos.sql \
        && chmod 666 /*.sql

COPY oyd-pia/target/*.war /oyd-pia/
COPY script/run.sh /bin/run.sh
COPY script/*.sql /oyd-pia/script/
COPY script/*.R /oyd-pia/script/
RUN chmod +x /bin/run.sh

EXPOSE 8080

CMD ["/bin/run.sh"]
