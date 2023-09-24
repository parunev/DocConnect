
# DocConnect - Simplify Your Medical Appointments

Welcome to DocConnect, a user-friendly medical appointments app designed to simplify the process of finding doctors and scheduling appointments. With DocConnect, you can effortlessly manage your healthcare appointments and improve your overall patient experience.


## Table of Contents

- [Introduction](#docconnect---simplify-your-medical-appointments)
- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)
- [License](#license)
## Features

- **User and Specialist Registration**: Users and specialists can easily register and log themselves into the application.
- **Profile Management**: Users have the option to edit their profiles, manage notifications, cancel appointments, schedule new appointments and enable/disable MFA.
- **Find Specialists**: You can search for specialists based on their specialty, country, and name, making it convenient to find the right healthcare provider for your needs.
- **Online Appointment Booking**: DocConnect allows you to book appointments online without the need to make phone calls, streamlining the appointment scheduling process.
- **Instant Notifications and Reminders**: Stay informed with instant notifications and appointment reminders, ensuring you never miss an important medical appointment.
- **Security and Privacy**: DocConnect implements stringent security measures in compliance with HIPAA regulations, ensuring the privacy and confidentiality of your medical information.
- **Admin Functions**: Administrators can manage the application by granting specialist access, and adding, deleting, or updating countries, cities, specialties, and more.


## Geting Started
### Prerequisites

Before you start using DocConnect, ensure you have the following prerequisites in place:

- **Java Development Kit (JDK) 17 or newer:** If you don't have Java installed or need to update your current version, you can download JDK 17 from the official Oracle website or use a compatible distribution like OpenJDK.

    - [Install Oracle JDK 17](https://www.oracle.com/java/technologies/downloads/)

  
- **MySQL Database:** Make sure you have MySQL installed on your machine. If not, you can download MySQL from the official website or use a package manager like Homebrew on macOS.

  - [Install MySQL](https://dev.mysql.com/downloads/)

- **SMTP Email (First option):** : If you don't have SMTP email configured or need to update your current settings, you can obtain the required SMTP server details from your email provider.

  - [More about SMTP Emails](https://www.baeldung.com/spring-email)

- **Azure Email(Second option):** : If you don't have Azure Email Services configured or need to update your current settings, you can access Azure's official documentation. These services offer scalable and dependable email sending and delivery solutions within the Azure cloud environment. Set up and configure these services in the application to ensure efficient email communication and notifications.

  - [More about Azure Email](https://learn.microsoft.com/en-us/java/api/overview/azure/communication-email-readme?view=azure-java-stable)

- **OAuth2 (Google Credentials):** : If you need to implement OAuth 2.0 authentication, you can follow the official documentation for guidance. OAuth 2.0 is a widely adopted protocol for secure and delegated access to resources.

  - [More about OAuth2 Google Credentials](https://www.tutorialspoint.com/spring_boot/spring_boot_google_oauth2_sign_in.htm) to get detailed information on creating and managing OAuth 2.0 credentials. This will enable your application to securely authenticate and access Google services on behalf of users who grant permission.




### Installation

To get started with DocConnect, clone this repository to your local machine using the following command:

  ```bash
   git clone https://github.com/parunev/DocConnect.git
```

#### Build and Run the Project

Once you have Java 17 or newer installed, follow these steps to build and run the DocConnect Spring Boot application:

- Open a terminal or command prompt in the project's root directory or navigate to it using the following command:
```
cd back-end
```

- Build the project using Maven:

```
./mvnw clean install
```

- Run the Spring Boot application:

```
./mvnw spring-boot:run
```
    
## Usage

To interact with the DocConnect API and explore available endpoints, we provide detailed documentation using Swagger.

### Swagger Documentation 

You can access the Swagger documentation for DocConnect by navigating to the following URL in your web browser after starting the application:

- [Swagger Documentation](http://localhost:8080/swagger-ui/index.html)

Here, you will find a user-friendly interface that allows you to:

- **Browse and search** for available API endpoints.
- View detailed **descriptions** and **usage** instructions for each endpoint.
- **Execute API requests directly** from the documentation to test the endpoints.

Swagger makes it easy to understand and interact with the API, making your integration efforts smooth and efficient.


## API Documentation

#### Class-Level Documentation

In addition to Swagger documentation, I've provided comprehensive class-level documentation within the source code. This documentation offers insights into the inner workings of the application, making it easier for developers to understand and extend the functionality.
 
#### Logging Levels

Comprehensive logging throughout the application to provide insights into the processes and functions. The following logging levels are used:

- **DEBUG**: Detailed information for debugging and troubleshooting. Use this level when investigating issues or monitoring specific application behaviors.

- **INFO**: General information about the application's operation. This level provides a high-level overview of key events and processes.

- **WARN**: Warnings about potential issues or irregularities that do not necessarily indicate a problem. These messages are essential for proactive monitoring.

- **ERROR**: Indicates errors or exceptional conditions that require immediate attention. These messages are crucial for identifying and resolving critical issues.

By leveraging these logging levels, developers can gain valuable insights into the application's behavior, diagnose problems, and maintain the system effectively. Please refer to the class-level documentation for specific information on where and how these log messages are utilized within the codebase.


## Contributing

Contributions are always welcome!

I'm excited that you want to help make DocConnect even better! Contributions from the community are always appreciated. Whether it's reporting a bug, suggesting an enhancement, or contributing code, your support is valuable. Thank you for considering contributing to DocConnect!


## License

This project is licensed under the [MIT License](https://github.com/parunev/DocConnect/blob/main/LICENSE). Feel free to use and modify the code as needed for your own purposes.

