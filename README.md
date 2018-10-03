# Bank Account

Demo RESTful service simulating operations with bank account

# Model
BankAccount is entity with 2 properties:
- Id - account identifier
   - positive integer
   - from 10000 to 99999
- Balance - amount of money on whis account
   - positive integer

# REST API

- Create bank account with specified id
  - URL `POST /bankaccount/{id}`
  - id in range [10000, 99999]
  - if id is invalid return `400` http status
  - if account with such id already exists return `400` http status
  - if operation successed return `200` http status
- Deposit to sepified bank account some amount of money
  - URL `PUT /bankaccount/{id}/deposit`
  - Amount of money must be passed in request body 
  - id in range [10000, 99999]
  - amount is positive integer or zero
  - if id or amount is invalid return `400` http status
  - if account with such id don't found return `404` http status
  - if operation successed return `200` http status
- Withdraw from sepified bank account some amount of money
  - URL `PUT /bankaccount/{id}/withdraw`
  - Amount of money must be passed in request body 
  - id in range [10000, 99999]
  - amount is positive integer or zero
  - if id or amount is invalid return `400` http status
  - if account with such id don't found return `404` http status
  - if account don't contain enough money return `400` http status
  - if operation successed return `200` http status
- Get balance of specified account
  - URL `GET /bankaccount/{id}/balance`
  - id in range [10000, 99999]
  - if id is invalid return `400` http status
  - if account with such id don't found return `404` http status
  - if operation successed return integer in response body - balance and `200` http status  

### Tech

This project written on java 8 with [spring-boot](https://spring.io/projects/spring-boot).

### Configuration

To configure for production you should modify `src/main/resources/application.properties`

To configure for tests you should modify `src/test/resources/application-test.properties`

### Installation

Build and run server without tests:
```sh
$ ./run.sh
```

Build with Maven:
```sh
$ mvn clean package
```
Resulting `bank.jar` located in `target` dir

 

