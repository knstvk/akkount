akkount
=======

A simple personal finance application built on [CUBA Platform](https://www.cuba-platform.com).

Features
--------

In short, the application solves two problems:
 1. It shows the current balance by all accounts: cash, credit cards, deposits, debts, etc.
 2. It can generate a report by expense and income categories that shows where the money came from and what they were spent on in some period of time.

Some details:
* There are _accounts_ that represent different kinds of money.
* There are _operations_: income to account, expense from account and transfer between accounts.
* A _category_ can be set for expense or income operations.
* The current balance is constantly displayed and is recalculated after each operation.
* Categories report shows the summary by two arbitrary periods of time to allow quick visual comparison. Any category can be excluded from the report. You can "drill down" into any row to see operations that comprise the row.
* The system consists of three web applications deployed onto one Tomcat instance:
   1. Middleware
   2. Full-functional CUBA Generic UI
   3. Polymer UI for mobile devices. 

Usage
-----

Install JDK 8 or above and set JAVA_HOME environment variable to the JDK root dir.
Open command line in the project directory and run the following command to build the application:
```
gradlew setupTomcat deploy
```

Now start HSQL server and create database in `data` directory:
```
gradlew startDb
gradlew createDb
```
To run Tomcat, use `gradlew start` Gradle command or `startup.*` scripts in `build/tomcat/bin`.

Main UI is available on `http://localhost:8080/app`, mobile-friendly UI on `http://localhost:8080/app-front`. 
Username: `admin`, password: `admin`.
