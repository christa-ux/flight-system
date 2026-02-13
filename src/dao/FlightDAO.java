package dao;

import database.DatabaseConnection;
import model.Flight;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlightDAO {

    public void create(Flight flight) throws SQLException {
        String sql = """
            INSERT INTO flights (flight_number, airline, origin, destination,
                                departure_time, arrival_time, total_seats, available_seats, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, flight.getFlightNumber());
            pstmt.setString(2, flight.getAirline());
            pstmt.setString(3, flight.getOrigin());
            pstmt.setString(4, flight.getDestination());
            pstmt.setString(5, flight.getDepartureTime());
            pstmt.setString(6, flight.getArrivalTime());
            pstmt.setInt(7, flight.getTotalSeats());
            pstmt.setInt(8, flight.getAvailableSeats());
            pstmt.setString(9, flight.getStatus());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    flight.setFlightId(rs.getInt(1));
                }
            }
        }
    }

    public Flight getById(int flightId) throws SQLException {
        String sql = "SELECT * FROM flights WHERE flight_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, flightId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFlight(rs);
                }
            }
        }
        return null;
    }

    public List<Flight> getAll() throws SQLException {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights ORDER BY departure_time";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                flights.add(mapResultSetToFlight(rs));
            }
        }
        return flights;
    }

    public void update(Flight flight) throws SQLException {
        String sql = """
            UPDATE flights SET flight_number = ?, airline = ?, origin = ?,
                              destination = ?, departure_time = ?, arrival_time = ?,
                              total_seats = ?, available_seats = ?, status = ?
            WHERE flight_id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, flight.getFlightNumber());
            pstmt.setString(2, flight.getAirline());
            pstmt.setString(3, flight.getOrigin());
            pstmt.setString(4, flight.getDestination());
            pstmt.setString(5, flight.getDepartureTime());
            pstmt.setString(6, flight.getArrivalTime());
            pstmt.setInt(7, flight.getTotalSeats());
            pstmt.setInt(8, flight.getAvailableSeats());
            pstmt.setString(9, flight.getStatus());
            pstmt.setInt(10, flight.getFlightId());

            pstmt.executeUpdate();
        }
    }

    public void delete(int flightId) throws SQLException {
        String sql = "DELETE FROM flights WHERE flight_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, flightId);
            pstmt.executeUpdate();
        }
    }

    public List<Flight> search(String keyword) throws SQLException {
        List<Flight> flights = new ArrayList<>();
        String sql = """
            SELECT * FROM flights
            WHERE flight_number LIKE ? OR airline LIKE ? OR origin LIKE ? OR destination LIKE ?
            ORDER BY departure_time
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
                    flights.add(mapResultSetToFlight(rs));
                }
            }
        }
        return flights;
    }

    public void updateAvailableSeats(int flightId, int change) throws SQLException {
        String sql = "UPDATE flights SET available_seats = available_seats + ? WHERE flight_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, change);
            pstmt.setInt(2, flightId);
            pstmt.executeUpdate();
        }
    }

    private Flight mapResultSetToFlight(ResultSet rs) throws SQLException {
        return new Flight(
            rs.getInt("flight_id"),
            rs.getString("flight_number"),
            rs.getString("airline"),
            rs.getString("origin"),
            rs.getString("destination"),
            rs.getString("departure_time"),
            rs.getString("arrival_time"),
            rs.getInt("total_seats"),
            rs.getInt("available_seats"),
            rs.getString("status")
        );
    }
}
