package models;

public class Airport {
    private String name;
    private int airportID;
    private String airportCode;
    private String city;

    public Airport(String name, int airportID, String airportCode, String city) {
        this.name = name;
        this.airportID = airportID;
        this.airportCode = airportCode;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public int getAirportID() {
        return airportID;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "name='" + name + '\'' +
                ", airportID=" + airportID +
                ", airportCode='" + airportCode + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
