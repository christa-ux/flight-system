package services;

import models.Airport;
import utils.Database;


public class Airportservice {

    public Airport createAirport(String name, String airportCode, String city) {
        int airportID = Database.airportCount++;
        Airport airport = new Airport(name, airportID, airportCode, city);
        Database.airports.add(airport);
        return airport;
    }
      

    public Airport getAirportById(int airportID) {
        return Database.airports.stream()
                .filter(airport -> airport.getAirportID() == airportID)
                .findFirst()
                .orElse(null);
    }       


    public void listAirports() {
        for (Airport airport : Database.airports) {
            System.out.println(airport);
        }
    }

  
}