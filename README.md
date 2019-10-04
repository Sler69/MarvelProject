# Marvel Prject(Albo)

This is a project that uses the marvel API to get information for different heroes!

## Getting Started

To be able to use this project you require to get your own API key from Marvel: https://developer.marvel.com/. Or ask your 
manager for credentials. 

## Notes

The api is case sensitive so we need to send exact name of character aka Iron Man to the service. In this case just add the param as this: Iron_Man = Iron Man, Black_Panther = Black Panther

### Prerequisites

What things you need to install the software and how to install them

```
Mongo
Gradle
Java
```
### Logging

For logging we are using log4j therefore you need to specify the logj4 jar you would like to use for the project.
In this project we compile the simple 4j JAR. https://mvnrepository.com/artifact/org.slf4j/slf4j-simple/1.7.28.
So to get some logging locally download the Jar and add it in the directory libs.

## Built With
* [Spark](http://sparkjava.com/) - Used for Microservice Framework
* [Gson](https://github.com/google/gson) - To construct and de-construct json.
* [Gradle](https://gradle.org/) - Dependency Management
* [Mongo](https://www.mongodb.com/) - Used for database
* [Log4j](https://logging.apache.org/log4j/2.x/) - Used for loggin

## Authors

* **Arturo Velazquez** - *Initial work* - [Sler69](https://github.com/Sler69)


