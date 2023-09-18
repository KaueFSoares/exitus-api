# Exitus - Restfull API - *on development*
 
Welcome to the restfull API designed for the providing data access and other funcionalities for Exitus platform!

## Introduction

The Exitus platform is a solution developed to simplify and enhance access control for students and employees at IFSUL Campus Sapiranga. With a variety of features including student and responsible management, detailed entry and exit records, administrator authentication, and early exit scheduling, this platform is designed to ensure security, agility and convenience.

## Key Features

### 1. User Authentication (Login) - In Progress

- Authentication is provided for four user roles: Admins, Students, Parents, and Employees.
- Users can manage their personal information through their accounts.

### 2. Early Exit Permissions - In Progress

- Minor-age students are not allowed to leave the school earlier by default.
- Parents have the option to grant permission for their minor-age children to leave the school premises earlier than usual.

### 3. Access Register - In Progress

A comprehensive register is maintained, recording entries and exits for all users whenever they access the school.

### 4. Admin Functionality - In Progress

Admins have access to various management functionalities, including user management, register management, and early exit permission management.

### 5. QR Code Authentication - In Progress

All users can generate and use their QR codes for authentication when entering the school premises.

## Technologies Used

The School Access Control System (Front-end) is developed using the following technologies:

- **[Spring Boot 3](https://spring.io/projects/spring-boot)**: A framework for building Java applications quickly and with minimal configuration.
- **[Swagger UI Docs](https://swagger.io/tools/swagger-ui/)**: A tool for documenting and testing RESTful APIs.
- **[JUnit 5](https://junit.org/junit5/)**: A popular testing framework for Java applications, ensuring code quality through automated tests.
- **[Auth0](https://auth0.com)**: Integration with Auth0 JSON Web Tokens for secure authentication and authorization.
- **[MYSQL Database](https://www.mysql.com)**: Reliable, open-source RDBMS for structured data storage, widely used in web apps and enterprise systems.

## Main Libraries Used

This project utilizes several key libraries to enhance its functionality and development process. Here's a brief overview of each library:


- **[Spring Security](https://spring.io/projects/spring-security)**: A powerful authentication and authorization framework for securing Spring-based applications.
- **[Spring Data JPA](https://spring.io/projects/spring-data-jpa)**: A data access framework that simplifies database interactions with Java applications.
- **[Jakarta Validation](https://beanvalidation.org)**: Essential Java framework for data validation, enhancing application security and reliability.
- **[Lombok](https://projectlombok.org)**: Java library simplifying code with annotations for getters, setters, and other boilerplate methods.

## How to run

1. Clone this repository to your local environment.
2. Open the editor and get into the project folder
3. Install the maven dependencies defined on pom.xml
4. Run ApiApplication class
