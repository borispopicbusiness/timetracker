FROM amazoncorretto:17.0.6

RUN yum update

WORKDIR /usr/local/bin

COPY gateway-1.0-SNAPSHOT.jar .

EXPOSE 80

CMD ["java","-jar","gateway-1.0-SNAPSHOT.jar"]