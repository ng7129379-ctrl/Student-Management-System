# Student Management System (Java + MySQL)

A beginner-friendly console application for managing student records. It demonstrates Java OOP, JDBC, MySQL, CRUD operations, input validation, and prepared statements.

## Features
- Add a student
- View all students
- Search by roll number
- Update student details
- Delete a student with confirmation
- Validate required fields, email format, positive roll number, and marks (0–100)
- Store data persistently in MySQL

## Requirements
- JDK 17 or later
- MySQL Server
- Maven
- VS Code with Extension Pack for Java (or IntelliJ IDEA)

## 1. Create the database
Open MySQL Workbench or the MySQL command line and run the contents of `database/schema.sql`.

This creates the `student_management` database and the `students` table.

## 2. Configure database access
The app reads connection settings from environment variables:
- `SMS_DB_URL` (optional; default: `jdbc:mysql://localhost:3306/student_management`)
- `SMS_DB_USER` (optional; default: `root`)
- `SMS_DB_PASSWORD` (recommended; set this to your local MySQL password)

Set these variables in your terminal before running. Never commit your real database password to GitHub.

### Windows PowerShell example
```powershell
$env:SMS_DB_URL="jdbc:mysql://localhost:3306/student_management"
$env:SMS_DB_USER="root"
$env:SMS_DB_PASSWORD="YOUR_LOCAL_MYSQL_PASSWORD"
```

## 3. Run the project
From the project root folder, run:
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.naman.sms.Main"
```

If your Maven setup does not have the exec plugin available, add this plugin inside the `<build><plugins>` section of `pom.xml`:
```xml
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>exec-maven-plugin</artifactId>
  <version>3.5.0</version>
</plugin>
```
Then run the command again.

## 4. Test it
1. Add a student with a unique roll number and email.
2. View all records.
3. Search using that roll number.
4. Update the student's marks.
5. Delete the student and confirm with `YES`.

## Project structure
```text
StudentManagementSystem/
├── database/
│   └── schema.sql
├── src/main/java/com/naman/sms/
│   ├── Main.java
│   ├── Student.java
│   ├── StudentDao.java
│   └── Database.java
├── pom.xml
└── README.md
```

## Resume bullet (use only after you have run and tested it)
**Student Management System | Java, JDBC, MySQL**
- Built a console-based CRUD application to add, search, update, view, and delete student records.
- Used JDBC prepared statements to interact with MySQL and persist student data.
- Added input validation and handled duplicate records and database errors.

## Next improvements
- Add unit tests
- Add sorting and pagination
- Build a Spring Boot REST API
- Add a web interface
- Publish to GitHub with screenshots and setup instructions
