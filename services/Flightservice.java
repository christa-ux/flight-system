package services;

import utils.Database;
import models.*;

import java.text.*;
import java.util.*;

public class Flightservice {

    public Flight createFlight(
        int airlineID,
        int sourceAirportID,
        int destinationAirportID,
        String departureTimeStr,
        String arrivalTimeStr,
        int capacity,
        double price
    ) {
        int flightID = Database.flightCount++;

        Airline airline = findAirlineById(airlineID);
        Airport origin = findAirportById(sourceAirportID);
        Airport destination = findAirportById(destinationAirportID);

        if (airline == null || origin == null || destination == null) {
            System.out.println("❌ Invalid airline or airport ID. Flight not created.");
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        try {
            Date departureTime = sdf.parse(departureTimeStr);
            Date arrivalTime = sdf.parse(arrivalTimeStr);

            Flight flight = new Flight(flightID, airline, origin, destination, departureTime, arrivalTime, capacity, price);
            Database.flights.add(flight);
            System.out.println("✅ Flight created successfully:\n" + flight);
            return flight;

        } catch (ParseException e) {
            System.out.println("❌ Error parsing dates. Please use format: yyyy/MM/dd HH:mm");
            return null;
        }
    }

    public void listFlights() {
        if (Database.flights.isEmpty()) {
            System.out.println("No flights found.");
            return;
        }
        for (Flight flight : Database.flights) {
            System.out.println(flight);
        }
    }

    public void modifyFlight(int flightID, String newDepartureTimeStr, String newArrivalTimeStr) {
        Flight flight = findFlightById(flightID);
        if (flight != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                Date newDepartureTime = sdf.parse(newDepartureTimeStr);
                Date newArrivalTime = sdf.parse(newArrivalTimeStr);

                flight.setDepartureTime(newDepartureTime);
                flight.setArrivalTime(newArrivalTime);

                System.out.println("✅ Flight updated:\n" + flight);
            } catch (ParseException e) {
                System.out.println("❌ Invalid date format. Use: yyyy/MM/dd HH:mm");
            }
        } else {
            System.out.println("❌ Flight not found.");
        }
    }

    public Flight findFlightById(int flightID) {
        for (Flight flight : Database.flights) {
            if (flight.getId() == flightID) {
                return flight;
            }
        }
        return null;
    }

    private Airline findAirlineById(int id) {
        for (Airline airline : Database.airlines) {
            try {
                if (Integer.parseInt(airline.getAirlineID()) == id) {
                    return airline;
                }
            } catch (NumberFormatException e) {
                continue;
            }
        }
        return null;
    }

    private Airport findAirportById(int id) {
        for (Airport airport : Database.airports) {
            if (airport.getAirportID() == id) {
                return airport;
            }
        }
        return null;
    }
}
