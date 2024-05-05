import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

/**
 * PomonaTransit is a class that provides functionalities for managing a transit
 * system.
 * It allows users to perform various operations such as displaying schedules,
 * adding trip offerings,
 * deleting trip offerings, changing bus or driver information, and more.
 */
public class PomonaTransit {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/pomona_transit";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "mysql123";

    /**
     * Main method to run the Pomona Transit System.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Statement statement = connection.createStatement();
            showMenu();
            while (true) {
                System.out.println("Please enter your choice...");
                int input = scanner.nextInt();
                scanner.nextLine();
                switch (input) {
                    case 1:
                        displaySchedule(statement, scanner);
                        break;
                    case 2:
                        deleteTripOffering(statement, scanner);
                        break;
                    case 3:
                        addTripOffering(statement, scanner);
                        break;
                    case 4:
                    case 5:
                        changeBusOrDriver(statement, scanner);
                        break;
                    case 6:
                        displayStop(statement, scanner);
                        break;
                    case 7:
                        displayWeeklySchedule(statement, scanner);
                        break;
                    case 8:
                        addDriver(statement, scanner);
                        break;
                    case 9:
                        addBus(statement, scanner);
                        break;
                    case 10:
                        deleteBus(statement, scanner);
                        break;
                    case 11:
                        insertTripInfo(statement, scanner);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid input, please try again");
                        break;
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the main menu of the Pomona Transit System.
     */
    public static void showMenu() {
        System.out.println("****** Pomona Transit System ******");
        System.out.println("1. Display Schedule");
        System.out.println("2. Delete Trip Offering");
        System.out.println("3. Add Trip Offering");
        System.out.println("4. Change Driver");
        System.out.println("5. Change Bus");
        System.out.println("6. Display Stops");
        System.out.println("7. Display Weekly Schedule");
        System.out.println("8. Add Driver");
        System.out.println("9. Add Bus");
        System.out.println("10. Delete Bus");
        System.out.println("11. Insert Trip Info");
        System.out.println("0. Quit");
    }

