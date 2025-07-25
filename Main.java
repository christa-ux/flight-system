import java.util.*;
import models.Airline;
import models.Airport;
import models.User;
import services.*;
import utils.Database;

public class Main  {
    public static void main (String[] args) {
        Scanner scan = new Scanner(System.in);
        Userservice userservice = new Userservice(); 
        Flightservice flightservice = new Flightservice();
        Bookingservice bookingservice = new Bookingservice();

        while (true) {  // Loop to keep prompting until exit
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
                    + "10: Exit\n"
                    );

            int choice = scan.nextInt();
            scan.nextLine(); // consume leftover newline

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

                    switch(role) {
                        case "customer":
                            System.out.println("Student? Yes/No");
                            String student = scan.nextLine();
                            System.out.println("Enter date of birth in this format: YYYY/MM/DD");
                            String dateOfBirth = scan.nextLine();
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
                            System.out.println("Invalid role entered.");
                            break;
                    }
                    break;  // Break case 1

                case 2:
                    // You can add Flight creation logic here (similarly fix input)
                    
    // List airlines
    if (Database.airlines.isEmpty()) {
        System.out.println("⚠️ No airlines available. Please create an airline first.");
        break;
    }
    System.out.println("Available Airlines:");
    for (Airline a : Database.airlines) {
        System.out.println("ID: " + a.getAirlineID() + " | Name: " + a.getName());
    }

    // List airports
    if (Database.airports.isEmpty()) {
        System.out.println("⚠️ No airports available. Please create airports first.");
        break;
    }
    System.out.println("Available Airports:");
    for (Airport ap : Database.airports) {
        System.out.println("ID: " + ap.getAirportID() + " | Name: " + ap.getName() + " (" + ap.getAirportCode() + ")");
    }

    // Prompt for flight details
    System.out.println("Enter airline ID (number):");
    int airlineID = scan.nextInt();
    scan.nextLine();

    System.out.println("Enter source airport ID:");
    int sourceAirportID = scan.nextInt();
    scan.nextLine();

    System.out.println("Enter destination airport ID:");
    int destAirportID = scan.nextInt();
    scan.nextLine();

    System.out.println("Enter departure time (YYYY/MM/DD HH:mm):");
    String departure = scan.nextLine();

    System.out.println("Enter arrival time (YYYY/MM/DD HH:mm):");
    String arrival = scan.nextLine();

    System.out.println("Enter capacity:");
    int capacity = scan.nextInt();

    System.out.println("Enter price:");
    double price = scan.nextDouble();
    scan.nextLine();

    // Create the flight
    flightservice.createFlight(
        airlineID,
        sourceAirportID,
        destAirportID,
        departure,
        arrival,
        capacity,
        price
    );
    break;


                case 3:
                    // Booking creation logic here
                    System.out.println("Enter Flight ID:");
                    int flightId = scan.nextInt();
                    System.out.println("Enter User ID:");
                    int userId = scan.nextInt();
                    scan.nextLine();
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
    System.out.println("Enter User ID to modify:");
    int userIDToModify = scan.nextInt();
    scan.nextLine();

    User userToModify = null;
    for (User user : Database.users) {
        if (user.getId() == userIDToModify) {
            userToModify = user;
            break;
        }
    }

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
            String newName = scan.nextLine();
            userToModify.setName(newName);
            break;
        case "email":
            System.out.println("Enter new email:");
            String newEmail = scan.nextLine();
            userToModify.setEmail(newEmail);
            break;
        case "phone":
            System.out.println("Enter new phone number:");
            String newPhone = scan.nextLine();
            userToModify.setPhone(newPhone);
            break;
        case "password":
            System.out.println("Enter new password:");
            String newPassword = scan.nextLine();
            userToModify.setPassword(newPassword);
            break;
        default:
            System.out.println("Invalid field.");
            break;
    }

    System.out.println("✅ User updated: " + userToModify);
    break;


                case 8:
                    // Modify flight logic
                   System.out.println("Enter Flight ID to modify:");
                    int modFlightID = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Enter new departure time (YYYY/MM/DD HH:mm):");
                    String newDeparture = scan.nextLine();
                    System.out.println("Enter new arrival time (YYYY/MM/DD HH:mm):");
                    String newArrival = scan.nextLine();
                    flightservice.modifyFlight(modFlightID, newDeparture, newArrival);
                    break;


                case 9:
                    // Modify booking logic
                   System.out.println("Enter Booking ID to modify:");
                    int bookingId = scan.nextInt();
                    System.out.println("Enter new Flight ID:");
                    int newFlightID = scan.nextInt();
                    System.out.println("Enter new User ID:");
                    int newUserID = scan.nextInt();
                    scan.nextLine();
                    bookingservice.modifyBooking(bookingId, newFlightID, newUserID);
                    break;

                case 0:
                    System.out.println("Enter Booking ID to cancel:");
                    int bookingID = scan.nextInt();
                    scan.nextLine();
                    bookingservice.cancelBooking(bookingID);
                    break;

                case 10:
                    System.out.println("Goodbye!");
                    scan.close();
                    return;  // Exit main method

                default:
                    System.out.println("Invalid Input");
                    break;
            }
        }
    }
}
