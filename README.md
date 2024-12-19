 Overview
The Library Management System is a comprehensive RESTful API for managing books and borrowers in a library. Key features include:

Borrower and Book Registration: Register new borrowers and books effortlessly.
Book Management: Borrow and return books with ease.
Comprehensive Logging: Detailed logging with SLF4J and Logback.
Containerization: Docker support for easy deployment.
Kubernetes Integration: Deployment with MySQL and Kubernetes.
CI/CD: Automated builds and deployments with GitHub Actions.
🚀 Features
Java 17: Utilizes the latest Java version with Maven for dependencies.
Spring Boot: Simplifies configuration and setup.
Dockerized: Multi-stage Docker builds for optimized images.
CI/CD: GitHub Actions automates Docker builds and pushes to Docker Hub.
API Documentation: Interactive API documentation with Swagger.
🛠️ Getting Started
Prerequisites
Ensure you have the following setup:

Java 17
Docker
Maven
GitHub account
Docker Hub account
Kubernetes (minikube or similar)
Build and Run Locally
Clone the repository:

git clone https://github.com/thilina01/library-management-system.git
cd library-management-system
Build the project using Maven:

mvn clean install
Run the application:

java -jar target/library-management-system-0.0.1-SNAPSHOT.jar
