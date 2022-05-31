# syntax=docker/dockerfile:1

FROM anapsix/alpine-java
MAINTAINER PatrickTheDev

ADD ./gateway-service/build/libs/gateway-service-1.0-SNAPSHOT.jar /home/gateway-service.jar

ENTRYPOINT ["java", "-jar", "/home/gateway-service.jar"]
EXPOSE 8080