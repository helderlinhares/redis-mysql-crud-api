# Redis API

## Description

This is a simple example of an API Project using Kotlin to create, edit, delete and find an item.

This project was implemented as a Memcached spike, so it has some redundancies on cache functionalities.
It also may need some refinements and additional unit tests.

### Technical Info

* Technologies:
    * Kotlin
    * Spring Boot
    * Sleuth
    * Docker Compose
    * Gradle
    * JPA
    * Redis
* Database:
    * MySQL

## Building from Source

To clean/build the project from the console use the command:

```console
    gradle clean build
```

To run only tests from the console use the command:

```console
    gradle test
```

OBS: You can also use your IDE to run Gradle tasks.

## Running the project

The first thing you need to do is prepare your database.

You can do that by using docker compose on docker folder as detailed on `Using Docker Compose` session of this document.

With your database already prepared you just use the command below to Run the project:

```console
    gradle bootRun
```

OBS: You can also use your IDE to run the Project.

## Using Docker Compose

In Tab "Terminal" access the folder `docker` and type `docker-compose up -d {{service_name}}`.

After that, the service will be available in a docker container.

Useful Commands:
* Start Container: `docker-compose up -d {{service_name}}`
* Stop Container: `docker-compose down -d {{service_name}}`
* Logs: `docker-compose logs {{service_name}}`
* Logs in Real Time: `docker-compose logs -f {{service_name}}`

Service names available for this project:
* `mysql_container` (Database)
* `redis_container` (MemCached Database)

## Redis Cli

To manage redis cache, you can access redis-cli with the commands below:

1. Access redis_container bash: `docker exec -it redis_container /bin/bash`
2. Access redis-cli: `redis-cli`

Commands:
* Show Available Keys: `KEYS *`
* Get a Key value: `get "${KEY_NAME}"` (KEY_NAME example: "findById::1")
* Delete a Key: `del "${KEY_NAME}"`

## Postman

To use this api you can install Postman and import the collection available on `postman` folder.

To import the collection on Postman:
1. Select the Menu `File > Import`
2. Click on `Upload Files` button (`File` tab)
3. Select `./postman/redis-api.postman_collection.json` file to import. 
