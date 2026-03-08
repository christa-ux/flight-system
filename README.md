# Flight Management System

A comprehensive flight booking system built with Java Swing, utilizing Object-Oriented Programming principles and SQLite for data persistence. The system supports flight management, passenger records, and booking functionality.

## Features

### Flight Operations
- Create and manage flight schedules
- Update flight information (departure times, destinations, status)
- List and search available flights

### Passenger Management
- Add and manage passenger records
- Store passenger details and contact information

### Booking System
- Create new flight reservations
- Link passengers to flights
- View and manage bookings

### Data Persistence
- SQLite database for reliable data storage
- Automatic data seeding for demo purposes
- Data integrity maintained across sessions

## Technologies Used

- **Java 8+** - Core programming language
- **Java Swing** - GUI framework
- **SQLite** - Embedded database
- **JDBC** - Database connectivity

## Project Structure

```
flight-system/
├── src/
│   ├── Main.java              # Application entry point
│   ├── database/
│   │   ├── DatabaseConnection.java
│   │   └── DataSeeder.java
│   ├── model/
│   │   ├── Flight.java
│   │   ├── Passenger.java
│   │   └── Booking.java
│   ├── dao/
│   │   ├── FlightDAO.java
│   │   ├── PassengerDAO.java
│   │   └── BookingDAO.java
│   └── gui/
│       ├── MainFrame.java
│       ├── FlightPanel.java
│       ├── PassengerPanel.java
│       └── BookingPanel.java
├── lib/
│   └── sqlite-jdbc-3.42.0.0.jar
├── out/                       # Compiled classes
└── flight_system.db          # SQLite database
```

## Prerequisites

- Java JDK 8 or higher

## Getting Started

### Option 1: Run Pre-compiled (Easiest)

If the project is already compiled (check for `out/` folder):

```bash
cd flight-system
java -cp "out:lib/sqlite-jdbc-3.42.0.0.jar" Main
```

**On Windows:**
```bash
java -cp "out;lib/sqlite-jdbc-3.42.0.0.jar" Main
```

### Option 2: Compile and Run

1. Clone the repository
   ```bash
   git clone <repository-url>
   cd flight-system
   ```

2. Compile the source files
   ```bash
   mkdir -p out
   javac -cp "lib/sqlite-jdbc-3.42.0.0.jar" -d out src/*.java src/**/*.java
   ```

3. Run the application
   ```bash
   java -cp "out:lib/sqlite-jdbc-3.42.0.0.jar" Main
   ```

### Option 3: Using an IDE

1. Open the project in IntelliJ IDEA, Eclipse, or VS Code
2. Add `lib/sqlite-jdbc-3.42.0.0.jar` to your project libraries
3. Run `Main.java`

## Database

The application uses SQLite with automatic initialization. On first run, it creates `flight_system.db` with the required tables and sample data.

To reset the database, simply delete `flight_system.db` and restart the application.
