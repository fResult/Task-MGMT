# Task Management
## Prerequisite
1. Java Development Kit (JDK) version 17 or more (I preferred version 21)
2. Gradle 8 (or more)
3. IntelliJ IDEA (or any preferred IDE)
4. Kotlin Plugin: (for IntelliJ IDEA’s user)
5. Docker (and Docker Compose)

## API Documentation
See [API_DOC.md](/apidoc/API_DOC.md) and [Task MGMT API | Postman](/apidoc/api-doc.json)
> **Note:**\
> Import _Task MGMT API Collection_ on Postman\
> and setup environment variable `base-url` = `http://«hostname»/«port»` (e.g. `http://localhost:8080`)

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
./gradlew bootRun
```

**Create Docker Image**
```shell
docker build -t «tag_name» .
# Example
docker build -t app-task-mgmt .
```

**Create Docker Container**
```shell
docker run -d --name «container_name» --network «network_name» \
  -e POSTGRES_URL=r2dbc:postgresql://«postgres_container_name»:5432/«database_name» \
  -p «target_port»/8080 \
  «app_image_name»
# Example
docker run -d --name app-task-mgmt \
  --network taskmgmt-dev \
  -e POSTGRES_URL=r2dbc:postgresql://taskmgmt-db:5432/task-mgmt \
  -p 8088:8080 \
  app-task-mgmt
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
    \dt+; -- To see every tables
    DROP TABLE «table_name»;
    
    exit; -- When finish dropping tables
    ```
4. Run project again
    ```shell
    ./gradlew bootRun
    # Data table will be re-created follow the `schemas.sql` file
    ```

## TODO
- [x] Validate body request to response 400 Bad Request
- [x] Create DTO for Tasks by UserId
- [x] Create GET `/tasks` API route to retrieve tasks that match specific due dates, statuses, or created/updated users.
- [ ] Create API Document with Open API or Swagger
- [ ] Add the source method in the Logger to (right now we know only source class)
    - **Actual Result   -** 2023-11-28T15:35:56.540+07:00 ERROR 39490 --- [actor-tcp-nio-2] d.fresult.taskmgmt.handlers.UserHandler [«METHOD_NAME»]  : [User] with ID [222] does not exist)
    - **Expected Result -** 2023-11-28T15:35:56.540+07:00 ERROR 39490 --- [actor-tcp-nio-2] d.fresult.taskmgmt.handlers.UserHandler [byId]  : [User] with ID [222] does not exist)
- [ ] Make `status` accept only possible 3 values in enum
- [ ] Hash Password
- [ ] Do authorization for most of API routes
- [ ] Make `createdBy` and `updatedBy` refer to authentication (token) automatic update by data auditing
