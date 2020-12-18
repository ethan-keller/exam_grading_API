# Exam Grading

Software to manage the grades of TU Delft students.

## Project description 

Software written for CSE2115 Software Engineering methods.

## Group Members

| 📸 | Name | Email |
|---|---|---|
| ![](https://eu.ui-avatars.com/api/?name=Arjun+Vilakathara&length=2&size=50&color=DDD&background=777&font-size=0.325) | Arjun Vilakathara | A.H.Vilakathara@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/3681e93a8bfcc597d587ddc00110167d?s=50&d=identicon)    | Adriaan Brands    | A.J.M.Brands@student.tudelft.nl |

### Running 
Before running the following, either create a database using the schema provided in doc\requirements\DatabaseSchema.PNG or use thr provided .sql file in the same folder*. 
1. Clone repository
2. Run `UserService\src\main\java\nl\tudelft\sem10\userservice\UserServiceApplication.java` and wait for it to run.
3. Run `AuthenticationService\src\main\java\nl\tudelft\sem10\authenticationservice\AuthenticationServiceApplication.java` and wait for it to run.
4. Run `CourseService\src\main\java\nl\tudelft\sem10\courseservice\CourseServiceApplication.java` and wait for it to run.
4. Run `D:\S\op5-sem10\GradingService\src\main\java\nl\tudelft\sem10\gradingservice\GradingServiceApplication.java` and wait for it to run.

*The database created from the file may not work properly in some instances.

## How to contribute

If you wish to contribute to this repository, please first discuss this via email with the owners of this repository.
Code of Conduct

    •    Abusive language will not be tolerated.
    •    You must not create conflicts with the current application.
    •    You have to keep in mind that your code might not be approved.
    
Each contributor needs to abide to the code of conduct.

### Testing
```
gradle test
```

To generate a coverage report:
```
gradle jacocoTestCoverageVerification
```


And
```
gradle jacocoTestReport
```
The coverage report is generated in: build/reports/jacoco/test/html, which does not get pushed to the repo. Open index.html in your browser to see the report. 

### Static analysis
```
gradle checkStyleMain
gradle checkStyleTest
gradle pmdMain
gradle pmdTest
```

## Licence 
None needed!