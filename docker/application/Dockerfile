FROM amazoncorretto:17.0.6

RUN yum update

WORKDIR /usr/local/bin

COPY application-1.0-SNAPSHOT.jar .

CMD ["java","-jar","application-1.0-SNAPSHOT.jar"]