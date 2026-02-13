package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:flight_system.db";
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Create flights table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS flights (
                    flight_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    flight_number TEXT NOT NULL,
                    airline TEXT NOT NULL,
                    origin TEXT NOT NULL,
                    destination TEXT NOT NULL,
                    departure_time TEXT NOT NULL,
                    arrival_time TEXT NOT NULL,
                    total_seats INTEGER NOT NULL,
                    available_seats INTEGER NOT NULL,
                    status TEXT NOT NULL DEFAULT 'SCHEDULED'
                )
            """);

            // Create passengers table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS passengers (
                    passenger_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    email TEXT NOT NULL,
                    phone TEXT,
                    passport_number TEXT NOT NULL UNIQUE
                )
            """);

            // Create bookings table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS bookings (
                    booking_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    flight_id INTEGER NOT NULL,
                    passenger_id INTEGER NOT NULL,
                    booking_date TEXT NOT NULL,
                    seat_number TEXT NOT NULL,
                    status TEXT NOT NULL DEFAULT 'CONFIRMED',
                    FOREIGN KEY (flight_id) REFERENCES flights(flight_id),
                    FOREIGN KEY (passenger_id) REFERENCES passengers(passenger_id)
                )
            """);

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
