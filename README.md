# Academic Management

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
1. [ ] Implement the concept of UG curriculum into the application.
2. [ ] You should maintain a list of Program Cores and Program Electives and information about BTP
Capstone.
3. [ ] Note that information in item 2 may change with time. For e.g., a course which was PC for a
batch may no longer be a PC for their junior batch.
4. [ ] In summary you would have to maintain enough information to track his progress through the UG
curriculum.
5. [ ] Implement a procedure to check for graduation check. A student is allowed to check if he/she has
completed all the Program Core courses, minimum number of program electives and passed the
BTP Capstone credits.

## Bonus functionalities
- Refractoring and better error handling left to be done.
- thorough testing left to be done.
- Encrypt passwords