FROM oydeu/oyd-pia_base_armhf:latest
ARG PG_PWD

RUN su postgres -c "initdb -D /var/lib/postgresql/data" \
        && mkdir /oyd-pia

COPY oyd-pia/target/*.war /oyd-pia
COPY script/run.sh /bin/run.sh
COPY script/*.sql /
RUN chmod +x /bin/run.sh

RUN git clone https://github.com/OwnYourData/service-scheduler.git \
        && cd service-scheduler \
        && bundle update

EXPOSE 8080 5432

CMD ["/bin/run.sh"]
