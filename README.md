# Smart-Fridge

School project, 2018-17.
System for Smart Fridge is designed for use in restaurnts.
It keeps the grocery inventory based on users' actions
i.e. grocery insertion, ejection, and edition of the information about items in use.

The system is not purposed for real use but only for leaning how to create such a systems as soon as it is a school project.

Detailed information on the project is provided in the report document.

## Getting Started

These instructions will get a copy of the project up and running on a local machine for development and testing purposes.
See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things are needed to install the software

```
Internet access
Java IDE
PostgreSQL JDBC driver
Valid login credentials to connect to the server
```

### Installing

Installation of Java IDE

* [IntelliJ IDEA](https://www.jetbrains.com/idea)
* [Netbeans](https://netbeans.org/downloads/index.html)

Installation of PostgreSQL JDBC driver

* [PostgreSQL JDBC driver](https://jdbc.postgresql.org/download.html)

## Deployment

Download the project on github. After the required software is installed, 
import the project to the IDE (IntelliJ IDEA will be used in expample) from external model - Maven to the desired root directory.
The rest of the import settings are default.

To connect to the server, in the Main class (in smart\fridge.application.ui package)
you should set the values of the variables url, username and password to login credentials for connection to the server.

Directory sql contains sql create and generating scripts separated by purpose. 
To generate the data, firstly connect the IDE to the server using DB Navigator tool, 
input the required connection data to the Connection\Database tab.
Run the scripts from the files in the following recommended order: 
create_script.sql, util_tables.sql, functions.sql, generate_script.sql. 
If run was not successful, use pgAdmin to connect to the server and generate the data.
* [pgAdmin](https://www.pgadmin.org/download/)

After successful data upload, you can run the application.

The application is controlled using console with typing the number of an action.

## Built With

* [Maven](https://maven.apache.org/)

## Authors

* **Tamara Savkova** - *Initial work* - [s-thora](https://github.com/s-thora)

## Acknowledgments
The project was mentored by Ivor Uhliarik.

The project was inspired by a sample project "Telekomunikačný systém" ("Telecommunication network") by Alexander Shanki Simko.

