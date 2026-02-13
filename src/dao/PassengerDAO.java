package dao;

import database.DatabaseConnection;
import model.Passenger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PassengerDAO {

    public void create(Passenger passenger) throws SQLException {
        String sql = """
            INSERT INTO passengers (first_name, last_name, email, phone, passport_number)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, passenger.getFirstName());
            pstmt.setString(2, passenger.getLastName());
            pstmt.setString(3, passenger.getEmail());
            pstmt.setString(4, passenger.getPhone());
            pstmt.setString(5, passenger.getPassportNumber());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    passenger.setPassengerId(rs.getInt(1));
                }
            }
        }
    }

    public Passenger getById(int passengerId) throws SQLException {
        String sql = "SELECT * FROM passengers WHERE passenger_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, passengerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPassenger(rs);
                }
            }
        }
        return null;
    }

    public List<Passenger> getAll() throws SQLException {
        List<Passenger> passengers = new ArrayList<>();
        String sql = "SELECT * FROM passengers ORDER BY last_name, first_name";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                passengers.add(mapResultSetToPassenger(rs));
            }
        }
        return passengers;
    }

    public void update(Passenger passenger) throws SQLException {
        String sql = """
            UPDATE passengers SET first_name = ?, last_name = ?, email = ?,
                                 phone = ?, passport_number = ?
            WHERE passenger_id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, passenger.getFirstName());
            pstmt.setString(2, passenger.getLastName());
            pstmt.setString(3, passenger.getEmail());
            pstmt.setString(4, passenger.getPhone());
            pstmt.setString(5, passenger.getPassportNumber());
            pstmt.setInt(6, passenger.getPassengerId());

            pstmt.executeUpdate();
        }
    }

    public void delete(int passengerId) throws SQLException {
        String sql = "DELETE FROM passengers WHERE passenger_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, passengerId);
            pstmt.executeUpdate();
        }
    }

    public List<Passenger> search(String keyword) throws SQLException {
        List<Passenger> passengers = new ArrayList<>();
        String sql = """
            SELECT * FROM passengers
            WHERE first_name LIKE ? OR last_name LIKE ? OR email LIKE ? OR passport_number LIKE ?
            ORDER BY last_name, first_name
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
                    passengers.add(mapResultSetToPassenger(rs));
                }
            }
        }
        return passengers;
    }

    private Passenger mapResultSetToPassenger(ResultSet rs) throws SQLException {
        return new Passenger(
            rs.getInt("passenger_id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("passport_number")
        );
    }
}
