# Meeting Scheduler Application

## Overview

The Meeting Scheduler Application is a Java console application designed to schedule meetings based on employee booking requests. It ensures that all meetings are scheduled within predefined office hours and do not overlap with one another.

## Prerequisites

- Java JDK 8 or newer
- Maven (for building the application)

## Building the Application

To compile and package the application into a runnable JAR file, navigate to the project's root directory and execute the following Maven command:

````bash
mvn clean package
This command generates a meeting-scheduler-1.0-SNAPSHOT.jar file within the target directory.

Running the Application
After building the application, run it using Java with the following command:

bash

java -jar target/meeting-scheduler-1.0-SNAPSHOT.jar
Note: The application reads booking requests from the standard input (stdin). Prepare your input accordingly before running the application.

Input Format
The application expects input in a specific text format:

The first line specifies the company's office hours in a 24-hour clock format (HHMM HHMM).
Subsequent lines represent individual booking requests, each formatted as follows:
[request submission time, YYYY-MM-DD HH:MM:SS] [employee id]
[meeting start time, YYYY-MM-DD HH:MM] [meeting duration in hours]
Example input:

yaml

0900 1730
2011-03-17 10:17:06 EMP001
2011-03-21 09:00 2
2011-03-16 12:34:56 EMP002
2011-03-21 09:00 2
2011-03-16 09:28:23 EMP003
2011-03-22 14:00 2
````

Output
The application outputs a schedule of successfully booked meetings, grouped by date and sorted chronologically. Each entry shows the meeting start and end times, along with the employee ID.

Example output:

````
2011-03-21
09:00 11:00 EMP002
2011-03-22
14:00 16:00 EMP003
16:00 17:00 EMP004

````
Notes
Meetings that fall outside of office hours or overlap with already scheduled meetings will not be booked.
The application processes booking requests in the order they were submitted.
Contributing
Feel free to fork this repository and submit pull requests to contribute to the development of this application. For major changes, please open an issue first to discuss what you would like to change.
License MIT
