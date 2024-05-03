/*
Trip ( TripNumber, StartLocationName, DestinationName)

TripOffering ( TripNumber, Date, ScheduledStartTime, SecheduledArrivalTime,                                                                                                      DriverName, BusID)

Bus ( BusID, Model,Year)

Driver( DriverName,  DriverTelephoneNumber)

Stop (StopNumber, StopAddress)

ActualTripStopInfo (TripNumber, Date, ScheduledStartTime, StopNumber, SecheduledArrivalTime, ActualStartTime, ActualArrivalTime, NumberOfPassengerIn, 
NumberOf PassengerOut)

TripStopInfo ( TripNumber, StopNumber, SequenceNumber, DrivingTime)
*/

CREATE TABLE Trip (
    TripNumber VARCHAR(10) PRIMARY KEY,
    StartLocation VARCHAR(50),
    FinalDestination VARCHAR(50)
);

CREATE TABLE TripOffering (
    TripNumber VARCHAR(10),
    Date DATE,
    ScheduledStartTime TIME,
    ScheduleArrivalTimeDestination TIME,
    DriverName VARCHAR(50),
    BusID VARCHAR(10),
    PRIMARY KEY (TripNumber, Date, ScheduledStartTime),
    FOREIGN KEY (TripNumber) REFERENCES Trip(TripNumber)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE TripStopInfo (
    TripNumber VARCHAR(10),
    StopNumber INT,
    StopName VARCHAR(50),
    SequenceNumber INT,
    ArrivalTime TIME,
    DepartureTime TIME,
    PRIMARY KEY (TripNumber, StopNumber),
    FOREIGN KEY (TripNumber) REFERENCES Trip(TripNumber)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (StopNumber) REFERENCES Stop(StopNumber)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE Driver (
    DriverName VARCHAR(50) PRIMARY KEY,
    DriverTelephoneNumber VARCHAR(15)
);

CREATE TABLE Bus (
    BusID VARCHAR(10) PRIMARY KEY,
    Model VARCHAR(50),
    Year INT
);

CREATE TABLE Stop (
    StopNumber INT PRIMARY KEY,
    StopAddress VARCHAR(100)
);

CREATE TABLE ActualStopInfo (
    TripNumber VARCHAR(10),
    Date DATE,
    ScheduledStartTime TIME,
    StopNumber INT,
    ScheduledArrivalTime TIME,
    ActualArrivalTime TIME,
    ActualStartTime TIME,
    PassengersIn INT,
    PassengersOut INT,
    PRIMARY KEY (TripNumber, Date, ScheduledStartTime, StopNumber),
    FOREIGN KEY (TripNumber, Date, ScheduledStartTime) REFERENCES TripOffering(TripNumber, Date, ScheduledStartTime)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (StopNumber) REFERENCES Stop(StopNumber)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);