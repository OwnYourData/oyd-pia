FROM oydeu/oyd-pia_base:latest
ARG PG_PWD
ARG SCHEDULER_SECRET

RUN su postgres -c "initdb -D /var/lib/postgresql/data" \
        && mkdir /oyd-pia

ADD oyd-pia/target/*.war /oyd-pia
ADD script/run.sh /bin/run.sh
RUN chmod +x /bin/run.sh

RUN git clone https://github.com/OwnYourData/service-scheduler.git \
	&& cd service-scheduler \
	&& bundle update

EXPOSE 8080 5432 3000

CMD ["/bin/run.sh"]
