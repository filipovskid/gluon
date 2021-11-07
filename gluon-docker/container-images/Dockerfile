FROM ubuntu:18.04

# Updates system
# Installs java 15 for running the environment driver
# Installs python 3.8 and virtualenv for python executor
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

# Prepare the environment used by python executor
RUN virtualenv --python python3.8 --system-site-packages /gluon/python3

# Install packages used by python executor
# These reflect the packages used during development
RUN /gluon/python3/bin/pip install \
    grpcio==1.39.0 \
    protobuf==3.17.3 \
    ipython==7.28.0 \
    numpy==1.21.3 \
    pandas==1.3.4 \
    matplotlib==3.4.3 \
    pyspark==3.2.0 \
    findspark==1.4.2

ARG spark_binaries=https://downloads.apache.org/spark/spark-3.2.0/spark-3.2.0-bin-hadoop2.7.tgz
ARG spark_home=/gluon/spark/spark-3.2.0-bin-hadoop2.7
RUN curl -o /gluon/spark/spark-bin.tgz --create-dirs $spark_binaries && \
    tar xf /gluon/spark/spark-bin.tgz -C /gluon/spark

ENV JAVA_HOME=/usr/lib/jvm/zulu15-ca-amd64
ENV SPARK_HOME=$spark_home

# Prepare environment driver jar
COPY driver /gluon/driver

# Provide executor plugin jars and corresponding configurations
# The executor plugins directory structure conforms to the directory
# plugin finder's strategy for locating plugins
COPY executors /gluon/executors

COPY entrypoint.sh /

ENTRYPOINT ["/entrypoint.sh"]
