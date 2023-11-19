# Task Management
## Prerequisite
1. Java Development Kit (JDK) version 17 or more (I preferred version 21)
2. Gradle 8 (or more)
3. IntelliJ IDEA (or any preferred IDE)
4. Kotlin Plugin: (for IntelliJ IDEA’s user)
5. Docker (and Docker Compose)

## Scripts
**Prepare**
```shell
docker-compose -f docker/compose.dev.yml up --build -d
```

**Build**
```shell
./gradlew clean build
```

**Run**
```shell
./graclew bootRun
```

**Data Table change**
1. Change Table definition in the `/src/main/resources/schemas.sql` file to relate with the Entity data classes
2. Access to the containerized database (Postgresql)
    ```shell
    docker exec -it taskmgmt-db bash
   
    # In the container
    psql -U postgres -d task-mgmt -W
    ```
3. Delete tables
    ```sql
    \dt+; # To see every tables
    DROP TABLE <table_name_which_is_changed_definition>;
    
    exit; # When finish dropping tables
    ```
4. Run project again
    ```shell
    ./graclew bootRun
    # Data table will be re-created follow the `schemas.sql` file
    ```
