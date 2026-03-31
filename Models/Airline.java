package models;

public class Airline {
    private String name;
    private String airlineID;
    private String country;

    public Airline(String name, String airlineID, String country) {
        this.name = name;
        this.airlineID = airlineID;
        this.country = country;
    }


  //Airline data is not expected to change, that's why we don't need setters

    public String getName() {
        return name;
    }


    public String getAirlineID() {
        return airlineID;
    }

  

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "Airline{" +
                "name='" + name + '\'' +
                ", airlineID='" + airlineID + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
