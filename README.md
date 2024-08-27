
# Desktop-Quiz-App

This is a Java-based desktop application designed for taking quizzes on various programming languages. The application features a simple and intuitive user interface that allows users to select from multiple quiz topics such as C, C++, Java, Python, and JavaScript. The app also provides functionality to review past quiz results, making it an effective tool for both learning and self-assessment.





## Features

- Multi-Topic Quiz Selection: Users can choose quizzes from different available topics.(For ex-Programming languages: C, C++, Java, Python, and JavaScript)
- User-Friendly Interface: The main interface displays large, colorful buttons for each topic, ensuring easy navigation and a visually appealing experience.
- Oracle Database Integration: Quiz questions and results are stored in an Oracle DB, ensuring efficient data management and retrieval.
- Result Storage and Retrieval: Quiz results are stored in the database, allowing users to review their performance at any time via the "Quiz Records" section.
- Cross-Platform Compatibility: The application is built with Java, ensuring compatibility across different operating systems.




## Screenshots

![Screenshot 2024-08-27 153153](https://github.com/user-attachments/assets/e7916208-30e1-4649-9bcb-720921b5a357)

![Screenshot 2024-08-27 153225](https://github.com/user-attachments/assets/87425984-99ea-44d8-b662-1f5be7770ab5)

![Screenshot 2024-08-27 153254](https://github.com/user-attachments/assets/31c2b7f5-fca9-41f5-8d08-b3cbc338f398)

![Screenshot 2024-08-27 153358](https://github.com/user-attachments/assets/d16ad45a-cc0b-429a-81c7-71dce2596404)

![Screenshot 2024-08-27 153423](https://github.com/user-attachments/assets/2e74cd98-2e6d-4b13-a355-c6c34d24742f)

![Screenshot 2024-08-27 230014](https://github.com/user-attachments/assets/59bc6f4e-a2d2-47c1-b6e3-9f62b6feb8d8)


## Tech Stack

- **Java**

- **Java Swing for GUI**

- **Oracle Database for storage**

- **JDBC for connecting the Java application to the Oracle database.**


## Installation

1. Clone the repository.

```bash
  git clone https://github.com/ashutosh1502/Desktop-Quiz-App.git
  cd Desktop-Quiz-App
```
2. Set up the Oracle Database and import the provided schema and data for quiz questions and results.

3. Configure the database connection in the application code.

4. Compile and run the project using any Java IDE or through the command line.
```bash
  javac -cp C:\Users\HP\OneDrive\Desktop\QuizMaster\resources\ojdbc14-10.2.0.3.0.jar -d out src\*.java
  java -cp out;C:\Users\HP\OneDrive\Desktop\QuizMaster\resources\ojdbc14-10.2.0.3.0.jar NewMainFrame

```
    
## Contributing

Contributions are always welcome!

If you'd like to contribute to the project, please follow these steps:

- Fork the repository
- Create a new branch (git checkout -b feature/new-feature)
- Commit your changes (git commit -m 'Add new feature')
- Push to the branch (git push origin feature/new-feature)
- Create a pull request

