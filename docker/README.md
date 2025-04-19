# How to build images

## Build steps

1. Do Maven clean and package
2. Copy application jar file (located under application/target/) to docker/application folder
3. Copy gateway jar file (located under gateway/target/) to docker/gateway folder
4. If there are some configuration chagnes on Keycloak update **tt_realm_config.json** file and copy it into **_compose
   ** folder
5. Open terminal/cmd
6. Navigate to .../docker/application folder
7. Execute the following command: **docker image build -t time-tracker .** (mind the dot in the end)
8. Navigate to .../docker/gateway folder
9. Execute the following command: **docker image build -t time-tracker-gateway .** (mind the dot in the end)