# Redis API

## Description

This is a simple example of an API Project using Kotlin to create, edit, delete and find an item.

This project also uses Redis as a Memcached solution to optimize performance.

### Technical Info

* Technologies:
    * Kotlin
    * Spring Boot
    * Sleuth
    * Docker Compose
    * Gradle
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
