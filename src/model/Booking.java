package model;

public class Booking {
    private int bookingId;
    private int flightId;
    private int passengerId;
    private String bookingDate;
    private String seatNumber;
    private String status;

    // For display purposes
    private String flightInfo;
    private String passengerInfo;

    public Booking() {}

    public Booking(int bookingId, int flightId, int passengerId,
                   String bookingDate, String seatNumber, String status) {
        this.bookingId = bookingId;
        this.flightId = flightId;
        this.passengerId = passengerId;
        this.bookingDate = bookingDate;
        this.seatNumber = seatNumber;
        this.status = status;
    }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getFlightId() { return flightId; }
    public void setFlightId(int flightId) { this.flightId = flightId; }

    public int getPassengerId() { return passengerId; }
    public void setPassengerId(int passengerId) { this.passengerId = passengerId; }

    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFlightInfo() { return flightInfo; }
    public void setFlightInfo(String flightInfo) { this.flightInfo = flightInfo; }

    public String getPassengerInfo() { return passengerInfo; }
    public void setPassengerInfo(String passengerInfo) { this.passengerInfo = passengerInfo; }

    @Override
    public String toString() {
        return "Booking #" + bookingId + " - Seat " + seatNumber;
    }
}
