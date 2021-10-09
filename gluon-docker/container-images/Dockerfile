FROM ubuntu:18.04

RUN apt-get update && \
  apt-get install --yes \
    gnupg \
    curl \
    bash \
    sudo \
    python3.8 \
    virtualenv && \
  apt-key adv \
       --keyserver hkp://keyserver.ubuntu.com:80 \
       --recv-keys 0xB1998361219BD9C9 && \
  curl -O https://cdn.azul.com/zulu/bin/zulu-repo_1.0.0-2_all.deb && \
  apt-get install --yes ./zulu-repo_1.0.0-2_all.deb && \
  apt-get update && \
  apt-get install --yes zulu15-jdk && \
  apt-get clean

COPY jars /jars
COPY entrypoint.sh /

ENTRYPOINT ["/entrypoint.sh"]
