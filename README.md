[![Java CI](https://github.com/aggekirikos/repo/actions/workflows/gradle.yml/badge.svg)](https://github.com/aggekirikos/repo/actions/workflows/gradle.yml)

# fOrk
![fork logo](https://github.com/aggekirikos/repo/blob/main/images/fork.PNG)


# Table of contents

- [General Information](#general-information)
    * [About us](#about-us)
    * [UML Diagram](#uml-diagram)
- [Tools](#tools)
- [System Requirements](#system-requirements)
- [Run the program](#run-the-program)
- [How to get the program's documentation](#how-to-get-the-program's-documentation)
- [Menu Description](#menu-description)
- [Authors](#authors)

# General Information

## About us 

In the framework of the course Programming II of the Department of Management Science and Technology, we developed an application named fork. Fork is a social media application that allows users to upload, share, review and comment recipes. Also, it gives users the opportunity to choose between other actions related to their account and their messages with other users. Practically, our program attempts to expose users in new cooking ideas by interacting with others and creating social relationships with them.

## UML Diagram
![uml diagram](https://github.com/aggekirikos/repo/blob/main/images/UML.jpg)


# Tools
* [Java](https://www.java.com/en/) - Programming Language
* [SQLite](https://www.sqlite.org/index.html) - For saving, editing, deleting and recalling data
* [Javadoc](https://www.oracle.com/java/technologies/javase/javadoc-tool.html) - Documentation Generator
* [CheckStyle](https://checkstyle.sourceforge.io/) - Static analysis tool
* [SpotBugs](https://spotbugs.github.io/) - Static analysis tool
* [JUnit](https://junit.org/junit4/) - Unit testing framework
* [Gradle](https://gradle.org/) - Building tool
* [Github Actions](https://github.com/features/actions) - Continuous Integration tool

# System Recuirements 
* In order to use the application,
**Java JDK 8** or later version is required. Click [here](https://www.oracle.com/java/technologies/downloads/), to download the Java JDK 8

# Run the program 
* Use the commands down below to run the program from cmd:
```
gradlew build
java -jar build/libs/fork/1.0.jar
```

# How to get the program's documentation
```
gradlew javadoc
```

# Menu Description
* Once your installation is completed , you will be able to run the application. The application screen will appear on your computer, where you will need to
   1. *Sign up* and create username, password , name and a small bio in order to become a fork member 
   2. *Log in*  and fill in your username and password that you have already created.
* Then you will able to see the main menu screen which contains,
    1. *Search a recipe*. The user will be asked to enter with hashtag the name of the recipe they want to search. The title of the recipes that contain the given hashtag, will be printed in the screen, where the user can choose between them. The chosen recipe will then be displayed.

    2. *Check your chatbox*. The user will be asked to open a new conversation with another user. If no users are matched , the appropriate message is displayed , otherwise the user will be asked to type the message that wants to be sent.

    3. *Make a post* The user will be asked to enter all the process and ingredients that are required for the recipe.

    4. *See or Edit your profile*.  The user will be asked to choose either to see the personal data that they entered while creating an account or edit those. If the desicion is to see the profile , his personal data will be printed otherwise the user will be asked to enter new personal information.

    5. *Log out*. The user is logged out and returned to the main menu.

    6. *Leave Fork*. The user closes the application..

    # Authors
| Full Name | Github Account | Mail |
| --- | --- | --- |
| Kirikos Aggelos (Team Coordinator) | [aggekirikos](https://github.com/aggekirikos) | t8210057@aueb.gr |
| Kapetanaki Elina | [ElinaKapetanaki](https://github.com/ElinaKapetanaki) | t8210050@aueb.gr |
| Papasideri Vasiliki | [vasipapasideri](https://github.com/vasipapasideri) | t8210115@aueb.gr |
| Siamantouras Vaggelis | [evansiam](https://github.com/evansiam) | t8210203@aueb.gr |
| Tsagkaraki Aggeliki | [Angeliki03](https://github.com/Angeliki03) | t8210150@aueb.gr |
| Tsaprounis Loukas | [LoukasTsaprounisAueb](https://github.com/LoukasTsaprounisAueb) | t8210151@aueb.gr |
| Tsetsila Despoina | [DespoinaTsetsila](https://github.com/DespoinaTsetsila) | t8210154@aueb.gr |
| Chlouveraki Nikol | [nikochlouveraki](https://github.com/nikochlouveraki) | t8210165@aueb.gr ||