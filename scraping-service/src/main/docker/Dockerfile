# syntax=docker/dockerfile:1

FROM debian:stable-slim
ADD ./scraping-service/build/libs/scraping-service-1.0-SNAPSHOT.jar /home/scraping-service.jar

ENTRYPOINT ["java","-jar","/home/scraping-service.jar"]
EXPOSE 8762