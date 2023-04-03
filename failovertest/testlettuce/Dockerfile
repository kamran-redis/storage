# Dockerfile

FROM ubuntu:20.04
RUN apt-get update \
  && apt-get install -y \
    iproute2 net-tools \
  && apt-get clean \
  && rm -rf /var/lib/apt/lists/*
ENV JAVA_HOME=/opt/java/openjdk
COPY --from=eclipse-temurin:11 $JAVA_HOME $JAVA_HOME
ENV PATH="${JAVA_HOME}/bin:${PATH}"

RUN mkdir /opt/app
WORKDIR /opt/app
RUN "pwd"
RUN "ls"
ADD target/Lettuce-0.0.1-SNAPSHOT.jar /opt/app

ENTRYPOINT java -jar /opt/app/Lettuce-0.0.1-SNAPSHOT.jar