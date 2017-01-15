FROM larmog/armhf-alpine-java:jdk-8u73

ENV VERSION=v6.9.4 NPM_VERSION=3

# build node according to https://github.com/mhart/alpine-node/blob/1bd1c34029b61d1af7d90cbd7df8617201191df5/Dockerfile
RUN echo "http://dl-cdn.alpinelinux.org/alpine/v3.4/main" > /etc/apk/repositories && \
  echo "http://dl-cdn.alpinelinux.org/alpine/v3.4/community" >> /etc/apk/repositories && \
  echo "@edge http://nl.alpinelinux.org/alpine/edge/main" >> /etc/apk/repositories && \
  echo "@testing http://dl-cdn.alpinelinux.org/alpine/edge/testing" >> /etc/apk/repositories && \
  echo "@community http://dl-cdn.alpinelinux.org/alpine/edge/community" >> /etc/apk/repositories && \
  apk update && \
  apk upgrade && \
  apk add curl make gcc g++ python linux-headers paxctl libgcc libstdc++ gnupg && \
  gpg --keyserver ha.pool.sks-keyservers.net --recv-keys \
    9554F04D7259F04124DE6B476D5A82AC7E37093B \
    94AE36675C464D64BAFA68DD7434390BDBE9B9C5 \
    0034A06D9D9B0064CE8ADF6BF1747F4AD2306D93 \
    FD3A5288F042B6850C66B31F09FE44734EB7990E \
    71DCFD284A79C3B38668286BC97EC7A07EDE3FC1 \
    DD8F2338BAE7501E3DD5AC78C273792F7D83545D \
    C4F0DFFF4E8C1A8236409D08E73BC641CC11F4C8 \
    B9AE9905FFD7803F25714661B63B535A4C206CA9 && \
  curl -o node-${VERSION}.tar.gz -sSL https://nodejs.org/dist/${VERSION}/node-${VERSION}.tar.gz && \
  curl -o SHASUMS256.txt.asc -sSL https://nodejs.org/dist/${VERSION}/SHASUMS256.txt.asc && \
  gpg --verify SHASUMS256.txt.asc && \
  grep node-${VERSION}.tar.gz SHASUMS256.txt.asc | sha256sum -c - && \
  tar -zxf node-${VERSION}.tar.gz && \
  cd node-${VERSION} && \
  export GYP_DEFINES="linux_use_gold_flags=0" && \
  ./configure --prefix=/usr ${CONFIG_FLAGS} && \
  NPROC=$(grep -c ^processor /proc/cpuinfo 2>/dev/null || 1) && \
  make -j${NPROC} -C out mksnapshot BUILDTYPE=Release && \
  paxctl -cm out/Release/mksnapshot && \
  make -j${NPROC} && \
  make install && \
  paxctl -cm /usr/bin/node && \
  cd / && \
  if [ -x /usr/bin/npm ]; then \
    npm install -g npm@${NPM_VERSION} && \
    find /usr/lib/node_modules/npm -name test -o -name .bin -type d | xargs rm -rf; \
  fi && \
  apk del curl make gcc g++ python linux-headers paxctl gnupg ${DEL_PKGS} && \
  rm -rf /etc/ssl /node-${VERSION}.tar.gz /SHASUMS256.txt.asc /node-${VERSION} ${RM_DIRS} \
    /usr/share/man /tmp/* /var/cache/apk/* /root/.npm /root/.node-gyp /root/.gnupg \
    /usr/lib/node_modules/npm/man /usr/lib/node_modules/npm/doc /usr/lib/node_modules/npm/html

RUN apk update \
	&& apk add curl libpq postgresql-client postgresql postgresql-contrib \
	&& mkdir /docker-entrypoint-initdb.d \
	&& curl -o /usr/local/bin/gosu -sSL "https://github.com/tianon/gosu/releases/download/1.2/gosu-armhf" \
	&& chmod +x /usr/local/bin/gosu \
	&& apk add git ruby ruby-irb ruby-json ruby-rake ruby-bigdecimal ruby-io-console libstdc++ tzdata ruby-dev build-base libxml2-dev libxslt-dev sqlite-dev postgresql-dev nodejs libffi-dev \
	&& echo "gem: --no-ri --no-rdoc" > ~/.gemrc \
	&& gem install rails --version=4.2.7.1 \
	&& gem install io-console \
	&& gem install ffi -v '1.9.14'

RUN apk update \
	&& apk add wget gfortran readline-dev bzip2-dev xz xz-libs xz-dev pcre-dev curl-dev coreutils bash perl \
	&& mkdir -p /opt/R \
	&& cd /opt/R \
	&& wget https://cloud.r-project.org/src/base/R-3/R-3.3.2.tar.gz \
	&& tar xzf R-3.3.2.tar.gz \
	&& cd R-3.3.2 \
	&& ./configure --prefix=/opt/R/R-3.3.2 --enable-R-shlib --with-x=no \
	&& make \
	&& make install \
	&& ln -s /opt/R/R-3.3.2/bin/R /bin/R

RUN mkdir -p ~/.R \
        && cd ~/.R \
        && echo "CXXFLAGS  += -D__MUSL__" > Makevars \
        && echo "CXX1XFLAGS += -D__MUSL__" >> Makevars \
        && cd / \
	&& R -e "install.packages(c('devtools', 'RCurl', 'httr', 'jsonlite', 'rjson', 'dplyr', 'tidyr', 'lubridate', 'stringi', 'digest'), repos='http://cran.r-project.org')"

RUN cd ~/.R \
	&& echo "CFLAGS += -D__USE_MISC" >> Makevars \
	&& cd / \
        && apk update \
	&& apk add cairo-dev libxt-dev mariadb-client-libs mariadb-dev \
	&& R -e "install.packages(c('Cairo', 'RMySQL', 'httpuv', 'shiny'), repos='https://cran.rstudio.com')"

RUN wget https://raw.githubusercontent.com/OwnYourData/app-template/master/init.r \
	&& R -e "source('init.r')" \
	&& rm -f init.r

CMD ["/bin/bash"]
