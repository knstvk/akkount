# akkount

<a href="https://travis-ci.com/knstvk/akkount"><img src="https://api.travis-ci.com/knstvk/akkount.svg?branch=master" alt="Build Status" title=""></a>

A simple personal finance application built on [CUBA Platform](https://www.cuba-platform.com).

![generic-ui](https://github.com/knstvk/akkount/blob/master/img/generic-ui.png)

<p align="center">
<img src="https://github.com/knstvk/akkount/blob/master/img/mobile-ui.png" width="300"></img>
</p>


## Features

In short, the application solves two problems:
 1. It shows the current balance by all accounts: cash, credit cards, deposits, debts, etc.
 2. It can generate a report by expense and income categories that shows where the money came from and what they were spent on in some period of time.

Some details:
* There are _accounts_ that represent different kinds of money.
* There are _operations_: income to account, expense from account and transfer between accounts.
* A _category_ can be set for expense or income operations.
* The current balance is constantly displayed and is recalculated after each operation.
* Categories report shows the summary by two arbitrary periods of time to allow quick visual comparison. Any category can be excluded from the report. You can "drill down" into any row to see operations that comprise the row.
* The system has a full-functional CUBA Generic UI and a mobile-friendly UI based on React. 

## Development

You should have Java 8+, npm 12+ and CUBA Studio 12+ installed.

Open the project in CUBA Studio and run the application server using *CUBA Application* run/debug configuration. The application will use the HSQL database served by Studio.

The main UI is available at http://localhost:8080/app. Login as `admin` / `admin`. 

You can generate some test data:

- Open *Administration > JMX Console*, find `app-core.akkount:type=SampleDataGenerator` MBean and open it.
- Enter the number of days to generate (e.g. 100) in the parameter field of the `generateSampleData` method and click *Invoke*.     

Open the terminal in the `modules/front` directory and run `npm run start`. The mobile-friendly UI will be available at http://localhost:3000.


## Building

The application contains a set of scripts and settings to run in a Docker container. In this case, it uses a file-based HSQL database stored in the `/akk-home/db/akk` directory inside the container.

Open the terminal in the project directory and run the following command to build the WAR file:

```
./gradlew buildWar
```

Copy the WAR file to the `docker` directory:

```
cp build/distributions/war/app.war docker-image
```

Build the docker image:

```
docker build -t akkount docker-image
```

Run the container:

```
docker run --rm -p 8080:8080 akkount
```

The main UI is available at http://localhost:8080/app, mobile-friendly UI at http://localhost:8080/app/front. Username: `admin`, password: `admin`.

If you want to keep the database after removal of the container, map the database location to some local directory, for example:

```
docker run --rm -p 8080:8080 -v /Users/me/akk-home:/akk-home akkount
```
