# The COGIP Microservice Architecture Challenge - REST API

This challenge encompasses weeks 6 to 10 from the BeCode Java track training.
The idea is to create a REST API and non-interactive CLI for an imaginary company named Cogip.

This is by no means a production ready piece of software, but was designed to learn and become more familiar with Java development.

## Features

The REST API connects to a MySQL database with information from and about the company.
Existing tables are User, Company, Contact and Invoices.

CRUD operations and endpoints were implemented for all tables.
Users have an admin, accountant or intern role. Certain endpoints are not available for accountants or interns.

## Development

The API was written using Spring Boot and is secured by JWT using Spring Security.

## Command Line Interface

To manipulate the data in the database, a non-interactive CLI was created, which can be found here:

https://github.com/Thibault-be/BeCode-Java-CogipCLI





## License

[MIT](https://choosealicense.com/licenses/mit/)