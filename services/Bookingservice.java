package services;

import utils.Database;
import models.*;

public class Bookingservice {
    public Booking createBooking(int flightID, int userID) {
        // Check for existing booking with same user and flight
        if (isDuplicateBooking(flightID, userID)) {
            System.out.println("❌ This user already has a booking for this flight.");
            return null;
        }

        // Check flight capacity
        Flight flight = getFlightById(flightID);
        if (flight != null && isFlightFull(flightID, flight.getCapacity())) {
            System.out.println("❌ Flight is already at full capacity.");
            return null;
        }

        // Create new booking if validations pass
        int bookingID = Database.bookingCount++;
        Booking booking = new Booking(bookingID, flightID, userID);
        Database.bookings.add(booking);
        System.out.println("✅ Booking created successfully. Booking ID: " + bookingID);
        return booking;
    }

    public void listBookings() {
        if (Database.bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.println("List of Bookings:");
        for (Booking booking : Database.bookings) {
            System.out.println(booking);
        }
    }

    public void modifyBooking(int bookingID, int newFlightID, int newUserID) {
        Booking booking = findBookingById(bookingID);
        if (booking == null) {
            System.out.println("❌ Booking not found.");
            return;
        }

        // Check for duplicate booking with new parameters
        if (isDuplicateBooking(newFlightID, newUserID)) {
            System.out.println("❌ The user already has a booking for this flight.");
            return;
        }

        // Check capacity for new flight if changing flight
        if (booking.getFlightID() != newFlightID) {
            Flight newFlight = getFlightById(newFlightID);
            if (newFlight == null) {
                System.out.println("❌ New flight does not exist.");
                return;
            }
            if (isFlightFull(newFlightID, newFlight.getCapacity())) {
                System.out.println("❌ New flight is already at full capacity.");
                return;
            }
        }

        booking.setFlightID(newFlightID);
        booking.setUserID(newUserID);
        System.out.println("✅ Booking updated: " + booking);
    }

    public void cancelBooking(int bookingID) {
        Booking booking = findBookingById(bookingID);
        if (booking != null) {
            Database.bookings.remove(booking);
            System.out.println("✅ Booking cancelled: " + booking);
        } else {
            System.out.println("❌ Booking not found.");
        }
    }

    private Booking findBookingById(int bookingID) {
        for (Booking booking : Database.bookings) {
            if (booking.getBookingID() == bookingID) {
                return booking;
            }
        }
        return null;
    }

    // Helper method to check for duplicate bookings
    private boolean isDuplicateBooking(int flightID, int userID) {
        return Database.bookings.stream()
                .anyMatch(b -> b.getFlightID() == flightID && b.getUserID() == userID);
    }

    // Helper method to check if flight is full
    private boolean isFlightFull(int flightID, int capacity) {
        long bookingsCount = Database.bookings.stream()
                .filter(b -> b.getFlightID() == flightID)
                .count();
        return bookingsCount >= capacity;
    }

    // Helper method to get flight by ID
    private Flight getFlightById(int flightID) {
        return Database.flights.stream()
                .filter(f -> f.getId() == flightID)
                .findFirst()
                .orElse(null);
    }
}