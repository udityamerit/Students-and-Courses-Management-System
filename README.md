# **SCMS - Students and Courses Management System**

<div align="center">
  <h3>A modern, production-grade administrative web application built with Java SE and SQLite.</h3>
</div>

---

## **1. Introduction**

Welcome to the **Students and Courses Management System (SCMS)** (formerly CCRM). SCMS is a robust, elegant web application engineered to solve real-world complexities in academic administration. Transitioning from legacy paper trails to a highly resilient, centralized digital architecture, SCMS eliminates manual bottlenecks, enforces strict data integrity, and scales effortlessly from small campuses to massive universities.

SCMS empowers administrators with a stunning glassmorphism UI and a suite of powerful tools:

* **Student Administration**: Effortlessly add, list, and update student information with beautiful floating-label forms.
* **Course Coordination**: Create, list, and modify courses, and assign instructors with ease.
* **Enrollment Oversight**: Manage student course registrations, tracking total enrollments directly from the live dashboard.
* **Live Dashboard**: Monitor system metrics (Total Students, Active Courses, Total Enrollments) in real-time.
* **Modern Feedback**: Enjoy smooth toast notifications, responsive table badges, and staggered page animations.

## **2. UI Showcase**

Take a look at the production-level UI that powers SCMS!

### **Live Dashboard**
![Dashboard Showcase](docs/showcase/dashboard.png)
*A sleek, modern dashboard with animated glowing orbs and real-time statistics.*

### **Student Records**
![Student Records](docs/showcase/students.png)
*Data tables utilizing modern zebra-striping and intuitive status pill badges.*

### **Elegant Forms**
![Floating Label Forms](docs/showcase/forms.png)
*Responsive data entry forms featuring interactive floating labels and custom validation.*

---

## **3. Getting Started**

To compile and execute the SCMS web application locally, ensure your system meets the following prerequisites:

* **Java Development Kit (JDK)**: Version 11 or newer.
* **SQLite Database**: The application is pre-configured to run with a local file-based SQLite database (`app-data/ccrm.db`), requiring **zero** additional database setup!

### **Execution Instructions:**

1. **Obtain the Source Code**:
   ```bash
   git clone https://github.com/saimerit/Campus-Course-Records-Manager.git
   ```

2. **Compile the Application**:  
   Navigate to the project's root directory and execute the following command. This will compile all Java source files and place the generated `.class` files into the `bin` directory, ensuring the SQLite JDBC driver is included.
   ```bash
   javac -d bin -cp "lib/sqlite-jdbc-3.36.0.3.jar" src/edu/ccrm/cli/*.java src/edu/ccrm/config/*.java src/edu/ccrm/domain/*.java src/edu/ccrm/exception/*.java src/edu/ccrm/io/*.java src/edu/ccrm/service/*.java src/edu/ccrm/util/*.java src/edu/ccrm/web/*.java src/edu/ccrm/web/handler/*.java
   ```

3. **Launch the Application**:  
   With the compilation complete, run the application using this command:
   ```bash
   java -cp "bin;lib/sqlite-jdbc-3.36.0.3.jar" edu.ccrm.web.CCRMWebServer
   ```

4. **Access the Web Interface**:  
   Upon successful execution, the custom web server will start on port `8080`. Open your favorite modern web browser and navigate to:
   **[http://localhost:8080](http://localhost:8080)**

---

## **4. Database Configuration**

This application uses **SQLite**, a fast, self-contained, file-based database. No external database servers or users are required!

### **Schema Initialization**

The application automates the creation of the database schema. When you first run the application, it will detect the absence of the required tables, automatically execute the `database_setup.sql` script to create them, and save the active database in the `app-data/` directory.

---

## **5. Project Architecture & Syllabus Mapping**

This project is a testament to a solid foundation in core Java principles, object-oriented programming (OOP), modern custom Web Server implementation using `com.sun.net.httpserver`, and JDBC for database interaction.

| Syllabus Concept | Demonstrated in File/Class/Method |
| :---- | :---- |
| **Custom HTTP Server** | `CCRMWebServer.java`, `StaticFileHandler.java` |
| **RESTful API Handlers** | `ApiHandlers.java` |
| **Encapsulation** | `Student.java`, `Course.java` |
| **Inheritance** | `Person.java` (abstract base class) |
| **Polymorphism** | `TranscriptService.java` |
| **Abstraction** | `Person.java` |
| **Singleton Design Pattern** | `AppConfig.java` |
| **Builder Design Pattern** | `Course.java` |
| **Custom Exceptions** | `DuplicateEnrollmentException.java`, `MaxCreditLimitExceededException.java` |

---

## **6. The Evolution of Java (Educational Context)**

* **1995**: Sun Microsystems officially announces the Java programming language.  
* **1996**: The first version of the Java Development Kit (JDK 1.0) is released.  
* **2004**: Java SE 5.0 introduces significant enhancements such as generics, annotations, and autoboxing.  
* **2014**: Java SE 8 is launched, bringing major changes with Lambda expressions and the Stream API.  
* **2021**: Java SE 17 is released, becoming the latest version with Long-Term Support (LTS).

### **A Comparison of Java Editions: ME, SE, and EE**

| Aspect | Java ME (Micro Edition) | Java SE (Standard Edition) | Java EE (Enterprise Edition) |
| :---- | :---- | :---- | :---- |
| **Primary Application** | Designed for mobile devices and embedded systems. | The foundation for desktop, local servers, and console-based programs. | Tailored for large-scale, distributed web-centric enterprise applications. |

