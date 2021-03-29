# Trip-Extractor
A simple project to calculate trips from a csv file that contains tap data from a card

This project is built with Gradle, the wrapper is included

# Assumption 

1. The csv file is always correctly formatted 
2. The file contains no invalid data
    e.g. a tap off with no matching tap on. These will be ignored

# Build

In cmd navigate to root project folder and execute
 
 1. `chmod +x gradlew` This is for change permission for gradlew wrapper
 2. `./gradlew build`
 
# Run Test  

Unit test can be executed by running this command in cmd

`./gradlew test`

# Run 

To run the project use this command

`./gradlew run --args "<filePath>" `

example 

`./gradlew run --args="demo.csv"`

## Arguments 

`filePath` this is the path to the csv file. The path is relative. A sample file "demo.csv" is used if this is 
not provided


# Distribute 

This project can be distributed via jar file.  

Execute
`./gradlew jar`

The jar file is located in `build/libs/trip-extractor-1.0.jar`


# Dependencies

`Jackson` Used for deserialize/serialize CSV file 
`Junit` for unit testing  