package utils;

import java.util.*;
import models.*;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Database {

    public static List<User> users = new ArrayList<>();
    public static List<Flight> flights = new ArrayList<>();
    public static List<Booking> bookings = new ArrayList<>();
    public static List<Airline> airlines = new ArrayList<>();
    public static List<Airport> airports = new ArrayList<>();
    public static final Map<String, String> passportPatterns = new LinkedHashMap<>();

    public static int userCount = 0;
    public static int flightCount = 0;
    public static int bookingCount = 0;
    public static int airlineCount = 0;
    public static int airportCount = 0;

    private static final String USER_DATA_FILE = "users.json";

    static {
        airlines.add(new Airline("Delta Airlines", String.valueOf(airlineCount++), "USA"));
        airlines.add(new Airline("Lufthansa", String.valueOf(airlineCount++), "Germany"));
        airlines.add(new Airline("Qatar Airways", String.valueOf(airlineCount++), "Qatar"));
        airlines.add(new Airline("Emirates", String.valueOf(airlineCount++), "UAE"));

        airports.add(new Airport("Hamad International Airport", airportCount++, "DOH", "Doha"));
        airports.add(new Airport("John F. Kennedy International Airport", airportCount++, "JFK", "New York"));
        airports.add(new Airport("Dubai International Airport", airportCount++, "DXB", "Dubai"));
        airports.add(new Airport("Frankfurt Airport", airportCount++, "FRA", "Frankfurt"));

        passportPatterns.put("usa", "^[0-9]{9}$");
        passportPatterns.put("uk", "^[A-Za-z0-9]{9}$");
        passportPatterns.put("india", "^[A-Z][0-9]{7}$");
        passportPatterns.put("canada", "^[A-Z]{2}[0-9]{6}$");
        passportPatterns.put("germany", "^[CFGHJKLMNPRTVWXYZ0-9]{9}$");
        passportPatterns.put("france", "^[0-9]{2}[A-Z]{2}[0-9]{5}$");
        passportPatterns.put("australia", "^[N][0-9]{7}$");
        passportPatterns.put("japan", "^[A-Z]{2}[0-9]{7}$");
        passportPatterns.put("china", "^[EG][0-9]{8}$");
        passportPatterns.put("brazil", "^[A-Z]{2}[0-9]{6}$");
        passportPatterns.put("southafrica", "^[0-9]{13}$");
        passportPatterns.put("nigeria", "^[A-Z]{1}[0-9]{8}$");
        passportPatterns.put("russia", "^[0-9]{2} [0-9]{6}$");
        passportPatterns.put("italy", "^[A-Z]{2}[0-9]{7}$");
        passportPatterns.put("mexico", "^[A-Z]{1}[0-9]{8}$");
    }

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(User.class, new UserAdapter()) // <-- register polymorphic adapter
            .setPrettyPrinting()
            .create();

    public static void saveUsersToFile() {
        try (Writer writer = new FileWriter(USER_DATA_FILE)) {
            gson.toJson(users, writer);
            System.out.println("✅ Users saved to users.json");
        } catch (IOException e) {
            System.out.println("❌ Error saving users: " + e.getMessage());
        }
    }

    public static void loadUsersFromFile() {
        File file = new File(USER_DATA_FILE);
        if (!file.exists()) return;

        try (Reader reader = new FileReader(file)) {
            users = gson.fromJson(reader, new TypeToken<List<User>>() {}.getType());
            userCount = users.size();
            System.out.println("✅ Loaded " + userCount + " users from file.");
        } catch (IOException e) {
            System.out.println("❌ Error loading users: " + e.getMessage());
        }
    }
}
