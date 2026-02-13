package dao;

import database.DatabaseConnection;
import model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public void create(Booking booking) throws SQLException {
        String sql = """
            INSERT INTO bookings (flight_id, passenger_id, booking_date, seat_number, status)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, booking.getFlightId());
            pstmt.setInt(2, booking.getPassengerId());
            pstmt.setString(3, booking.getBookingDate());
            pstmt.setString(4, booking.getSeatNumber());
            pstmt.setString(5, booking.getStatus());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    booking.setBookingId(rs.getInt(1));
                }
            }
        }
    }

    public Booking getById(int bookingId) throws SQLException {
        String sql = """
            SELECT b.*, f.flight_number, f.origin, f.destination,
                   p.first_name, p.last_name
            FROM bookings b
            JOIN flights f ON b.flight_id = f.flight_id
            JOIN passengers p ON b.passenger_id = p.passenger_id
            WHERE b.booking_id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookingId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBooking(rs);
                }
            }
        }
        return null;
    }

    public List<Booking> getAll() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
            SELECT b.*, f.flight_number, f.origin, f.destination,
                   p.first_name, p.last_name
            FROM bookings b
            JOIN flights f ON b.flight_id = f.flight_id
            JOIN passengers p ON b.passenger_id = p.passenger_id
            ORDER BY b.booking_date DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        }
        return bookings;
    }

    public void update(Booking booking) throws SQLException {
        String sql = """
            UPDATE bookings SET flight_id = ?, passenger_id = ?,
                               booking_date = ?, seat_number = ?, status = ?
            WHERE booking_id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, booking.getFlightId());
            pstmt.setInt(2, booking.getPassengerId());
            pstmt.setString(3, booking.getBookingDate());
            pstmt.setString(4, booking.getSeatNumber());
            pstmt.setString(5, booking.getStatus());
            pstmt.setInt(6, booking.getBookingId());

            pstmt.executeUpdate();
        }
    }

    public void delete(int bookingId) throws SQLException {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookingId);
            pstmt.executeUpdate();
        }
    }

    public List<Booking> getByFlightId(int flightId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
            SELECT b.*, f.flight_number, f.origin, f.destination,
                   p.first_name, p.last_name
            FROM bookings b
            JOIN flights f ON b.flight_id = f.flight_id
            JOIN passengers p ON b.passenger_id = p.passenger_id
            WHERE b.flight_id = ?
            ORDER BY b.booking_date DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, flightId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        }
        return bookings;
    }

    public List<Booking> getByPassengerId(int passengerId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
            SELECT b.*, f.flight_number, f.origin, f.destination,
                   p.first_name, p.last_name
            FROM bookings b
            JOIN flights f ON b.flight_id = f.flight_id
            JOIN passengers p ON b.passenger_id = p.passenger_id
            WHERE b.passenger_id = ?
            ORDER BY b.booking_date DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, passengerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        }
        return bookings;
    }

    public List<Booking> search(String keyword) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
            SELECT b.*, f.flight_number, f.origin, f.destination,
                   p.first_name, p.last_name
            FROM bookings b
            JOIN flights f ON b.flight_id = f.flight_id
            JOIN passengers p ON b.passenger_id = p.passenger_id
            WHERE f.flight_number LIKE ? OR p.first_name LIKE ? OR p.last_name LIKE ?
                  OR b.seat_number LIKE ?
            ORDER BY b.booking_date DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        }
        return bookings;
    }

    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking(
            rs.getInt("booking_id"),
            rs.getInt("flight_id"),
            rs.getInt("passenger_id"),
            rs.getString("booking_date"),
            rs.getString("seat_number"),
            rs.getString("status")
        );

        // Set display info
        booking.setFlightInfo(rs.getString("flight_number") + " - " +
                             rs.getString("origin") + " to " + rs.getString("destination"));
        booking.setPassengerInfo(rs.getString("first_name") + " " + rs.getString("last_name"));

        return booking;
    }
}
