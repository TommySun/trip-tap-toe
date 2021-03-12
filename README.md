# account-balance-calculator
A simple project to calculate balance over a period from a csv file

This project is built with Gradle, the wrapper is included

# Build

In cmd navigate to root project folder and execute
 
 1. `chmod +x gradlew` This is for change permission for gradlew wrapper
 2. `./gradlew build`
 
# Run Test  

Unit test can be executed by running this command in cmd

`./gradlew test`

# Run 

To run the project use this command

`./gradlew run --args "<filePath> <accountIds> <fromDate> <toDate>" `

example 

`./gradlew run --args="transaction.csv ACC334455,ACC998877 \"20/10/2018 12:00:00\" \"20/10/2018 19:00:00\""`

## Arguments 

`filePath` this is the path to the csv file. The path is relative. A sample file "transaction.csv" is provide

`accountIds` A comma separated string. Multiple Account Id can be supplied and the end result will be grouped by given account Id

`fromDate` The from date of transaction records of which will be included in the calculation. This must be in format of "dd/MM/yyyy HH:mm:ss" and wrapped by escaped double quote. E.G. `\"20/10/2018 12:00:00\"`  

`toDate` The end date of transaction records of which will be included in the calculation. This must be in format of "dd/MM/yyyy HH:mm:ss" and wrapped by escaped double quote. E.G. `\"20/10/2018 12:00:00\"`. This argument is optional, if not given, the calcuation will include all record start with `fromDate`  

## Calculate All

When only file path is supplied the calculation will include all transaction record. The result will grouped by accountIds

Example:

`./gradlew run --args "transaction.csv"`

The end result will be

```
ACC998877:
Relative balance for the period is: -$5.00
Number of transactions included is: 1

ACC334455:
Relative balance for the period is: -$32.25
Number of transactions included is: 4
```

# Distribute 

This project can be distributed via jar file.  

Execute
`./gradlew jar`

The jar file is located in `build/libs/account-transaction-calculator-1.0.jar`


# Dependencies

`Jackson` Used for deserialize CSV file 
`Google Guava` for date related functionality
`Junit` for unit testing  