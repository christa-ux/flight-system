import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import models.Airline;
import models.Airport;
import models.Booking;
import models.Flight;
import models.User;
import services.*;
import utils.Database;

public class Main {
    public static void main(String[] args) {
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
                    System.out.println("Enter Full Name: ");
                    String name = scan.nextLine();
                    System.out.println("Enter email address");
                    String email = scan.nextLine();
                    System.out.println("Enter phone number");
                    String phoneNumber = scan.nextLine();
                    System.out.println("Enter username");
                    String username = scan.nextLine();
                    System.out.println("Enter password");
                    String password = scan.nextLine();
                    System.out.println("Enter user role: customer / staff / admin");
                    String role = scan.nextLine().toLowerCase();

                    switch (role) {
                        case "customer":
                            System.out.println("Student? Yes/No");
                            String student = scan.nextLine();
                            String dateOfBirth = getValidDate(scan, "Enter date of birth (YYYY/MM/DD):");
                            System.out.println("Enter nationality");
                            String nationality = scan.nextLine();
                            System.out.println("Enter passport number");
                            String passportNumber = scan.nextLine();
                            userservice.createCustomer(name, email, phoneNumber, username, password, student, dateOfBirth, nationality, passportNumber);
                            break;

                        case "admin":
                            System.out.println("Enter badge number");
                            String adminBadge = scan.nextLine();
                            userservice.createAdmin(name, email, phoneNumber, username, password, adminBadge);
                            break;

                        case "staff":
                            System.out.println("Enter badge number");
                            String staffBadge = scan.nextLine();
                            userservice.createStaff(name, email, phoneNumber, username, password, staffBadge);
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
                            break;
                        case "email":
                            System.out.println("Enter new email:");
                            userToModify.setEmail(scan.nextLine());
                            break;
                        case "phone":
                            System.out.println("Enter new phone number:");
                            userToModify.setPhone(scan.nextLine());
                            break;
                        case "password":
                            System.out.println("Enter new password:");
                            userToModify.setPassword(scan.nextLine());
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
