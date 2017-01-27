FROM oydeu/oyd-pia_base_armhf:latest
MAINTAINER "Christoph Fabianek" christoph@ownyourdata.eu

ARG PG_PWD
ARG DEBUG_MODE=false

RUN git clone https://github.com/OwnYourData/service-scheduler.git \
        && cd service-scheduler \
        && bundle update

RUN su postgres -c "initdb -D /var/lib/postgresql/data" \
        && mkdir -p /oyd-pia/script

COPY oyd-pia/target/*.war /oyd-pia/
COPY script/run.sh /bin/run.sh
COPY script/*.sql /oyd-pia/script/
RUN chmod +x /bin/run.sh

EXPOSE 8080 5432

CMD ["/bin/run.sh"]
