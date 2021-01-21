# Exam Grading

Software to manage the grades of TU Delft students.

## Project description

Software written for CSE2115 Software Engineering methods.

## Group Members

| 📸 | Name | Email |
|---|---|---|
| ![](https://secure.gravatar.com/avatar/c79751c9c0cfee31bd1413c41dae9c48?s=50&d=identicon) | Arjun Vilakathara | A.H.Vilakathara@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/4e833c0277007cd483152cf7b3fd053a?s=50&d=identicon) | Ethan Keller | E.Keller@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/3681e93a8bfcc597d587ddc00110167d?s=50&d=identicon)    | Adriaan Brands    | A.J.M.Brands@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/32771f3eb0f2aad9d9f0dbb94d3cf32a?s=50&d=identicon)    | Gideon Bot        | G.J.T.Bot@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/7026ce51a9906069d30c74d9a317547e?s=50&d=identicon)    | Stijn Coppens     | S.Coppens@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/0dafb2b8bebf8568a3c01f0424e9c86f?s=50&d=identicon)    | Noyan Eren Toksoy     | n.e.toksoy@student.tudelft.nl |

### Running

Before running the following, either create a database using the schema provided in doc\requirements\DatabaseSchema.PNG
or use thr provided .sql file in the same folder*.

1. Clone repository
2. Run `UserService\src\main\java\nl\tudelft\sem10\userservice\UserServiceApplication.java` and wait for it to run.
3.
Run `AuthenticationService\src\main\java\nl\tudelft\sem10\authenticationservice\AuthenticationServiceApplication.java`
and wait for it to run.
4. Run `CourseService\src\main\java\nl\tudelft\sem10\courseservice\CourseServiceApplication.java` and wait for it to
   run.
4. Run `D:\S\op5-sem10\GradingService\src\main\java\nl\tudelft\sem10\gradingservice\GradingServiceApplication.java` and
   wait for it to run.

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

The coverage report is generated in: build/reports/jacoco/test/html, which does not get pushed to the repo. Open
index.html in your browser to see the report.

### Static analysis

```
gradle checkStyleMain
gradle checkStyleTest
gradle pmdMain
gradle pmdTest
```

## Licence

None needed!