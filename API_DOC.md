# API Usages Documentation
## Table of Content
- [Users](#users)
  - [Get All Users](#get-all-users)
- [Task](#tasks)
  - [Create Task](#create-task)
  - [Get All Tasks](#get-all-tasks)
  - 

## Users
### Get All Users
#### Description
```txt
Method: GET
Route: /users
```

#### Response Body
```json
[
  {
    "id": 1,
    "email": "john.d@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "createdAt": "2023-11-24T17:40:13.603696Z",
    "updatedAt": "2023-11-24T17:40:13.603696Z"
  },
  {
    "id": 2,
    "email": "john.w@example.com",
    "firstName": "John",
    "lastName": "Wick",
    "createdAt": "2023-11-24T17:40:14.173952Z",
    "updatedAt": "2023-11-24T17:40:14.173952Z"
  }
]
```

## Tasks
### Create Task
#### Description
```text
Method: POST
Route: /tasks
```
#### Request Body
**title**: String\
**description**: String (Optional)\
**dueDate**: `YYYY-MM-dd` or `YYYY-MM-ddTHH:mm:ss.SSSZ` (e.g. `2024-10-31`, or `2024-10-31T10:26:13.441Z`)\
**status**: TaskStatus (e.g. `PENDING`, `IN_PROGRESS`, or `COMPLETED`)\
**userId**: Integer (e.g. `1`, `2`, or `3`)
```json
{
    "title": "Task Title",
    "description": "Task Description",
    "dueDate": "2023-11-31T10:26:13.441Z",
    "status": "PENDING",
    "userId": 1
}
```

#### Response Body
```json
{
    "id": 1,
    "title": "Task Title",
    "description": "Task Description",
    "dueDate": "2024-10-31",
    "status": "PENDING",
    "createdBy": 1,
    "updatedBy": 1,
    "createdAt": "2023-11-25T11:36:10.068296Z",
    "updatedAt": "2023-11-25T11:36:10.068296Z"
}
```

### Get All Tasks
#### Description
```text
Method: GET
Route: /tasks
Query Params¹:
  due-date: YYYY-MM-dd (e.g. 2023-12-24)
  status: PENDING | IN_PROGRESS | COMPLETED
  created-by: «user_id» (e.g. 1, 2, 3)
  updated-by: «user_id» (e.g. 1, 2, 3)  
```

> **Note:**\
> ¹ Any Query Param can be used more than 1.\
> Example API Route: `/tasks?due-date=2023-12-24&due-date=2023-12-27&status=COMPLETED&created-by=1`.\
> (You may notice `due-date` is used twice.)


#### Response Body
```json
[
  {
    "id": 1,
    "title": "Task-1",
    "description": "Task 1's Description",
    "dueDate": "2024-11-21",
    "status": "PENDING",
    "createdBy": 1,
    "updatedBy": 1,
    "createdAt": "2023-11-24T17:40:15.270398Z",
    "updatedAt": "2023-11-24T17:40:15.270398Z"
  },
  {
    "id": 2,
    "title": "Task-2",
    "description": "Task 2's Description",
    "dueDate": "2024-11-20",
    "status": "IN_PROGRESS",
    "createdBy": 2,
    "updatedBy": 1,
    "createdAt": "2023-11-24T17:40:15.841047Z",
    "updatedAt": "2023-11-24T17:40:15.841047Z"
  }
]
```
