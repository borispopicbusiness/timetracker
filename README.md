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

mvn exec:java -Dexec.mainClass="com.semiramide.timetracker.gateway.Gateway" -f ./gateway

Next

mvn exec:java -Dexec.mainClass="com.semiramide.timetracker.Backend" -f ./application

#### Testing Keycloak flow

After you build and run the keycloak image and container, execute the following steps:
    - sudo apt install jq       if you do not already have it
    - go to /dev/time-tracker-dev-env
    - chmod +x ./keycloak-flow-test.sh
    ./keycloak-flow-test.sh

If the output looks like the following one then tke keycloak works according to the desired behaviour:

            Requesting access token...
            Access token obtained successfully.
            Creating a user...
            Create user response:

            Fetching user ID for email newuser@example.com...
            User ID retrieved: 1411213d-e813-415f-a9dd-cf742639af8a
            Updating user 1411213d-e813-415f-a9dd-cf742639af8a...
            Update user response:

            Assigning role EMPLOYEE to user 1411213d-e813-415f-a9dd-cf742639af8a...
            Requesting client ID...
            Client ID retrieved: 76e3892b-f346-4518-9720-897e6dbc9dbe
            Role ID response:
            [{"id":"004aeda0-a42b-4ddc-8371-3c7c9d56d212","name":"EMPLOYEE","composite":false,"clientRole":true,"containerId":"76e3892b-f346-4518-9720-897e6dbc9dbe"},{"id":"7ee5a884-ffd5-48e2-b5ae-0ca97248b3dc","name":"ADMIN","composite":false,"clientRole":true,"containerId":"76e3892b-f346-4518-9720-897e6dbc9dbe"}]
            Assign role response:

            Deleting user 1411213d-e813-415f-a9dd-cf742639af8a...
            Delete user response:
