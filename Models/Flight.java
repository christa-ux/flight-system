package Models;
import utils.*;
import services.*;

import java.util.*; 

public class Flight {
    private int flightID;
    private Airline flightAirline;
    private Airport origin;
    private Airport destination;
    private Date departureTime;
    private Date arrivalTime;
    private int capacity;
    private double price;


    public Flight(int flightID, Airline flightAirline, Airport origin, Airport destination, Date departureTime, Date arrivalTime, int capacity, double price) {
        this.flightID = flightID;
        this.flightAirline = flightAirline;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.capacity = capacity;
        this.price = price;
    }

    public int getId() {
        return flightID;
    }

    public String getFlightAirline() {
        return flightAirline;
    }

    public Airport getOrigin() {
        return origin;
    }

    public Airport getDestination() {
        return destination;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getPrice() {
        return price;
    }





    @Override
    public String toString() {
        return "Flight{" +
                "id=" + flightID +
                ", flightAirline='" + flightAirline + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", capacity=" + capacity +
                ", price=" + price +
                '}';
    }
}

