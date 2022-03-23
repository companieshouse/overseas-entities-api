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