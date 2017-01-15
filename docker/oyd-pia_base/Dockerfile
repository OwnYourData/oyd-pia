FROM anapsix/alpine-java:8_server-jre_unlimited

RUN echo "@edge http://nl.alpinelinux.org/alpine/edge/main" >> /etc/apk/repositories && \
    apk update && \
    apk upgrade && \
    apk add curl libpq postgresql-client postgresql postgresql-contrib && \
    mkdir /docker-entrypoint-initdb.d && \
    curl -o /usr/local/bin/gosu -sSL "https://github.com/tianon/gosu/releases/download/1.2/gosu-amd64" && \
    chmod +x /usr/local/bin/gosu && \
    apk add git ruby ruby-irb ruby-json ruby-rake ruby-bigdecimal ruby-io-console libstdc++ tzdata ruby-dev build-base libxml2-dev libxslt-dev sqlite-dev postgresql-dev nodejs libffi-dev && \
    echo "gem: --no-ri --no-rdoc" > ~/.gemrc && \
    gem install rails --version=4.2.7.1 && \
    gem install io-console && \
    gem install nokogiri -v '1.7.0' -- --use-system-libraries --with-xml2-include=/usr/include/libxml2 --with-xml2-lib=/usr/lib && \
    gem install ffi -v '1.9.14' && \
    gem install gpgme

ENV LANG en_US.utf8
ENV LC_ALL en_US.UTF-8

RUN apk --no-cache add ca-certificates wget \
	&& wget -q -O /etc/apk/keys/sgerrand.rsa.pub https://github.com/sgerrand/alpine-pkg-R/releases/download/3.3.1-r0/sgerrand.rsa.pub \
	&& wget https://github.com/sgerrand/alpine-pkg-R/releases/download/3.3.1-r0/R-3.3.1-r0.apk \
	&& apk add R-3.3.1-r0.apk \
	&& rm -f R-3.3.1-r0.apk

RUN echo "@testing http://dl-cdn.alpinelinux.org/alpine/edge/testing" >> /etc/apk/repositories \
	&& echo "@community http://dl-cdn.alpinelinux.org/alpine/edge/community" >> /etc/apk/repositories \
	&& apk update \
	&& apk add R-dev@community openblas@community openblas-dev@community --update-cache --repository http://dl-3.alpinelinux.org/alpine/edge/community/ --allow-untrusted \
	&& ln -s /usr/include/R /usr/lib/R/include \
	&& rm -rf /var/cache/apk/*

RUN wget https://raw.githubusercontent.com/OwnYourData/app-template/master/init.r \
	&& ln -s /bin/tar /usr/bin/tar \
#	&& R -e "install.packages(c('RCurl', 'jsonlite', 'rjson'), repos='http://cran.r-project.org')" \
	&& R -e "source('init.r')" \
	&& rm -f init.r

CMD ["/bin/bash"]
