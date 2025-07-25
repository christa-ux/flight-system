package services;

import utils.Database;

import models.Airline;

public class Airlineservice {
    public Airline createAirline(String name, String airlineID, String country) {
        Airline airline = new Airline(name, airlineID, country);
        Database.airlines.add(airline);
        return airline;
    }

    public Airline getAirlineById(String airlineID) {
        return Database.airlines.stream()
                .filter(airline -> airline.getAirlineID().equals(airlineID))
                .findFirst()
                .orElse(null);
    }
}
