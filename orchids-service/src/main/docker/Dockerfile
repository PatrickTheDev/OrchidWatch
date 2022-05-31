# syntax=docker/dockerfile:1

FROM anapsix/alpine-java
MAINTAINER PatrickTheDev

ADD ./orchids-service/build/libs/orchids-service-1.0-SNAPSHOT.jar /home/orchids-service.jar

ENTRYPOINT ["java", "-jar", "/home/orchids-service.jar"]
EXPOSE 8763