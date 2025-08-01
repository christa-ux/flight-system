package services;

import utils.Database;
import models.*;


public class Bookingservice {
    public Booking createBooking(int flightID, int userID) {
        int bookingID = Database.bookingCount++;
        Booking booking = new Booking(bookingID, flightID, userID);
        Database.bookings.add(booking);
        return booking;
    }

    public void listBookings() {
        for (Booking booking : Database.bookings) {
            System.out.println(booking);
        }
    }


    public void modifyBooking(int bookingID, int newFlightID, int newUserID) {
    Booking booking = findBookingById(bookingID);
    if (booking != null) {
        booking.setFlightID(newFlightID);
        booking.setUserID(newUserID);
        System.out.println("Booking updated: " + booking);
    } else {
        System.out.println("Booking not found.");
    }
}
public void cancelBooking(int bookingID) {
    Booking booking = findBookingById(bookingID);
    if (booking != null) {
        Database.bookings.remove(booking);
        System.out.println("Booking cancelled: " + booking);
    } else {
        System.out.println("Booking not found.");
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
}
