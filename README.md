# Time tracker

## The keycloak docker issue, Important Note:

I am still addressing the Keycloak issue. I plan to build a custom Docker 
image for Keycloak to include the tt_realm_config.json file. It seems that 
the current and newer Keycloak Docker images are not importing the JSON realm 
configuration file correctly.

Go to: [The keycloak docker issue](https://github.com/borispopicbusiness/timetracker/issues/1#issue-2461832992)

## Update: The keycloak docker issue, Important Note:

Go to [The keycloak docker issue](https://github.com/borispopicbusiness/timetracker/issues/1#issue-2461832992)

The issue is still officially open. For now, the README.md update has been pushed to the master branch. 
The realm update is available in master branch, the docker-compose.yml is improved. The complete issue
is expected to be officially closed within the next two to three days due to new tests.

Status: Dockerfile made, realm import is performed automatically.

## Introduction

This is my time tracker web app, actually its backend part, developed in Java.

### Basic Instructions

NOTE: Check the Keycloak Docker issue

#### Tests

If You are inside timetracker directory

mvn test
mvn clean test 

If you are one directory above timetracker.

mvn test -f ./timetracker
mvn clean test -f ./timetracker

#### Compilation

if you are inside timetracker directory

mvn clean install

If you are one directory above timetracker

mvn clean install -f ./timetracker

#### Running the Application

NOTE: Check the Keycloak Docker issue

After successfully running tests and compiling the application, you can start the backend part.

First go to ./dev/time-tracker-dev-env/ perform

docker build . -t keycloak-ubuntu-test:4

THat will generate a docker image for Keycloak.

Then run

docker-compose up

or

docker-compose up -d

Next, you need to start the Gateway. If you are in the parent directory of timetracker, use the following command:

mvn exec:java -Dexec.mainClass="com.semiramide.timetracker.gateway.Gateway" -f ./timetracker/gateway

Next

mvn exec:java -Dexec.mainClass="com.semiramide.timetracker.Backend" -f ./timetracker/application
