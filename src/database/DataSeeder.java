package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSeeder {

    public static void seedDatabase() {
        clearDatabase();
        seedFlights();
        seedPassengers();
        seedBookings();
        System.out.println("Database seeded with sample data successfully!");
    }

    private static void clearDatabase() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM bookings");
            stmt.execute("DELETE FROM passengers");
            stmt.execute("DELETE FROM flights");
            stmt.execute("DELETE FROM sqlite_sequence"); // Reset auto-increment
            System.out.println("Cleared existing data.");
        } catch (SQLException e) {
            System.err.println("Error clearing database: " + e.getMessage());
        }
    }

    private static void seedFlights() {
        String sql = """
            INSERT INTO flights (flight_number, airline, origin, destination,
                                departure_time, arrival_time, total_seats, available_seats, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        Object[][] flights = {
            // Domestic US Flights
            {"AA1234", "American Airlines", "New York (JFK)", "Los Angeles (LAX)", "2025-03-15 08:00", "2025-03-15 11:30", 180, 45, "SCHEDULED"},
            {"DL567", "Delta Air Lines", "Atlanta (ATL)", "Chicago (ORD)", "2025-03-15 09:15", "2025-03-15 11:00", 160, 82, "SCHEDULED"},
            {"UA890", "United Airlines", "San Francisco (SFO)", "Denver (DEN)", "2025-03-15 07:45", "2025-03-15 11:15", 150, 67, "SCHEDULED"},
            {"SW234", "Southwest Airlines", "Dallas (DFW)", "Phoenix (PHX)", "2025-03-15 10:30", "2025-03-15 12:00", 143, 98, "SCHEDULED"},
            {"B6789", "JetBlue Airways", "Boston (BOS)", "Miami (MIA)", "2025-03-15 06:00", "2025-03-15 09:30", 162, 34, "SCHEDULED"},
            {"AA456", "American Airlines", "Miami (MIA)", "New York (JFK)", "2025-03-15 14:00", "2025-03-15 17:30", 180, 120, "SCHEDULED"},
            {"DL123", "Delta Air Lines", "Seattle (SEA)", "Minneapolis (MSP)", "2025-03-15 11:00", "2025-03-15 16:30", 160, 55, "SCHEDULED"},
            {"UA456", "United Airlines", "Houston (IAH)", "Washington (IAD)", "2025-03-15 13:45", "2025-03-15 18:00", 150, 72, "SCHEDULED"},

            // International Flights
            {"BA178", "British Airways", "New York (JFK)", "London (LHR)", "2025-03-15 19:00", "2025-03-16 07:00", 280, 156, "SCHEDULED"},
            {"LH401", "Lufthansa", "Chicago (ORD)", "Frankfurt (FRA)", "2025-03-15 17:30", "2025-03-16 08:45", 300, 189, "SCHEDULED"},
            {"AF007", "Air France", "Los Angeles (LAX)", "Paris (CDG)", "2025-03-15 16:00", "2025-03-16 11:30", 290, 142, "SCHEDULED"},
            {"EK201", "Emirates", "New York (JFK)", "Dubai (DXB)", "2025-03-15 22:00", "2025-03-16 19:30", 350, 220, "SCHEDULED"},
            {"JL005", "Japan Airlines", "San Francisco (SFO)", "Tokyo (NRT)", "2025-03-15 12:00", "2025-03-16 15:30", 250, 88, "SCHEDULED"},
            {"QF12", "Qantas", "Los Angeles (LAX)", "Sydney (SYD)", "2025-03-15 21:30", "2025-03-17 07:45", 300, 175, "SCHEDULED"},
            {"AC855", "Air Canada", "Toronto (YYZ)", "Vancouver (YVR)", "2025-03-15 08:30", "2025-03-15 10:45", 140, 62, "SCHEDULED"},

            // Flights with different statuses
            {"AA100", "American Airlines", "Los Angeles (LAX)", "New York (JFK)", "2025-03-14 06:00", "2025-03-14 14:30", 180, 0, "DEPARTED"},
            {"DL200", "Delta Air Lines", "New York (JFK)", "Atlanta (ATL)", "2025-03-14 07:00", "2025-03-14 09:30", 160, 0, "ARRIVED"},
            {"UA300", "United Airlines", "Denver (DEN)", "Chicago (ORD)", "2025-03-14 15:00", "2025-03-14 18:30", 150, 150, "CANCELLED"},

            // More upcoming flights
            {"SW567", "Southwest Airlines", "Las Vegas (LAS)", "Seattle (SEA)", "2025-03-16 09:00", "2025-03-16 11:30", 143, 143, "SCHEDULED"},
            {"B6234", "JetBlue Airways", "Fort Lauderdale (FLL)", "New York (JFK)", "2025-03-16 10:15", "2025-03-16 13:30", 162, 162, "SCHEDULED"},
            {"AA789", "American Airlines", "Chicago (ORD)", "Dallas (DFW)", "2025-03-16 14:00", "2025-03-16 16:30", 180, 180, "SCHEDULED"},
            {"DL890", "Delta Air Lines", "Detroit (DTW)", "Orlando (MCO)", "2025-03-16 08:45", "2025-03-16 12:00", 160, 160, "SCHEDULED"},
            {"NK101", "Spirit Airlines", "Fort Lauderdale (FLL)", "Chicago (ORD)", "2025-03-16 06:30", "2025-03-16 09:15", 180, 145, "SCHEDULED"},
            {"AS505", "Alaska Airlines", "Seattle (SEA)", "Los Angeles (LAX)", "2025-03-16 11:00", "2025-03-16 13:30", 150, 98, "SCHEDULED"},
            {"WN333", "Southwest Airlines", "Denver (DEN)", "Las Vegas (LAS)", "2025-03-16 15:30", "2025-03-16 16:45", 143, 112, "SCHEDULED"}
        };

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Object[] flight : flights) {
                pstmt.setString(1, (String) flight[0]);
                pstmt.setString(2, (String) flight[1]);
                pstmt.setString(3, (String) flight[2]);
                pstmt.setString(4, (String) flight[3]);
                pstmt.setString(5, (String) flight[4]);
                pstmt.setString(6, (String) flight[5]);
                pstmt.setInt(7, (Integer) flight[6]);
                pstmt.setInt(8, (Integer) flight[7]);
                pstmt.setString(9, (String) flight[8]);
                pstmt.executeUpdate();
            }
            System.out.println("Inserted " + flights.length + " flights.");
        } catch (SQLException e) {
            System.err.println("Error seeding flights: " + e.getMessage());
        }
    }

    private static void seedPassengers() {
        String sql = """
            INSERT INTO passengers (first_name, last_name, email, phone, passport_number)
            VALUES (?, ?, ?, ?, ?)
        """;

        Object[][] passengers = {
            {"James", "Wilson", "james.wilson@email.com", "+1-555-0101", "US12345678"},
            {"Emily", "Chen", "emily.chen@gmail.com", "+1-555-0102", "US23456789"},
            {"Michael", "Johnson", "m.johnson@outlook.com", "+1-555-0103", "US34567890"},
            {"Sarah", "Williams", "sarah.w@yahoo.com", "+1-555-0104", "US45678901"},
            {"David", "Brown", "david.brown@email.com", "+1-555-0105", "US56789012"},
            {"Jennifer", "Garcia", "jen.garcia@gmail.com", "+1-555-0106", "US67890123"},
            {"Robert", "Martinez", "r.martinez@email.com", "+1-555-0107", "US78901234"},
            {"Lisa", "Anderson", "lisa.anderson@outlook.com", "+1-555-0108", "US89012345"},
            {"William", "Taylor", "w.taylor@email.com", "+1-555-0109", "US90123456"},
            {"Jessica", "Thomas", "jessica.t@gmail.com", "+1-555-0110", "US01234567"},
            {"Christopher", "Moore", "c.moore@email.com", "+1-555-0111", "CA11223344"},
            {"Amanda", "Jackson", "amanda.j@yahoo.com", "+1-555-0112", "CA22334455"},
            {"Daniel", "White", "daniel.white@email.com", "+1-555-0113", "UK33445566"},
            {"Michelle", "Harris", "m.harris@gmail.com", "+1-555-0114", "UK44556677"},
            {"Matthew", "Clark", "matt.clark@outlook.com", "+1-555-0115", "AU55667788"},
            {"Ashley", "Lewis", "ashley.lewis@email.com", "+1-555-0116", "AU66778899"},
            {"Andrew", "Robinson", "a.robinson@email.com", "+1-555-0117", "DE77889900"},
            {"Stephanie", "Walker", "steph.walker@gmail.com", "+1-555-0118", "FR88990011"},
            {"Joshua", "Hall", "josh.hall@email.com", "+1-555-0119", "JP99001122"},
            {"Nicole", "Allen", "nicole.allen@yahoo.com", "+1-555-0120", "BR00112233"},
            {"Kevin", "Young", "kevin.young@email.com", "+1-555-0121", "US11122233"},
            {"Elizabeth", "King", "liz.king@gmail.com", "+1-555-0122", "US22233344"},
            {"Ryan", "Wright", "ryan.wright@outlook.com", "+1-555-0123", "US33344455"},
            {"Megan", "Scott", "megan.scott@email.com", "+1-555-0124", "US44455566"},
            {"Brandon", "Green", "b.green@email.com", "+1-555-0125", "US55566677"},
            {"Rachel", "Adams", "rachel.adams@gmail.com", "+1-555-0126", "US66677788"},
            {"Justin", "Baker", "justin.baker@email.com", "+1-555-0127", "US77788899"},
            {"Lauren", "Nelson", "lauren.nelson@yahoo.com", "+1-555-0128", "US88899900"},
            {"Tyler", "Carter", "tyler.carter@outlook.com", "+1-555-0129", "US99900011"},
            {"Samantha", "Mitchell", "sam.mitchell@email.com", "+1-555-0130", "US00011122"},
            {"Alexander", "Perez", "alex.perez@gmail.com", "+1-555-0131", "MX11133344"},
            {"Victoria", "Roberts", "v.roberts@email.com", "+1-555-0132", "MX22244455"},
            {"Benjamin", "Turner", "ben.turner@outlook.com", "+1-555-0133", "IE33355566"},
            {"Olivia", "Phillips", "olivia.p@email.com", "+1-555-0134", "IE44466677"},
            {"Ethan", "Campbell", "ethan.campbell@gmail.com", "+1-555-0135", "NZ55577788"}
        };

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Object[] passenger : passengers) {
                pstmt.setString(1, (String) passenger[0]);
                pstmt.setString(2, (String) passenger[1]);
                pstmt.setString(3, (String) passenger[2]);
                pstmt.setString(4, (String) passenger[3]);
                pstmt.setString(5, (String) passenger[4]);
                pstmt.executeUpdate();
            }
            System.out.println("Inserted " + passengers.length + " passengers.");
        } catch (SQLException e) {
            System.err.println("Error seeding passengers: " + e.getMessage());
        }
    }

    private static void seedBookings() {
        String sql = """
            INSERT INTO bookings (flight_id, passenger_id, booking_date, seat_number, status)
            VALUES (?, ?, ?, ?, ?)
        """;

        // Bookings: flight_id, passenger_id, booking_date, seat_number, status
        Object[][] bookings = {
            // AA1234 - New York to Los Angeles (Flight 1)
            {1, 1, "2025-03-01", "12A", "CONFIRMED"},
            {1, 2, "2025-03-02", "12B", "CONFIRMED"},
            {1, 3, "2025-03-03", "14C", "CONFIRMED"},
            {1, 4, "2025-03-05", "18F", "CONFIRMED"},
            {1, 5, "2025-03-07", "22A", "CONFIRMED"},

            // DL567 - Atlanta to Chicago (Flight 2)
            {2, 6, "2025-03-02", "8A", "CONFIRMED"},
            {2, 7, "2025-03-04", "8B", "CONFIRMED"},
            {2, 8, "2025-03-06", "15D", "CONFIRMED"},

            // UA890 - San Francisco to Denver (Flight 3)
            {3, 9, "2025-03-01", "5A", "CONFIRMED"},
            {3, 10, "2025-03-03", "5B", "CONFIRMED"},
            {3, 11, "2025-03-05", "10C", "CONFIRMED"},
            {3, 12, "2025-03-08", "20F", "CANCELLED"},

            // B6789 - Boston to Miami (Flight 5)
            {5, 13, "2025-02-28", "3A", "CONFIRMED"},
            {5, 14, "2025-03-01", "3B", "CONFIRMED"},
            {5, 15, "2025-03-02", "7C", "CONFIRMED"},
            {5, 16, "2025-03-04", "7D", "CONFIRMED"},
            {5, 17, "2025-03-06", "12E", "CONFIRMED"},
            {5, 18, "2025-03-08", "12F", "CONFIRMED"},

            // BA178 - New York to London (Flight 9)
            {9, 19, "2025-03-01", "2A", "CONFIRMED"},
            {9, 20, "2025-03-02", "2B", "CONFIRMED"},
            {9, 21, "2025-03-03", "5K", "CONFIRMED"},
            {9, 22, "2025-03-05", "14A", "CONFIRMED"},
            {9, 1, "2025-03-07", "14B", "CONFIRMED"},

            // LH401 - Chicago to Frankfurt (Flight 10)
            {10, 23, "2025-03-02", "1A", "CONFIRMED"},
            {10, 24, "2025-03-04", "1B", "CONFIRMED"},
            {10, 25, "2025-03-06", "8C", "CONFIRMED"},

            // EK201 - New York to Dubai (Flight 12)
            {12, 26, "2025-03-01", "4A", "CONFIRMED"},
            {12, 27, "2025-03-03", "4B", "CONFIRMED"},
            {12, 28, "2025-03-05", "10K", "CONFIRMED"},
            {12, 29, "2025-03-07", "15A", "CONFIRMED"},

            // JL005 - San Francisco to Tokyo (Flight 13)
            {13, 30, "2025-02-25", "6A", "CONFIRMED"},
            {13, 31, "2025-02-28", "6B", "CONFIRMED"},
            {13, 32, "2025-03-02", "12C", "CONFIRMED"},
            {13, 33, "2025-03-05", "18D", "CONFIRMED"},
            {13, 34, "2025-03-08", "24E", "CONFIRMED"},

            // AA100 - Los Angeles to New York - DEPARTED (Flight 16)
            {16, 2, "2025-03-10", "11A", "COMPLETED"},
            {16, 4, "2025-03-10", "11B", "COMPLETED"},
            {16, 6, "2025-03-11", "15C", "COMPLETED"},

            // DL200 - New York to Atlanta - ARRIVED (Flight 17)
            {17, 8, "2025-03-10", "9A", "COMPLETED"},
            {17, 10, "2025-03-11", "9B", "COMPLETED"},

            // Upcoming flights with new bookings
            {19, 35, "2025-03-10", "4A", "CONFIRMED"},
            {20, 1, "2025-03-11", "8B", "CONFIRMED"},
            {21, 3, "2025-03-12", "12C", "CONFIRMED"},
            {22, 5, "2025-03-12", "16D", "CONFIRMED"},
            {23, 7, "2025-03-13", "20E", "CONFIRMED"},
            {24, 9, "2025-03-13", "24F", "CONFIRMED"},
            {25, 11, "2025-03-14", "2A", "CONFIRMED"}
        };

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Object[] booking : bookings) {
                pstmt.setInt(1, (Integer) booking[0]);
                pstmt.setInt(2, (Integer) booking[1]);
                pstmt.setString(3, (String) booking[2]);
                pstmt.setString(4, (String) booking[3]);
                pstmt.setString(5, (String) booking[4]);
                pstmt.executeUpdate();
            }
            System.out.println("Inserted " + bookings.length + " bookings.");
        } catch (SQLException e) {
            System.err.println("Error seeding bookings: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();
        seedDatabase();
    }
}
