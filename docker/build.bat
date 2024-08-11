@echo off
title Docker building...
if "%1" == "" (
    echo Image version was not supplied...
    echo Build process terminated!
    exit /b 1
)
echo Building the app for Docker environment started!
cd..
echo Starting Maven build...
call mvn clean package
echo Maven build finished!
copy /Y "%CD%\application\target\application-3.0.1.jar" "%CD%\docker\application\application-3.0.1.jar"
copy /Y "%CD%\gateway\target\gateway-3.0.1.jar" "%CD%\docker\gateway\gateway-3.0.1.jar"
cd "Docker\application"
docker image build -t time-tracker-backend:%1 .
cd..
cd "gateway"
docker image build -t time-tracker-gateway:%1 .
cd..
cd..
cd..
cd "time-tracker-fe"
docker image build -t time-tracker-frontend:%1 .