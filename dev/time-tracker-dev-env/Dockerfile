FROM ubuntu:latest

RUN apt update && \
    apt install -y openjdk-21-jdk wget

RUN wget https://github.com/keycloak/keycloak/releases/download/25.0.2/keycloak-25.0.2.tar.gz && \
    tar -xzf keycloak-25.0.2.tar.gz && \
    rm keycloak-25.0.2.tar.gz

ENV KEYCLOAK_HOME=/root/keycloak-25.0.2
ENV KEYCLOAK_ADMIN=admin
ENV KEYCLOAK_ADMIN_PASSWORD=admin
ENV PATH="$KEYCLOAK_HOME/bin:$PATH"

COPY ./tt_realm_config.json $KEYCLOAK_HOME/data/import/tt_realm_config.json

WORKDIR $KEYCLOAK_HOME/bin

#RUN cp /tmp/tt_realm_config.json $KEYCLOAK_HOME/conf/tt_realm_config.json

EXPOSE 8080 9990

ENTRYPOINT ["/keycloak-25.0.2/bin/kc.sh"]
CMD ["start-dev", "--import-realm"]
