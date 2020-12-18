# Microservice endpoints

## Roles

- Teacher: type = 1
- Student: type = 0

Endpoints available to students: /student/*
Endpoints available to teachers: /teacher/*
Endpoints available to all roles: /*

## Endpoints

All requests will need a header called “Authorization” with the token in a special format:
format → `Authorization: Bearer <token>`
E.g. → `Authorization: Bearer thisIsMyVeryLongToken1234567`
If the token is not valid you get rejected, else you get access

### Authentication service (on port *8080*)

- **GET** */getToken*
    gets a token after validation user credentials
    - body: `{
    "netid": "somenetid",
    "password": "somehashedpassword"
    }`
    - *returns* String representation of token

- **GET** */encode/{hashedPassword}*
    - *returns* an encoded version of the given hashed password
    - *returns* status code `404 (NOT FOUND)` if hashed password is missing

- **GET** */validate/{token}*
    - *returns* “STUDENT” if user’s a student
    - *returns* “TEACHER” if user’s a teacher
    - *returns* status code `404 (NOT FOUND)` if user is not found

- **GET** */validate/netId/{netId}* + Authorization header set with token
    - *returns* TRUE if token belongs with the user with the given netId
    - *returns* FALSE otherwise

### User service (on port *8083*)

- **GET** */user*
    - required parameter: None 
    - *returns* a List of Users where each entry is a User object.'

- **GET** */user/{netId}*
    - *returns* a User object

-  **POST** */user* 
    - body: `[{"netId":"student1","password":"password","type":0}]`
    - *returns*: status code `201 CREATED` if the creation was successful.
    - If the user already exists, *return* status code `226 IM USED`
    - If the format of the JSON is wrong, *return* status code `400 BAD REQUEST`

- **DELETE** */user*
    - body: `[{"netId":"student1","password":"password","type":0}]` or just `[{"netId":"student1"}]`
    - *returns* status code `200 OK` if the deletion was successful
    - *returns* status code `404 NOT FOUND` if the user doesn't exist
     - *returns* status code `400 BAD REQUEST` if the JSON body was wrong.

- **PUT** */user*
    - body: `[{"netId":"student1","password":"password","type":0}]`
    - *returns* status code `200 OK` if the update was successful
    - *returns* `404 NOT FOUND` if the user doesn't exist.
    - *returns* status code `400 BAD REQUEST` if the JSON body was wrong.

- **GET** */user/type/{type}*
    - *returns* A list of Users matching the type id (0 or 1)

### Course Service (on port *8081*)

#### Routes through */teacher*
(Requests requiring the teacher role will return a status code `401 UNAUTHORIZED` if the token is invalid and a status code `403 FORBIDDEN` if the user is not a teacher)


- **GET** */course/courses*
    - *returns* all known courses as JSON array.

- **GET** */course/get*
    - Required parameter: courseCode
    - *returns* a course with the given code or a status code `404 NOT FOUND` if no such course exists

- **POST** */course/add*
    - Required header: Authorization (String token)
    - Required body: Course object (String name, String code)
    - *returns* the created course or a status code `409 CONFLICT` if a course with the same code already exists

- **DELETE** */course/remove*
    - Required header: Authorization (String token)
    - Required parameter: courseCode
    - *returns* the deleted course or a status code `204 NO CONTENT` if no course with the given code exists

- **GET** */category/categories*
    - *returns* all known course categories as JSON array

- **GET** */category/get*
    - Required parameters: courseCode, categoryName
    - *returns* a category with the given code or a status code `404 NOT FOUND` if no such category exists

- **POST** */category/add*
    - Required header: Authorization (String token)
    - Required body: Category object (String course, String category, double weight)
    -  *returns* the created category or a status code `409 CONFLICT` if a category with the same course and name already exists

- **DELETE** */category/remove*
    - Required header: Authorization (String token)
    -  Required parameters: courseCode, categoryName
Returns the deleted course category or a status code `204 NO CONTENT` if no category with the given course and name exists

### Grade Service (on port *8082*)
    
- **GET** */grade/grade*
    - Required headers: `Authorization: Bearer <some token>`
    - Optional parameters in JSON body: netid, course code, grade type


