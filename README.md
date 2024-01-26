# overseas-entities-api

### Overview
API service for submitting overseas entity registrations

### Requirements
In order to run the service locally you will need the following:
- [Java 11](https://www.oracle.com/java/technologies/downloads/#java11)
- [Maven](https://maven.apache.org/download.cgi)
- [Git](https://git-scm.com/downloads)

### Getting started
To checkout and build the service:
1. Clone [Docker CHS Development](https://github.com/companieshouse/docker-chs-development) and follow the steps in the README.
2. Run ./bin/chs-dev modules enable overseas-entities
3. Run ./bin/chs-dev development enable overseas-entities-api (this will allow you to make changes).
4. Run docker using "tilt up" in the docker-chs-development directory.
5. Use spacebar in the command line to open tilt window - wait for overseas-entities-api to become green.
6. Open your browser and go to page http://chs.local/register-an-overseas-entity
7. If you are using the api directly, then use this url: http://chs.local/overseas-entities

These instructions are for a local docker environment.

### Endpoints

The full path for each public endpoints that requires a transaction id begins with the app url:
`${API_URL}/overseas-entities`

Method    | Path                                                                         | Description
:---------|:-----------------------------------------------------------------------------|:-----------

### Health Endpoints

We can define health groups by defining properties in application.properties file:
- ```management.endpoint.health.group.detail-healthcheck.include=detailHealthcheckIndicator```:
  - This will only display customised ```detailHealthcheckIndicator``` health info based on the class ```DetailHealthcheckIndicator```
    when calling ```http://api.chs.local/overseas-entity/healthcheck/detail-healthcheck``` **and not anything else**
    
  - Note: calling ```http://api.chs.local/overseas-entity/healthcheck``` will display everything including ```detailHealthcheckIndicator``` details in any groups,
    I wasn't able to exclude ```detailHealthcheckIndicator``` details on this endpoint so created another group endpoint below that excludes it

    

### Health Endpoints Groups
- By defining another group to have everything in the normal health group minus any ```detailHealthcheckIndicator``` info we can define another group endpoint:
  ```management.endpoint.health.group.simple-healthcheck.exclude=detailHealthcheckIndicator``` which has everything ```health``` has minus the ```detailHealthcheckIndicator``` details


- We can create multiple different health indicators by implementing the ```HealthIndicator``` interface. We can include and exclude them into health groups defined at the ```application.properties``` level


### Inspecting Docker containers from within other docker containers
- As part of service startup in docker, its important to be able to check whether a another dependent service (e.g. kafka) is up 
  and running before we attempt to bring our service up. This is a bit more challenging as we'd need for example, in the case of kafka, inspect the kafka container is alive from within another application container that uses kafka.
  Ideas for executing docker inspect commands inside containers might be a way, but needs exploring. Other professional inspection tools might be available, but again will need more research in our dev environment setup.
