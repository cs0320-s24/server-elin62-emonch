> **GETTING STARTED:** You must start from some combination of the CSV Sprint code that you and your partner ended up with. Please move your code directly into this repository so that the `pom.xml`, `/src` folder, etc, are all at this base directory.

> **IMPORTANT NOTE**: In order to run the server, run `mvn package` in your terminal then `./run` (using Git Bash for Windows users). This will be the same as the first Sprint. Take notice when transferring this run sprint to your Sprint 2 implementation that the path of your Server class matches the path specified in the run script. Currently, it is set to execute Server at `edu/brown/cs/student/main/server/Server`. Running through terminal will save a lot of computer resources (IntelliJ is pretty intensive!) in future sprints.

Sprint 1: CSV
Team members: elin62
Time allocated: 16h
Git repository: https://github.com/cs0320-s24/server-elin62-emonch.git

We choose to search for matches that exactly match the search value rather than just contain it as
We believe that this makes more sense for most user applications. This is easily adjustable by chaining
it form contain to equals in my main class. We are referencing this in an inline comment as well.

Overview:
The code we have written sets up a web server that serves as an interface to both CSV file data and external API data,
specifically the U.S. Census API. The server is designed to handle HTTP requests, process them to either read from CSV
files or fetch data from the API, and then return the data in a structured JSON format. This setup demonstrates our
understanding of web API development, JSON handling, and the implementation of testing strategies including mocking and
fuzz testing to ensure robustness and reliability of the server functionality.


Key features:
CSV Parsing: Efficiently parses CSV files into Java objects using a customizable parsing strategy.

Error Handling: Incorporates comprehensive error handling, including custom exceptions, to manage parsing and data
conversion errors. Error messages that are being displayed to the user are highly informative

Object Creation: Uses the CreatorFromRow interface for flexible object instantiation from CSV row data.

Data Modeling: Features MyDataClass for representing parsed data, demonstrating the application's ability to
adapt to various data structures.

Tests:
We are testing various incorrect user inputs including the null case, as we learned that null sometimes works even for
booleans, so we specifically made sure null being passed in as an argument leads to an informative error message.

Run/Build Program:
Run the tests by entering the TestClass and run them by clicking the green triangle at the top to run all at once
or the triangle next to a test, to only run that specific test.
In order to run the code, you need to enter the maven package (mvn package)

Errors/Bugs:
Issues with testing classes.
