# Flight Management System

A comprehensive flight booking system built with Java and Maven, utilizing Object-Oriented Programming principles and basic data structures. The system supports user management, flight operations, and booking functionality with JSON-based data persistence.

## Features

### User Management
- Multi-role Support: Create and manage three types of users:
 - Customer: Can search flights, make bookings, and manage their reservations
 - Admin: Full system access with administrative privileges
 - Staff: Operational access to manage flights and assist customers

### Flight Operations
- Create and manage flight schedules
- Update flight information (departure times, gates, status)
- List and search available flights
- Real-time flight status tracking

### Booking System
- Create new flight reservations
- Modify existing bookings
- Cancel reservations
- View booking history and details

### Data Validation
- Input Validation: Robust validation system for:
 - Passport numbers (format and authenticity checks)
 - Phone numbers (international format support)
 - Email addresses
 - Flight dates and times
 - User credentials

### Data Persistence
- JSON Storage: All data is persistently stored in JSON format
- Automatic data serialization/deserialization
- Data integrity maintenance across sessions

## Technologies Used

- Java: Core programming language
- Maven: Project management and build automation
- JSON: Data storage and serialization
- Object-Oriented Programming: Design paradigm
- Data Structures: Lists, Maps, and custom collections

## Prerequisites

- Java JDK 8 or higher
- Apache Maven 3.6 or higher
- IDE (IntelliJ IDEA, Eclipse, or VS Code recommended)


