# API Usages Documentation

## Table of Content

- [Users](#users)
  - [Create User](#create-user)
  - [Get All Users](#get-all-users)
  - [Get User by ID](#get-user-by-id)
  - [Update User](#update-user)
  - [Change Password](#change-password) (Not Available yet)
  - [Delete User](#delete-user)
- [Task](#tasks)
  - [Create Task](#create-task)
  - [Get All Tasks (with query params)](#get-all-tasks)
  - [Get Task By ID](#get-task-by-id)
  - [Update Task](#update-task)
  - [Update Task Status](#update-task-status) (Not Available yet)
  - [Delete Task](#delete-task)

## Users
### Create User
#### Description
**Method:** POST\
**Route:** `/users`

#### Request Body
**firstName:** String\
**lastName:** String\
**email:** Email (e.g. `user@example.com`)\
**password:** String
```json
{
  "email": "john.d@example.com",
  "password": "Pa55J0hnW0rdD0e",
  "firstName": "John",
  "lastName": "Doe"
}
```

#### Response Body
**Success Case:** 201 Created
```json
{
  "id": 1,
  "email": "example2@domain.com",
  "firstName": "John",
  "lastName": "Doe",
  "createdAt": "2023-11-25T12:07:02.401157Z",
  "updatedAt": "2023-11-25T12:07:02.401157Z"
}
```

### Get All Users
#### Description
**Method:** GET\
**Route:** `/users`
**Success Case:** 200 OK
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

**Not Found Case:** 200 OK
```json
[]
```

### Get User by ID
#### Description
**Method:** GET\
**Route:** `/users/:id` (e.g. `/users/2`)

#### Response Body
**Success Case:** 200 OK
```json
{
  "id": 2,
  "email": "john.w@example.com",
  "firstName": "John",
  "lastName": "Wick",
  "createdAt": "2023-11-24T17:40:14.173952Z",
  "updatedAt": "2023-11-24T17:40:14.173952Z"
}
```

**Not Found Case:** 404 Not Found
```json
null
```

### Update User
#### Description
**Method:** PUT\
**Route:** `/users/:id` (e.g. `/users/1`)

#### Request Body
**firstName:** String\
**lastName:** String\
**email:** Email (e.g. `user@example.com`)
```json
{
  "firstName": "Johny",
  "lastName": "Utah",
  "email": "johny.u@example.com"
}
```

#### Response Body
**Success Case:** 200 OK
```json
{
  "id": 1,
  "email": "johny.u@example.com",
  "firstName": "Johny",
  "lastName": "Utah",
  "createdAt": "2023-11-24T17:40:13.603696Z",
  "updatedAt": "2023-11-25T12:07:02.401157Z"
}
```

**Not Found Case:** 404 Not Found
```json
null
```

### Change Password (Not Available yet)
#### Description
**Method:** PATCH\
**Route:** `/users/:id/password` (e.g. `/users/1/password`)

#### Request Body
**password:** String
```json
{
  "password": "NewPassword"
}
```

#### Response Body
**Success Case:** 200 OK
```json
"Change Password Successfully"
```

**Fail Case:**\
Use Same Current Password: 422 Unprocessable Entity
```json
{
  "errors": {
    "password": "The new password is the same as the current password. Password remains unchanged."
  },
  "statusCode": 422
}
```

Not Found Case: 404 Not Found
```json
{
  "errors": {
    "message": "User Not Found"
  },
  "statusCode": 404
}
```


### Delete User
#### Description
**Method:** DELETE\
**Route:** `/users/:id` (e.g. `/users/1`)

#### Response Body
**Success Case:** 204 No Content
```json
null
```

**Fail Case:** 204 No Content
```json
null
```

## Tasks
### Create Task
#### Description
**Method:** POST\
**Route:** `/tasks`
#### Request Body
**title:** String\
**description:** String (Optional)\
**dueDate:** `YYYY-MM-dd` or `YYYY-MM-ddTHH:mm:ss.SSSZ` (e.g. `2024-10-31`, or `2024-10-31T10:26:13.441Z`)\
**status:** TaskStatus (e.g. `PENDING`, `IN_PROGRESS`, or `COMPLETED`)\
**userId:** Integer (e.g. `1`, `2`, or `3`)
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
**Success Case:** 201 Created
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

**User Not Found Case:** 400 Bad Request
```json
{
  "errors": {
    "userId": "User with ID [«not_created_user_id» does not exists"
  },
  "statusCode": 400
}
```

### Get All Tasks
#### Description
**Method:** GET\
**Route:** `/tasks`\
**Query Params¹:**\
due-date: `YYYY-MM-dd` (e.g. 2023-12-24)\
status: `PENDING`, `IN_PROGRESS`, or `COMPLETED`\
created-by: «user_id» (e.g. `1`, `2`, or `3`)\
updated-by: «user_id» (e.g. `1`, `2`, or `3`)
> **Note:**\
> ¹ Any Query Param can be used more than 1.\
> Example API Route: `/tasks?due-date=2023-12-24&due-date=2023-12-27&status=COMPLETED&created-by=1`.\
> (You may notice `due-date` is used twice.)

#### Response Body
**Success Case:** 200 OK
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

**Fail Case:** 204 No Content
```json
[]
```

### Get Task by ID
#### Description
**Method:** GET\
**Route:** `/tasks/:id` (e.g. `/tasks/2`)

#### Response Body
**Success Case:** 200 OK
```json
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
```

**Not Found Case:** 404 Not Found
```json
null
```

### Update Task
#### Description
**Method:** PUT\
**Route:** `/tasks/:id` (e.g. `/tasks/1`)

#### Request Body
**title:** String\
**description:** String (Optional)\
**dueDate:** `YYYY-MM-dd` or `YYYY-MM-ddTHH:mm:ss.SSSZ` (e.g. `2024-10-31`, or `2024-10-31T10:26:13.441Z`)\
**status:** TaskStatus (e.g. `PENDING`, `IN_PROGRESS`, or `COMPLETED`)\
**userId:** Integer (e.g. `1`, `2`, or `3`)
```json
{
  "title": "Task 1",
  "description": "Task 1's More Description",
  "dueDate": "2024-9-16",
  "status": "PENDING",
  "userId": 2
}
```

#### Response Body
**Success Case:** 200 OK
```json
{
    "id": 1,
    "title": "Task 1",
    "description": "Task 1's More Description",
    "dueDate": "2024-9-16",
    "status": "PENDING",
    "createdBy": 1,
    "updatedBy": 2,
    "createdAt": "2023-11-24T17:40:15.841047Z",
    "updatedAt": "2023-11-25T11:36:10.068296Z"
}
```

**Not Found Case:** 404 Not Found
```json
null
```

### Update Task Status
#### Description
**Method:** PATCH\
**Route:** `/tasks/:id/status` (e.g. `/tasks/1/status`)

#### Request Body
**status:** TaskStatus (e.g. `PENDING`, `IN_PROGRESS`, or `COMPLETED`)\
```json
{
  "status": "IN_PROGRESS"
}
```

#### Response Body
**Success Case:** 200 OK
```json
"Task ID 1's Status is changed [PENDING -> IN_PROGRESS]"
```

**Not Found Case:** 404 Not Found
```json
{
  "errors": {
    "message": "Task ID 1 Not Found"
  },
  "statusCode": 404
}
```

**Wrong Status Case:** 400 Bad Request
```json
{
  "errors": {
    "status": "status must only be [PENDING], [IN_PROCESS], or [COMPLETED]"
  },
  "statusCode": 400
}
```


### Delete Task
#### Description
**Method:** DELETE\
**Route:** `/tasks/:id` (e.g. `/tasks/1`)

#### Response Body
**Success Case:** 204 No Content
```json
null
```

**Fail Case:** 204 No Content
```json
null
```
