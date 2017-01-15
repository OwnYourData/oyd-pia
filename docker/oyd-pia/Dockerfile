FROM oydeu/oyd-pia_base:latest
ARG PG_PWD

RUN su postgres -c "initdb -D /var/lib/postgresql/data" \
        && mkdir -p /oyd-pia/script

COPY oyd-pia/target/*.war /oyd-pia/
COPY script/run.sh /bin/run.sh
COPY script/*.sql /oyd-pia/script/
RUN chmod +x /bin/run.sh

RUN git clone https://github.com/OwnYourData/service-scheduler.git  \
	&& cd service-scheduler \
	&& bundle update

EXPOSE 8080 5432

CMD ["/bin/run.sh"]
