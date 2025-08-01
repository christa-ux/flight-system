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
        Database.loadFlightsFromFile();
        Database.loadBookingsFromFile();


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
                            if (isDuplicateUser(username, email, phoneNumber, passportNumber, null)) {
                                System.out.println("❌ User creation aborted due to duplicate information.");
                                break;
                            }
                            userservice.createCustomer(name, email, phoneNumber, username, password, student, dateOfBirth, nationality, passportNumber);
                            Database.saveUsersToFile();
                            break;

                        case "admin":
//                            System.out.println("Enter badge number");
                            String adminBadge = getValidBadgeNumber(scan, "admin",false);
                            if (isDuplicateUser(username, email, phoneNumber, null, adminBadge)) {
                                System.out.println("❌ Admin creation aborted due to duplicate information.");
                                break;
                            }
                            userservice.createAdmin(name, email, phoneNumber, username, password, adminBadge);
                            Database.saveUsersToFile();
                            break;

                        case "staff":
//                            System.out.println("Enter badge number");
                            String staffBadge = getValidBadgeNumber(scan, "staff",false);
                            if (isDuplicateUser(username, email, phoneNumber, null, staffBadge)) {
                                System.out.println("❌ Staff creation aborted due to duplicate information.");
                                break;
                            }
                            userservice.createStaff(name, email, phoneNumber, username, password, staffBadge);
                            Database.saveUsersToFile();
                            break;

                        default:
                            System.out.println("❌ Invalid role entered.");
                            break;
                    }
                    break;

                case 2:
                    // 🔁 1️⃣ Admin Badge Verification with loop
                    boolean isAdminValid = false;
                    String enteredAdminBadge;

                    do {
                        enteredAdminBadge = getValidBadgeNumber(scan, "admin", true);
                        for (User user : Database.users) {
                            if (user instanceof Admin && ((Admin) user).getAdminBadge().equals(enteredAdminBadge)) {
                                isAdminValid = true;
                                break;
                            }
                        }

                        if (!isAdminValid) {
                            System.out.println("❌ Invalid or unauthorized admin badge number. Please try again.");
                        }
                    } while (!isAdminValid);

                    // 2️⃣ Airline & Airport Availability Check
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

                    // 3️⃣ Flight Info Input
                    int airlineID = getIntInput(scan, "Enter airline ID (number):");

                    int sourceAirportID;
                    int destAirportID;

                    // 🔁 Keep asking until source ≠ destination
                    do {
                        sourceAirportID = getIntInput(scan, "Enter source airport ID:");
                        destAirportID = getIntInput(scan, "Enter destination airport ID:");

                        if (sourceAirportID == destAirportID) {
                            System.out.println("❌ Source and destination airports cannot be the same. Please enter different IDs.");
                        }
                    } while (sourceAirportID == destAirportID);

                    String departure = getValidDate(scan, "Enter departure time (YYYY/MM/DD HH:mm):");
                    String arrival = getValidDate(scan, "Enter arrival time (YYYY/MM/DD HH:mm):");
                    int capacity = getIntInput(scan, "Enter capacity:");
                    double price = getDoubleInput(scan, "Enter price:");

                    // 4️⃣ Create Flight
                    flightservice.createFlight(airlineID, sourceAirportID, destAirportID, departure, arrival, capacity, price);
                    Database.saveFlightsToFile(); // <-- Save flights to file
                    System.out.println("✅ Flight successfully created.");

                    break;



                case 3:
                    // 🔐 Verify role is STAFF or ADMIN via badge
                    String badge = getValidBadgeNumber(scan, "staff or admin", true);
                    boolean isAuthorized = false;

                    for (User user : Database.users) {
                        if (user instanceof Staff && ((Staff) user).getStaffBadge().equals(badge)) {
                            isAuthorized = true;
                            break;
                        }
                        if (user instanceof Admin && ((Admin) user).getAdminBadge().equals(badge)) {
                            isAuthorized = true;
                            break;
                        }
                    }

                    if (!isAuthorized) {
                        System.out.println("❌ Unauthorized. Only staff or admin can create bookings.");
                        break;
                    }

                    // ✈️ Prompt for valid Flight ID
                    int flightId;
                    Flight selectedFlight;
                    do {
                        flightId = getIntInput(scan, "Enter Flight ID:");
                        selectedFlight = getFlightById(flightId);
                        if (selectedFlight == null) {
                            System.out.println("❌ Flight does not exist. Try again.");
                        }
                    } while (selectedFlight == null);

                    // 👤 Prompt for valid User ID
                    int userId;
                    User selectedUser;
                    do {
                        userId = getIntInput(scan, "Enter User ID:");
                        selectedUser = getUserById(userId);
                        if (selectedUser == null) {
                            System.out.println("❌ User does not exist. Try again.");
                        }
                    } while (selectedUser == null);

                    // ✅ Create booking with validations
                    bookingservice.createBooking(flightId, userId);
                    Database.saveBookingsToFile();
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
                    System.out.println("Which field do you want to modify? (name, email, phone, username, password)");
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
                        case "username":
                            String newUsername = getValidUsername(scan, "Enter new username (exactly 8 characters, letters and digits only):");
                            userToModify.setUsername(newUsername);
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
                    // Verify admin credentials first
                    String adminBadge = getValidBadgeNumber(scan, "admin", true);
                    boolean adminValid = false;
                    Admin adminUser = null;

                    // Check if the badge exists and belongs to an admin
                    for (User user : Database.users) {
                        if (user instanceof Admin && ((Admin) user).getAdminBadge().equals(adminBadge)) {
                            adminValid = true;
                            adminUser = (Admin) user;
                            break;
                        }
                    }

                    if (!adminValid) {
                        System.out.println("❌ Invalid admin credentials. Only authorized admins can modify flights.");
                        break;
                    }

                    // Proceed with flight modification if admin is verified
                    int modFlightID = getIntInput(scan, "Enter Flight ID to modify:");
                    Flight flight = getFlightById(modFlightID);
                    if (flight == null) {
                        System.out.println("❌ Flight not found.");
                        break;
                    }

                    System.out.println("Flight found. Current details:");
                    System.out.println("Departure: " + flight.getDepartureTime());
                    System.out.println("Arrival: " + flight.getArrivalTime());

                    String newDeparture = getValidDate(scan, "Enter new departure time (YYYY/MM/DD HH:mm):");
                    String newArrival = getValidDate(scan, "Enter new arrival time (YYYY/MM/DD HH:mm):");

                    flightservice.modifyFlight(modFlightID, newDeparture, newArrival);
                    Database.saveFlightsToFile();

                    // Log which admin made the change
                    System.out.println("✅ Flight successfully modified by admin: " + adminUser.getName() + " (" + adminBadge + ")");
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
                    Database.saveBookingsToFile();
                    break;

                case 0:
                    int bookingID = getIntInput(scan, "Enter Booking ID to cancel:");
                    if (getBookingById(bookingID) == null) {
                        System.out.println("❌ Booking not found.");
                        break;
                    }
                    bookingservice.cancelBooking(bookingID);
                    Database.saveBookingsToFile();
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
        String pattern = "^(\\+1)?\\d{10}$";
        Pattern regex = Pattern.compile(pattern);

        while (true) {
            System.out.println("Enter Canadian phone number; exactly 10 digits:");
            String input = scan.nextLine().trim();
            Matcher matcher = regex.matcher(input);
            if (!matcher.matches()) {
                System.out.println("❌ Invalid Canadian phone number format.");
                continue;
            }

            boolean exists = Database.users.stream()
                    .anyMatch(u -> u.getPhone().equals(input));
            if (exists) {
                System.out.println("❌ Phone number already exists.");
            } else {
                return input;
            }
        }
    }





    public static String getValidEmail(Scanner scan, String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = scan.nextLine().toLowerCase();
            if (!input.matches("^[A-Za-z0-9._%+-]+@(gmail|hotmail|yahoo|icloud|outlook)\\.com$")) {
                System.out.println("❌ Invalid email. Allowed domains: gmail.com, hotmail.com, yahoo.com, icloud.com");
                continue;
            }

            boolean exists = Database.users.stream()
                    .anyMatch(u -> u.getEmail().equalsIgnoreCase(input));
            if (exists) {
                System.out.println("❌ Email already exists.");
            } else {
                return input;
            }
        }
    }

    public static String getValidUsername(Scanner scan, String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = scan.nextLine();
            if (!input.matches("^[A-Za-z0-9]{8}$")) {
                System.out.println("❌ Invalid username. It must be exactly 8 characters long and contain only letters and digits.");
                continue;
            }
            // Check for duplicates
            boolean exists = Database.users.stream()
                    .anyMatch(u -> u.getUsername().equalsIgnoreCase(input));
            if (exists) {
                System.out.println("❌ Username already exists.");
            } else {
                return input;
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

        // Print the expected format based on nationality
        String formatMessage = "Expected passport format for " + nationality + ": ";
        switch (lowerNationality) {
            case "usa":
                formatMessage += "9 digits (e.g., 123456789)";
                break;
            case "uk":
                formatMessage += "9 alphanumeric characters (e.g., AB123CD45)";
                break;
            case "india":
                formatMessage += "1 letter followed by 7 digits (e.g., A1234567)";
                break;
            case "canada":
                formatMessage += "2 letters followed by 6 digits (e.g., AB123456)";
                break;
            case "germany":
                formatMessage += "9 characters from CFGHJKLMNPRTVWXYZ or digits (e.g., C12D34F56)";
                break;
            case "france":
                formatMessage += "2 digits, 2 letters, 5 digits (e.g., 12AB34567)";
                break;
            case "australia":
                formatMessage += "N followed by 7 digits (e.g., N1234567)";
                break;
            case "japan":
                formatMessage += "2 letters followed by 7 digits (e.g., AB1234567)";
                break;
            case "china":
                formatMessage += "E or G followed by 8 digits (e.g., E12345678)";
                break;
            case "brazil":
                formatMessage += "2 letters followed by 6 digits (e.g., AB123456)";
                break;
            case "southafrica":
                formatMessage += "13 digits (e.g., 1234567890123)";
                break;
            case "nigeria":
                formatMessage += "1 letter followed by 8 digits (e.g., A12345678)";
                break;
            case "russia":
                formatMessage += "2 digits, space, 6 digits (e.g., 12 345678)";
                break;
            case "italy":
                formatMessage += "2 letters followed by 7 digits (e.g., AB1234567)";
                break;
            case "mexico":
                formatMessage += "1 letter followed by 8 digits (e.g., A12345678)";
                break;
            default:
                formatMessage += "Please check your country's passport format";
        }

        System.out.println(formatMessage);

        Pattern regex = Pattern.compile(pattern);

        while (true) {
            System.out.println("Enter passport number for " + nationality + ":");
            String input = scan.nextLine().trim().toUpperCase();
            Matcher matcher = regex.matcher(input);
            if (!matcher.matches()) {
                System.out.println("❌ Invalid passport number format.");
                continue;
            }

            boolean exists = Database.users.stream()
                    .filter(u -> u instanceof Customer)
                    .map(u -> (Customer) u)
                    .anyMatch(c -> c.getPassportNumber().equalsIgnoreCase(input));

            if (exists) {
                System.out.println("❌ Passport number already exists.");
            } else {
                return input;
            }
        }
    }

//    public static String getValidBadgeNumber(Scanner scan, String role) {
//        String prefix = role.equals("admin") ? "A" : "S";
//        String pattern = "^" + prefix + "\\d{9}$"; // A + 9 digits or S + 9 digits
//
//        Pattern regex = Pattern.compile(pattern);
//
//        while (true) {
//            System.out.println("Enter badge number (must start with '" + prefix + "' and be exactly 10 characters):");
//            String input = scan.nextLine().trim();
//            Matcher matcher = regex.matcher(input);
//
//            if (!matcher.matches()) {
//                System.out.println("❌ Invalid badge number format.");
//                continue;
//            }
//
//            boolean exists = Database.users.stream()
//                    .anyMatch(u -> (role.equals("admin") && u instanceof Admin && ((Admin) u).getAdminBadge().equalsIgnoreCase(input)) ||
//                            (role.equals("staff") && u instanceof Staff && ((Staff) u).getStaffBadge().equalsIgnoreCase(input)));
//
//            if (exists) {
//                System.out.println("❌ Badge number already exists.");
//            } else {
//                return input;
//            }
//        }
//    }


    public static String getValidBadgeNumber(Scanner scan, String role, boolean mustExist) {
        String prefixPattern;
        if (role.equals("staff or admin")) {
            prefixPattern = "^[AS]\\d{9}$"; // A or S + 9 digits
        } else {
            String prefix = role.equals("admin") ? "A" : "S";
            prefixPattern = "^" + prefix + "\\d{9}$";
        }

        Pattern regex = Pattern.compile(prefixPattern);

        while (true) {
            System.out.println("Enter " + role + " badge (A or S + 9 digits):");
            String input = scan.nextLine().trim();
            Matcher matcher = regex.matcher(input);

            if (!matcher.matches()) {
                System.out.println("❌ Invalid badge number format.");
                continue;
            }

            boolean exists = Database.users.stream().anyMatch(u ->
                    (input.startsWith("A") && u instanceof Admin && ((Admin) u).getAdminBadge().equalsIgnoreCase(input)) ||
                            (input.startsWith("S") && u instanceof Staff && ((Staff) u).getStaffBadge().equalsIgnoreCase(input))
            );

            if (mustExist && !exists) {
                System.out.println("❌ Badge number does not exist.");
            } else if (!mustExist && exists) {
                System.out.println("❌ Badge number already exists.");
            } else {
                return input;
            }
        }
    }

    public static boolean isDuplicateUser(String username, String email, String phone, String passportNumber, String badgeNumber) {
        for (User user : Database.users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                System.out.println("❌ Username already exists.");
                return true;
            }
            if (user.getEmail().equalsIgnoreCase(email)) {
                System.out.println("❌ Email already exists.");
                return true;
            }
            if (user.getPhone().equals(phone)) {
                System.out.println("❌ Phone number already exists.");
                return true;
            }

            if (user instanceof Customer) {
                Customer cust = (Customer) user;
                if (cust.getPassportNumber().equalsIgnoreCase(passportNumber)) {
                    System.out.println("❌ Passport number already exists.");
                    return true;
                }
            }

            if (user instanceof Admin && badgeNumber != null) {
                if (((Admin) user).getAdminBadge().equalsIgnoreCase(badgeNumber)) {
                    System.out.println("❌ Admin badge number already exists.");
                    return true;
                }
            }

            if (user instanceof Staff && badgeNumber != null) {
                if (((Staff) user).getStaffBadge().equalsIgnoreCase(badgeNumber)) {
                    System.out.println("❌ Staff badge number already exists.");
                    return true;
                }
            }
        }
        return false;
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
    public static long getBookingCountForFlight(int flightId) {
        return Database.bookings.stream()
                .filter(b -> b.getFlightID() == flightId)
                .count();
    }
}
