# rmm-services-server-app
A Remote Monitoring and Management (RMM) software that allows to register and monitor devices.

# Setup
## Manual setup
This repository runs on a local postgres instance. Its build management is handled by gradle, a 
build automation tool. Steps to use this app:


1. You need to create a local database named `postgres`. By default, it requires the database to be 
   owned by the `postgres` username without a password.
2. To build the application without the test suite you can run the following command:
   `./gradlew build -x test`. This step will generate a jar of the project.
3. Run `java -jar build/libs/rmm-services-server-app-0.0.1-SNAPSHOT`, to execute the server. This 
   will create 
   the database with data 
   about the services and prices used by the application
4. You can access the server with the following URL: `http://localhost:8081/rmm/{endpoint}`

# Getting Started
## Authorization token
This server assumes that the user has a valid token. In order to get one send a POST request to the 
following endpoints:
`http://localhost:8081/rmm/api/customer/auth/1/` or `http://localhost:8081/rmm/api/customer/auth/2/`

This returns information from two pre-loaded users that can be used as part of the authorization 
header as follows:
`Authorization: Bearer ${token}`

## Managing devices
The server provides operations to create, update, delete and list devices. The next subsections 
will contain examples on how to use these endpoints.
### Create a device
#### Endpoint
POST `http://localhost:8081/rmm/api/devices/`
#### Body
```
   {
    "name": "Device 1",
    "type": "WINDOWS_SERVER"
   }
```
Valid values for type are `MAC`,`WINDOWS_SERVER` and `WINDOWS_WORKSTATION`
#### Response
STATUS 200
```
   {
      "data": {
         "id": 1,
         "name": "A1",
         "type": "WINDOWS_SERVER"
      },
      "error": null
   }
```
#### Alternative Flows
It can return a 400 status response if the name or type attributes are not provided or have 
incorrect values.

### Delete a device
#### Endpoint
DELETE `http://localhost:8081/rmm/api/devices/:id`
where `:id` can be replaced with the id of the device that we want to delete
#### Response
STATUS 200
#### Alternative Flows
- It can return a 404 status response if the device does not exist
- It can return a 401 error if the customer is not the owner of the device

### Get all devices
#### Endpoint
GET `http://localhost:8081/rmm/api/devices/`
#### Response
STATUS 200
```
{
   "data": [
      {
         "id": 2,
         "name": "A1",
         "type": "WINDOWS_SERVER"
      }
   ],
   "error": null
}
```

### Update a device
#### Endpoint
PATCH `http://localhost:8081/rmm/api/devices/:id`
where `:id` can be replaced with the id of the device that we want to update
#### Body
```
{
   "name": "A5",
   "type": "WINDOWS_WORKSTATION"
}
```
#### Response
STATUS 200
#### Alternative Flows
- It can return a 404 status response if the device does not exist
- It can return a 401 error if the customer is not the owner of the device

## Managing Services
Customers can contract services that will be applied to all of their devices. In order to do 
those operations, this server provides endpoint to add, delete and list all the services that a 
customer contracted.
### Add a service
POST `http://localhost:8081/rmm/api/customer/services/`
#### Body
```
{
   "serviceIds": [1]
}
```
#### Response
STATUS 200
```
{
   "data": [
      {
         "id": 1,
         "name": "Antivirus"
      }
   ],
   "error": null
}
```
Valid values for the service id are:
- 1: Antivirus
- 2: Cloudberry
- 3: PSA
- 4: Teamviewer
#### Alternative Flows
- It can return a 422 status response if the services were already added
- It can return a 422 error if any of the services do not exist

### Remove a service
DELETE `http://localhost:8081/rmm/api/customer/services/`
#### Body
```
{
   "serviceIds": [1]
}
```
#### Response
STATUS 200
```
{
   "data": [
      {
         "id": 1,
         "name": "Antivirus"
      }
   ],
   "error": null
}
```
#### Alternative Flows
- It can return a 422 status response if the services were already added
- It can return a 422 error if any of the services do not exist

### Get all services
GET `http://localhost:8081/rmm/api/customer/services/`
#### Response
STATUS 200
```
{
   "data": [
      {
         "id": 1,
         "name": "Antivirus"
      }
   ],
   "error": null
}
```

## Invoice Services
This service calculates the monthly cost of the services and devices for the customer.
GET `http://localhost:8081/rmm/api/invoices/`
#### Response
STATUS 200
```
{
    "data": {
        "total": 9.00
    },
    "error": null
}
```

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.5/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.5/gradle-plugin/reference/html/#build-image)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.6.5/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.6.5/reference/htmlsingle/#boot-features-com.heliosqs.rmmservicesserverapp.security)
* [Rest Repositories](https://docs.spring.io/spring-boot/docs/2.6.5/reference/htmlsingle/#howto-use-exposing-spring-data-repositories-rest-endpoint)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

