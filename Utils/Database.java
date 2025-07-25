package utils;

import java.util.*;
import models.*;

public class Database {
    public static List<User> users = new ArrayList<>();
    public static List<Flight> flights = new ArrayList<>();
    public static List<Booking> bookings = new ArrayList<>();
    public static List<Airline> airlines = new ArrayList<>();
    public static List<Airport> airports = new ArrayList<>();

    public static int userCount = 0;
    public static int flightCount = 0;
    public static int bookingCount = 0;
    public static int airlineCount = 0;
    public static int airportCount = 0;

    // Static block to preload airlines and airports
    static {
        // Preload Airlines
        airlines.add(new Airline("Qatar Airways", String.valueOf(airlineCount++), "Qatar"));
        airlines.add(new Airline("Delta Airlines", String.valueOf(airlineCount++), "USA"));
        airlines.add(new Airline("Emirates", String.valueOf(airlineCount++), "UAE"));
        airlines.add(new Airline("Lufthansa", String.valueOf(airlineCount++), "Germany"));

        // Preload Airports
        airports.add(new Airport("Hamad International Airport", airportCount++, "DOH", "Doha"));
        airports.add(new Airport("John F. Kennedy International Airport", airportCount++, "JFK", "New York"));
        airports.add(new Airport("Dubai International Airport", airportCount++, "DXB", "Dubai"));
        airports.add(new Airport("Frankfurt Airport", airportCount++, "FRA", "Frankfurt"));
    }
}