    /**
     * Displays the schedule for a specific start location, destination, and date.
     * 
     * @param statement JDBC Statement object
     * @param scanner   Scanner object for user input
     */
    public static void displaySchedule(Statement statement, Scanner scanner) {
        try {
            System.out.println("Please enter start location");
            String startLocation = scanner.nextLine();
            System.out.println("Please enter destination name");
            String destination = scanner.nextLine();
            System.out.println("Please enter date (MM/dd/yyyy)");
            String date = scanner.nextLine();

            String sql = "SELECT T.startLocation, T.finalDestination, O.* "
                    + "FROM TripOffering O JOIN Trip T "
                    + "ON O.tripNumber = T.tripNumber "
                    + "WHERE T.startLocation = ? AND T.finalDestination = ? AND O.date = ?";

            PreparedStatement pstmt = statement.getConnection().prepareStatement(sql);
            pstmt.setString(1, startLocation);
            pstmt.setString(2, destination);
            pstmt.setString(3, date);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No schedule found from " + startLocation + " to " + destination);
                return;
            }

            printResultSet(rs);

        } catch (SQLException e) {
            handleSQLException(e, "displaying schedule");
        }
    }

    /**
     * Deletes a trip offering based on trip number, date, and scheduled start time.
     * 
     * @param statement JDBC Statement object
     * @param scanner   Scanner object for user input
     */
    public static void deleteTripOffering(Statement statement, Scanner scanner) {
        try {
            System.out.println("Please enter Trip number");
            String tripNumber = scanner.nextLine();
            System.out.println("Please enter date (MM/dd/yyyy)");
            String date = scanner.nextLine();
            System.out.println("Please enter ScheduledStartTime");
            String scheduledStartTime = scanner.nextLine();

            String sql = "DELETE FROM TripOffering "
                    + "WHERE TripNumber = ? AND Date = ? AND ScheduledStartTime = ?";
            PreparedStatement pstmt = statement.getConnection().prepareStatement(sql);
            pstmt.setString(1, tripNumber);
            pstmt.setString(2, date);
            pstmt.setString(3, scheduledStartTime);
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Deleted trip offering successfully.");
            } else {
                System.out.println("No trip offering with TripNumber: " + tripNumber + ", Date: " + date
                        + ", ScheduledStartTime: " + scheduledStartTime);
            }
        } catch (SQLException e) {
            handleSQLException(e, "deleting trip offering");
        }
    }

    /**
     * Adds a new trip offering to the system.
     * 
     * @param statement JDBC Statement object
     * @param scanner   Scanner object for user input
     */
    public static void addTripOffering(Statement statement, Scanner scanner) {
        try {
            while (true) {
                System.out.println("Please enter trip number");
                String tripNumber = scanner.next();
                System.out.println("Please enter date (MM/dd/yyyy)");
                String date = scanner.next();
                System.out.println("Please enter scheduledStartTime");
                String scheduledStartTime = scanner.next();
                System.out.println("Please enter scheduled Arrival Destination Time");
                String scheduledArrivalTimeDestination = scanner.next();
                System.out.println("Please enter driverName");
                String driverName = scanner.next();
                System.out.println("Please enter busID");
                String busID = scanner.next();

                String sql = "INSERT INTO TripOffering VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = statement.getConnection().prepareStatement(sql);
                pstmt.setString(1, tripNumber);
                pstmt.setString(2, date);
                pstmt.setString(3, scheduledStartTime);
                pstmt.setString(4, scheduledArrivalTimeDestination);
                pstmt.setString(5, driverName);
                pstmt.setString(6, busID);

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Insertion successfully.");
                } else {
                    System.out.println("Insertion Unsuccessfully, please check your data format.");
                }

                System.out.println("Add another trip offering (Y/N)");
                String option = scanner.next().toUpperCase();
                if (option.equals("N")) {
                    break;
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "adding trip offering");
        }
    }

    /**
     * Changes the bus or driver associated with a trip offering.
     * 
     * @param statement JDBC Statement object
     * @param scanner   Scanner object for user input
     */
    public static void changeBusOrDriver(Statement statement, Scanner scanner) {
        try {
            String option;
            while (true) {
                System.out.println("Do you want to change Bus or Driver (bus/driver)?");
                option = scanner.nextLine().toUpperCase();

                if (option.equals("BUS") || option.equals("DRIVER")) {
                    break; // Exit the loop if a valid option is entered
                } else {
                    System.out.println("Invalid option. Please enter either 'bus' or 'driver'.");
                }
            }

            System.out.println("Please enter the " + option + " information that you want to change");
            String info = scanner.nextLine();
            System.out.println("Please enter Trip number");
            String tripNumber = scanner.nextLine();
            System.out.println("Please enter Date");
            String date = scanner.nextLine();
            System.out.println("Please enter ScheduledStartTime");
            String scheduledStartTime = scanner.nextLine();

            String columnName = (option.equals("BUS")) ? "BusID" : "DriverName";

            String sql = "UPDATE TripOffering "
                    + "SET " + columnName + " = ? "
                    + "WHERE TripNumber = ? AND Date = ? AND ScheduledStartTime = ?";

            PreparedStatement pstmt = statement.getConnection().prepareStatement(sql);
            pstmt.setString(1, info);
            pstmt.setString(2, tripNumber);
            pstmt.setString(3, date);
            pstmt.setString(4, scheduledStartTime);

            int row = pstmt.executeUpdate();
            if (row == 1) {
                System.out.println("Updated " + columnName + " successfully.");
            } else {
                System.out.println("Updated " + columnName + " unsuccessfully.");
            }
        } catch (SQLException e) {
            handleSQLException(e, "changing bus or driver");
        }
    }

    /**
     * Displays the stops for a specific trip.
     * 
     * @param statement JDBC Statement object
     * @param scanner   Scanner object for user input
     */
    public static void displayStop(Statement statement, Scanner scanner) {
        try {
            System.out.println("Please enter the Trip number");
            String tripNumber = scanner.nextLine().trim();

            String sql = "SELECT * FROM TripStopInfo WHERE TripNumber = ? ORDER BY SequenceNumber ASC";
            PreparedStatement pstmt = statement.getConnection().prepareStatement(sql);
            pstmt.setString(1, tripNumber);

            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No stops found for trip number " + tripNumber);
                return;
            }

            printResultSet(rs);
        } catch (SQLException e) {
            handleSQLException(e, "displaying trip stop information");
        }
    }

    /**
     * Displays the weekly schedule for a driver.
     * 
     * @param statement JDBC Statement object
     * @param scanner   Scanner object for user input
     */
    public static void displayWeeklySchedule(Statement statement, Scanner scanner) {
        try {
            System.out.println("Please enter driver name");
            String driverName = scanner.nextLine().trim();
            System.out.println("Please enter date (MM/dd/yyyy)");
            String dateString = scanner.nextLine().trim();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Calendar calDate = Calendar.getInstance();
            calDate.setTime(sdf.parse(dateString));

            for (int i = 1; i <= 7; i++) {
                dateString = sdf.format(calDate.getTime());
                System.out.println("######### Day " + i + " ##########");
                String sql = "SELECT TripNumber, Date, ScheduledStartTime, ScheduleArrivalTimeDestination, BusID "
                        + "FROM TripOffering "
                        + "WHERE DriverName = ? AND Date = ?";
                PreparedStatement pstmt = statement.getConnection().prepareStatement(sql);
                pstmt.setString(1, driverName);
                pstmt.setString(2, dateString);

                ResultSet rs = pstmt.executeQuery();

                if (!rs.isBeforeFirst()) {
                    System.out.println("No schedule for " + driverName + " on " + dateString);
                } else {
                    printResultSet(rs);
                }
                calDate.add(Calendar.DATE, 1);
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
            System.out.println("Exception occurred while displaying weekly schedule.");
        }
    }

    /**
     * Adds a new driver to the system.
     * 
     * @param statement JDBC Statement object
     * @param scanner   Scanner object for user input
     */
    public static void addDriver(Statement statement, Scanner scanner) {
        try {
            System.out.println("Please enter driver name");
            String driverName = scanner.nextLine().trim();
            System.out.println("Please enter driver telephone number");
            String telephoneNumber = scanner.nextLine().trim();

            String sql = "INSERT INTO Driver (DriverName, DriverTelephoneNumber) VALUES (?, ?)";
            PreparedStatement pstmt = statement.getConnection().prepareStatement(sql);
            pstmt.setString(1, driverName);
            pstmt.setString(2, telephoneNumber);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Driver inserted successfully.");
            } else {
                System.out.println("Failed to insert driver.");
            }
        } catch (SQLException e) {
            handleSQLException(e, "adding driver");
        }
    }

    /**
     * Adds a new bus to the system.
     * 
     * @param statement JDBC Statement object
     * @param scanner   Scanner object for user input
     */
    public static void addBus(Statement statement, Scanner scanner) {
        try {
            System.out.println("Please enter bus ID");
            String busID = scanner.nextLine();
            System.out.println("Please enter model");
            String model = scanner.nextLine();
            System.out.println("Please enter year");
            String year = scanner.nextLine();

            String sql = "INSERT INTO Bus (BusID, Model, Year) VALUES (?, ?, ?)";
            PreparedStatement pstmt = statement.getConnection().prepareStatement(sql);
            pstmt.setString(1, busID);
            pstmt.setString(2, model);
            pstmt.setString(3, year);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Bus added successfully.");
            } else {
                System.out.println("Failed to add bus.");
            }
        } catch (SQLException e) {
            handleSQLException(e, "adding bus");
        }
    }

    /**
     * Deletes a bus from the system.
     * 
     * @param statement JDBC Statement object
     * @param scanner   Scanner object for user input
     */
    public static void deleteBus(Statement statement, Scanner scanner) {
        try {
            System.out.println("Please enter the Bus ID of the bus you want to delete");
            String busID = scanner.nextLine().trim();

            String sql = "DELETE FROM Bus WHERE BusID = ?";

            PreparedStatement pstmt = statement.getConnection().prepareStatement(sql);
            pstmt.setString(1, busID);

            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Bus with Bus ID " + busID + " has been successfully deleted.");
            } else {
                System.out.println("No bus found with Bus ID " + busID + ". Deletion failed.");
            }
        } catch (SQLException e) {
            handleSQLException(e, "deleting bus");
        }
    }

    /**
     * Inserts actual stop information for a trip.
     * 
     * @param statement JDBC Statement object
     * @param scanner   Scanner object for user input
     */
    public static void insertTripInfo(Statement statement, Scanner scanner) {
        try {
            System.out.println("Please enter trip number");
            String tripNumber = scanner.nextLine();
            System.out.println("Please enter date (MM/dd/yyyy)");
            String date = scanner.nextLine();
            System.out.println("Please enter schedule start time (HH:mm:ss)");
            String scheduleStartTime = scanner.nextLine();
            System.out.println("Please enter stop number");
            String stopNumber = scanner.nextLine();
            System.out.println("Please enter scheduled arrival time (HH:mm:ss)");
            String scheduleArrivalTime = scanner.nextLine();
            System.out.println("Please enter actual arriving time (HH:mm:ss)");
            String actualArrivalTime = scanner.nextLine();
            System.out.println("Please enter actual start time (HH:mm:ss)");
            String actualStartTime = scanner.nextLine();
            System.out.println("Please enter number of passengers in");
            int passIn = scanner.nextInt();
            System.out.println("Please enter number of passengers out");
            int passOut = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            String sql = "INSERT INTO ActualStopInfo (TripNumber, Date, ScheduledStartTime, StopNumber, " +
                    "ScheduledArrivalTime, ActualArrivalTime, ActualStartTime, PassengersIn, PassengersOut) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = statement.getConnection().prepareStatement(sql);
            pstmt.setString(1, tripNumber);
            pstmt.setString(2, date);
            pstmt.setString(3, scheduleStartTime);
            pstmt.setString(4, stopNumber);
            pstmt.setString(5, scheduleArrivalTime);
            pstmt.setString(6, actualArrivalTime);
            pstmt.setString(7, actualStartTime);
            pstmt.setInt(8, passIn);
            pstmt.setInt(9, passOut);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Actual stop information added successfully.");
            } else {
                System.out.println("Failed to add actual stop information.");
            }
        } catch (SQLException e) {
            handleSQLException(e, "adding actual stop information");
        }
    }

    /**
     * Prints the contents of a ResultSet.
     * 
     * @param rs ResultSet object to print
     * @throws SQLException if a database access error occurs
     */
    public static void printResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int colCount = rsMetaData.getColumnCount();

        // Print column names
        for (int i = 1; i <= colCount; i++) {
            System.out.print(rsMetaData.getColumnName(i) + "\t");
        }
        System.out.println();

        // Print row data
        while (rs.next()) {
            for (int i = 1; i <= colCount; i++) {
                System.out.print(rs.getString(i) + "\t");
            }
            System.out.println();
        }
    }

    /**
     * Handle SQLException by printing the stack trace and a custom error message.
     * 
     * @param e SQLException object to handle
     * @param action Description of the action being performed when the exception occurred
     */
    public static void handleSQLException(SQLException e, String action) {
        e.printStackTrace();
        System.out.println("SQL Exception occurred while " + action + ": " + e.getMessage());
    }
}