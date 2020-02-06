# spring-boot-security
In this article, we will see how to secure a spring-boot-2.0 MVC pattern application using spring basic security. a user can registration with user name &amp; password as a role base and user can update password. after user login a can view module by role wise. it's has two role admin and user.

### Import spring-boot-security
1. `$ clone git https://github.com/ahasanhabibsumon/spring-boot-security.git`
2. `$ mvn clean install`
3. `$ mvn spring-boot:run` 
or import maven project on IDE 

### MySQL database configuration
1. change database user_name and password.
2. `create database basic_security;`

### Run spring-boot-security project
Go to browser and enter URL `http://localhost:8088`
