package models;
public class Booking {
    protected int bookingID;
    protected int flightID;
    protected int userID;

    public Booking(int bookingID, int flightID, int userID) {
        this.bookingID = bookingID;
        this.flightID = flightID;
        this.userID = userID;
    }

    public int getBookingID() {
        return bookingID;
    }

    public int getFlightID() {
        return flightID;
    }

    public int getUserID() {
        return userID;
    }

 public void setFlightID(int flightID) {
    this.flightID = flightID;
}

public void setUserID(int userID) {
    this.userID = userID;
}


    @Override
    public String toString() {
        return "Booking{" +
                "bookingID=" + bookingID +
                ", flightID=" + flightID +
                ", userID=" + userID +
                '}';
    }
}
