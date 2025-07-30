import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import models.Airline;
import models.Airport;
import models.Booking;
import models.Flight;
import models.*;
import services.*;
import utils.Database;
import java.util.regex.Pattern;
import java.util.regex.Matcher;




public class Main {
    public static void main(String[] args) {
        Database.loadUsersFromFile();
        Scanner scan = new Scanner(System.in);
        Userservice userservice = new Userservice();
        Flightservice flightservice = new Flightservice();
        Bookingservice bookingservice = new Bookingservice();

        while (true) {
            System.out.println("Select what action you want to perform:\n"
                    + "1: Create User\n"
                    + "2: Create Flight\n"
                    + "3: Create Booking\n"
                    + "4: List Users\n"
                    + "5: List Flights\n"
                    + "6: List Bookings\n"
                    + "7: Modify User\n"
                    + "8: Modify Flight\n"
                    + "9: Modify Booking\n"
                    + "0: Cancel Booking\n"
                    + "10: Exit\n");

            int choice = getIntInput(scan, "Enter choice:");

            switch (choice) {
                case 1:

                    String name = getValidFullName(scan, "Enter Full Name:");
                    String email = getValidEmail(scan, "Enter email address:");
                    String phoneNumber = getValidCanadianPhoneNumber(scan);
                    String username = getValidUsername(scan, "Enter username (exactly 8 characters, letters and digits only):");
                    String password = getValidPassword(scan, "Enter password (exactly 8 characters, with at least 1 uppercase letter and 1 symbol):");
                    String role = getValidRole(scan, "Enter user role: customer / staff / admin");

                    switch (role) {
                        case "customer":

                            String student = validateStudent(scan, "Student? Yes/No");
                            String dateOfBirth = getValidBirthdayDateOnly(scan, "Enter date of birth (YYYY/MM/DD):");
                            String nationality = getValidNationality(scan, "Enter nationality:");
                            String passportNumber = getValidPassportNumber(scan, nationality);
                            userservice.createCustomer(name, email, phoneNumber, username, password, student, dateOfBirth, nationality, passportNumber);
                            Database.saveUsersToFile();
                            break;

                        case "admin":
//                            System.out.println("Enter badge number");
                            String adminBadge = getValidBadgeNumber(scan, "admin");
                            userservice.createAdmin(name, email, phoneNumber, username, password, adminBadge);
                            Database.saveUsersToFile();
                            break;

                        case "staff":
//                            System.out.println("Enter badge number");
                            String staffBadge = getValidBadgeNumber(scan, "staff");
                            userservice.createStaff(name, email, phoneNumber, username, password, staffBadge);
                            Database.saveUsersToFile();
                            break;

                        default:
                            System.out.println("❌ Invalid role entered.");
                            break;
                    }
                    break;

                case 2:
                    if (Database.airlines.isEmpty()) {
                        System.out.println("⚠️ No airlines available. Please create an airline first.");
                        break;
                    }

                    System.out.println("Available Airlines:");
                    for (Airline a : Database.airlines) {
                        System.out.println("ID: " + a.getAirlineID() + " | Name: " + a.getName());
                    }

                    if (Database.airports.isEmpty()) {
                        System.out.println("⚠️ No airports available. Please create airports first.");
                        break;
                    }

                    System.out.println("Available Airports:");
                    for (Airport ap : Database.airports) {
                        System.out.println("ID: " + ap.getAirportID() + " | Name: " + ap.getName() + " (" + ap.getAirportCode() + ")");
                    }

                    int airlineID = getIntInput(scan, "Enter airline ID (number):");
                    int sourceAirportID = getIntInput(scan, "Enter source airport ID:");
                    int destAirportID = getIntInput(scan, "Enter destination airport ID:");
                    String departure = getValidDate(scan, "Enter departure time (YYYY/MM/DD HH:mm):");
                    String arrival = getValidDate(scan, "Enter arrival time (YYYY/MM/DD HH:mm):");
                    int capacity = getIntInput(scan, "Enter capacity:");
                    double price = getDoubleInput(scan, "Enter price:");

                    flightservice.createFlight(airlineID, sourceAirportID, destAirportID, departure, arrival, capacity, price);

                    break;

                case 3:
                    int flightId = getIntInput(scan, "Enter Flight ID:");
                    int userId = getIntInput(scan, "Enter User ID:");

                    if (getFlightById(flightId) == null) {
                        System.out.println("❌ Flight does not exist.");
                        break;
                    }
                    if (getUserById(userId) == null) {
                        System.out.println("❌ User does not exist.");
                        break;
                    }

                    bookingservice.createBooking(flightId, userId);
                    System.out.println("✅ Booking created.");
                    break;

                case 4:
                    userservice.listUsers();
                    break;

                case 5:
                    flightservice.listFlights();
                    break;

                case 6:
                    bookingservice.listBookings();
                    break;

                case 7:
                    int userIDToModify = getIntInput(scan, "Enter User ID to modify:");
                    User userToModify = getUserById(userIDToModify);

                    if (userToModify == null) {
                        System.out.println("❌ User not found.");
                        break;
                    }

                    System.out.println("User found: " + userToModify);
                    System.out.println("Which field do you want to modify? (name, email, phone, password)");
                    String field = scan.nextLine().toLowerCase();

                    switch (field) {
                        case "name":
                            System.out.println("Enter new name:");
                            userToModify.setName(scan.nextLine());
                            Database.saveUsersToFile();
                            break;
                        case "email":
                            System.out.println("Enter new email:");
                            userToModify.setEmail(scan.nextLine());
                            Database.saveUsersToFile();
                            break;
                        case "phone":
                            System.out.println("Enter new phone number:");
                            userToModify.setPhone(scan.nextLine());
                            Database.saveUsersToFile();
                            break;
                        case "password":
                            System.out.println("Enter new password:");
                            userToModify.setPassword(scan.nextLine());
                            Database.saveUsersToFile();
                            break;
                        default:
                            System.out.println("Invalid field.");
                            break;
                    }

                    System.out.println("✅ User updated: " + userToModify);
                    break;

                case 8:
                    int modFlightID = getIntInput(scan, "Enter Flight ID to modify:");
                    Flight flight = getFlightById(modFlightID);
                    if (flight == null) {
                        System.out.println("❌ Flight not found.");
                        break;
                    }
                    String newDeparture = getValidDate(scan, "Enter new departure time (YYYY/MM/DD HH:mm):");
                    String newArrival = getValidDate(scan, "Enter new arrival time (YYYY/MM/DD HH:mm):");
                    flightservice.modifyFlight(modFlightID, newDeparture, newArrival);
                    break;

                case 9:
                    int bookingId = getIntInput(scan, "Enter Booking ID to modify:");
                    Booking booking = getBookingById(bookingId);
                    if (booking == null) {
                        System.out.println("❌ Booking not found.");
                        break;
                    }

                    int newFlightID = getIntInput(scan, "Enter new Flight ID:");
                    int newUserID = getIntInput(scan, "Enter new User ID:");
                    if (getFlightById(newFlightID) == null || getUserById(newUserID) == null) {
                        System.out.println("❌ Invalid flight or user ID.");
                        break;
                    }
                    bookingservice.modifyBooking(bookingId, newFlightID, newUserID);
                    break;

                case 0:
                    int bookingID = getIntInput(scan, "Enter Booking ID to cancel:");
                    if (getBookingById(bookingID) == null) {
                        System.out.println("❌ Booking not found.");
                        break;
                    }
                    bookingservice.cancelBooking(bookingID);
                    break;

                case 10:
                    System.out.println("Goodbye!");
                    Database.saveUsersToFile();
                    scan.close();
                    return;

                default:
                    System.out.println("Invalid Input");
                    break;
            }
        }
    }

