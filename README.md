# Academic Management


## How to run
### Part 1: Connect to database:
- run following commands inorder in bash terminal with postgres installed:
  ```
  cd ./app/db/
  ./init.sh
  ```
- enter password if prompted to do so
- type ```psql`` in the teminal
- run the following commands:
  ```
  create database academic_management;
  \i db.sql
  ```

### Part 2: Run the application:
- run the following command in terminal:
  ```
  ./gradlew.bat run --console=plain -q (for windows)
  ./gradlew run --console=plain -q (for linux)
  ```
- to build run the following command:
  ```
  ./gradlew.bat build jacocoTestReport (for windows)
  ./gradlew build jacocoTestReport (for linux)
  ```

### Part 3: Evaluate the application
- see the test and coverage reports in the following directory:
  ```
  ./app/build/reports/tests/test/index.html
  ./app/build/reports/jacoco/test/html/index.html
  ```
### Part 4: Plans
- see the directory ```diagrams``` for activity and class diagram.




## Basic functionalities
- [x]  A Student must be able to: 
  - [x]  Register/deregister for a course:
    - [x]  not allowed to register for a course without clearing the pre-req.
    - [x]  not allowed to register for more than the allowed credit limit.
  - [x]  View only their grades.
  - [x]  Compute their current CGPA. 

   ```📍Checkpoint: 11-02-23 18:05```

- [x]  Academic office must be able to:
  - [x]  Edit the course catalog (with username Staff Dean’s office)
  - [x]  View grade of all students.
  - [x]  Generate transcript (Preferably a .txt file) of students.
   
   ```📍Checkpoint: 12-02-23 12:16```

- [x]  Faculty must be able to:
  - [x]  View grade of all students.
  - [x]  Register courses they would like to offer.
  - [x]  deregister courses they would like to offer.
  - [x]  Must be able to update course grades via .csv files. Input would be a filepath and the file
contents must be updated in the database.
   ```📍Checkpoint: 13-02-23 00:00```

  ```👹Deadline: 26-02-23 18:05👺``` ✅
## Advanced functionalities
1. [x] Implement the concept of UG curriculum into the application.
2. [x] Note that information in item 2 may change with time. For e.g., a course which was PC for a
batch may no longer be a PC for their junior batch.
1. [x] In summary you would have to maintain enough information to track his progress through the UG
curriculum.
   ```📍Checkpoint: 14-02-23 10:18```
1. [x] Implement a procedure to check for graduation check. A student is allowed to check if he/she has
completed all the Program Core courses, minimum number of program electives and passed the
BTP Capstone credits.
1. [x] You should maintain a list of Program Cores and Program Electives and information about BTP
Capstone.
   ```📍Checkpoint: 14-02-23 21:31```

## Bonus functionalities
- Refractoring and better error handling left to be done. ✅
- thorough testing left to be done. ✅


