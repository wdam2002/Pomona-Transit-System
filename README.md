# Pomona Transit System

The Pomona Transit System is a Java application designed to manage transit operations, leveraging JDBC for database connectivity. This system provides users with a range of functionalities, including scheduling, driver and bus management, and trip information recording.

## Installation

To run the Pomona Transit System, ensure you have the following requirements:

- Java Development Kit (JDK)
- MySQL Server
- MySQL Connector/J JDBC Driver for Java database connectivity with MySQL

Follow these steps to set up the application:

1. Clone or download the repository to your local machine.
2. Open the project in your preferred Java IDE.
3. Download the MySQL Connector/J JDBC Driver from [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/).
4. Add the MySQL Connector/J executable JAR file to your project's classpath.
5. Set up a MySQL database named `pomona_transit` with appropriate tables. You can find the database schema in the `db_schema.sql` file.
6. Update the database connection details in the `main` method of the `PomonaTransit` class.
7. Build and run the application.

## Usage

Upon running the application, you will be presented with a menu displaying various options. Choose an option by entering the corresponding number and follow the prompts to perform the desired operation.

The main menu options include:

1. Display Schedule
2. Delete Trip Offering
3. Add Trip Offering
4. Change Driver
5. Change Bus
6. Display Stops
7. Display Weekly Schedule
8. Add Driver
9. Add Bus
10. Delete Bus
11. Insert Trip Info
0. Quit