    // --------- Helper Methods ---------

    public static int getIntInput(Scanner scan, String prompt) {
        while (true) {
            System.out.println(prompt);
            if (scan.hasNextInt()) {
                int input = scan.nextInt();
                scan.nextLine(); // consume newline
                return input;
            } else {
                System.out.println("❌ Invalid number. Please enter a valid integer.");
                scan.nextLine(); // consume invalid input
            }
        }
    }

    public static double getDoubleInput(Scanner scan, String prompt) {
        while (true) {
            System.out.println(prompt);
            if (scan.hasNextDouble()) {
                double input = scan.nextDouble();
                scan.nextLine(); // consume newline
                return input;
            } else {
                System.out.println("❌ Invalid number. Please enter a valid decimal number.");
                scan.nextLine(); // consume invalid input
            }
        }
    }

    public static String getValidDate(Scanner scan, String prompt) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        format.setLenient(false);
        while (true) {
            System.out.println(prompt);
            String input = scan.nextLine();
            try {
                format.parse(input);
                return input;
            } catch (ParseException e) {
                System.out.println("❌ Invalid date format. Please use YYYY/MM/DD HH:mm");
            }
        }
    }
    public static String getValidBirthdayDateOnly(Scanner scan, String prompt) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        format.setLenient(false);
        while (true) {
            System.out.println(prompt);
            String input = scan.nextLine();
            try {
                format.parse(input);
                return input;
            } catch (ParseException e) {
                System.out.println("❌ Invalid date format. Please use YYYY/MM/DD");
            }
        }
    }
    public static String getValidRole(Scanner scan, String prompt) {
        while (true) {
            System.out.println(prompt);
            String role = scan.nextLine().trim().toLowerCase();
            if (role.equals("customer") || role.equals("staff") || role.equals("admin")) {
                return role;
            } else {
                System.out.println("❌ Invalid role. Please enter one of: customer / staff / admin");
            }
        }
    }
    public static String getValidFullName(Scanner scan, String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = scan.nextLine();
            if (input.matches("^[A-Za-z ]+$")) {
                return input;
            } else {
                System.out.println("❌ Invalid name. Only letters and spaces are allowed.");
            }
        }
    }
    public static String getValidNationality(Scanner scan, String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = scan.nextLine();
            if (input.matches("^[A-Za-z]+$")) {
                return input;
            } else {
                System.out.println("❌ Invalid nationality. Only letters are allowed.");
            }
        }
    }
    public static String getValidCanadianPhoneNumber(Scanner scan) {
        // Pattern: optional "+1", then exactly 10 digits
        String pattern = "^(\\+1)?\\d{10}$";
        Pattern regex = Pattern.compile(pattern);

        while (true) {
            System.out.println("Enter Canadian phone number; exactly 10 digits:");
            String input = scan.nextLine().trim();
            Matcher matcher = regex.matcher(input);
            if (matcher.matches()) {
                return input;
            } else {
                System.out.println("❌ Invalid Canadian phone number format. Please enter exactly 10 digits, optionally preceded by +1.");
            }
        }
    }




    public static String getValidEmail(Scanner scan, String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = scan.nextLine().toLowerCase();
            if (input.matches("^[A-Za-z0-9._%+-]+@(gmail|hotmail|yahoo|icloud|outlook)\\.com$")) {
                return input;
            } else {
                System.out.println("❌ Invalid email. Allowed domains: gmail.com, hotmail.com, yahoo.com, icloud.com");
            }
        }
    }
    public static String getValidUsername(Scanner scan, String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = scan.nextLine();
            if (input.matches("^[A-Za-z0-9]{8}$")) {
                return input;
            } else {
                System.out.println("❌ Invalid username. It must be exactly 8 characters long and contain only letters and digits.");
            }
        }
    }
    public static String getValidPassword(Scanner scan, String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = scan.nextLine();
            if (input.length() == 8 &&
                    input.matches(".*[A-Z].*") &&      // At least one uppercase
                    input.matches(".*[^A-Za-z0-9].*")) // At least one symbol
            {
                return input;
            } else {
                System.out.println("❌ Invalid password. It must be exactly 8 characters and include at least 1 uppercase letter and 1 symbol.");
            }
        }
    }

    public static String validateStudent(Scanner scan, String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = scan.nextLine().trim().toLowerCase();
            if (input.equals("yes") || input.equals("no")) {
                return input;
            } else {
                System.out.println("❌ Invalid input. Please enter 'Yes' or 'No'.");
            }
        }
    }
    public static String getValidPassportNumber(Scanner scan, String nationality) {
        String lowerNationality = nationality.toLowerCase();
        String pattern = Database.passportPatterns.get(lowerNationality);

        if (pattern == null) {
            System.out.println("❌ No passport pattern defined for nationality: " + nationality);
            return null;
        }

        Pattern regex = Pattern.compile(pattern);

        while (true) {
            System.out.println("Enter passport number for " + nationality + ":");
            String input = scan.nextLine().trim().toUpperCase();  // Normalize input if needed
            Matcher matcher = regex.matcher(input);
            if (matcher.matches()) {
                return input;
            } else {
                System.out.println("❌ Invalid passport number format for " + nationality + ".");
            }
        }
    }
    public static String getValidBadgeNumber(Scanner scan, String role) {
        String prefix = role.equals("admin") ? "A" : "S";
        String pattern = "^" + prefix + "\\d{9}$"; // e.g. A + 9 digits or S + 9 digits

        Pattern regex = Pattern.compile(pattern);

        while (true) {
            System.out.println("Enter badge number (must start with '" + prefix + "' and be exactly 10 characters):");
            String input = scan.nextLine().trim();
            Matcher matcher = regex.matcher(input);
            if (matcher.matches()) {
                return input;
            } else {
                System.out.println("❌ Invalid badge number format. Please try again.");
            }
        }
    }



    public static User getUserById(int id) {
        for (User user : Database.users) {
            if (user.getId() == id) return user;
        }
        return null;
    }

    public static Flight getFlightById(int id) {
        for (Flight flight : Database.flights) {
            if (flight.getId() == id) return flight;
        }
        return null;
    }

    public static Booking getBookingById(int id) {
        for (Booking booking : Database.bookings) {
            if (booking.getBookingID() == id) return booking;
        }
        return null;
    }


}
