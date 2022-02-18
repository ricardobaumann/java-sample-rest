## Coding conventions for Java REST backend services

This service implements an address management mechanism, and help us to explain our coding standards for Spring Boot. It is
basically a CRUD spring boot service, with validations, persistence and external service communication.

GOALS

* Separation of concerns
* Configuration
* Composition over inheritance
* Unit testing
* Mocking
* Integration testing

Local usage

* Run `docker-compose up` on root folder
* Run the service main class
* Check the `AddressController` class to check the API