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
> Import _Task MGMT API_ on Postman\
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
- [ ] Make `status` accept only possible 3 values in enum
- [ ] Hash Password
- [ ] Do authorization for most of API routes
- [ ] Make `createdBy` and `updatedBy` refer to authentication (token) automatic update by data auditing
