 

/**
 * Represents one listing of a property for rental on Airbnb.
 * This is essentially one row in the data table. Each column
 * has a corresponding field.
 */ 

public class Station {
    /**
     * The name of the individual station
     */
    private String name;
    /**
     * The type of the station
     */
    private String network;

    /**
     * The zone where the station is located
     */
    private int zone;

    /**
     * The location on a map where the station is situated.
     */
    private double latitude;
    private double longitude;

    public Station(String name, String network, int zone, double longitude, 
    double latitude) 
    {
        this.name = name;
        this.network = network;
        this.zone = zone;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public String getNetwork() {
        return network;
    }

    public int getZone() {
        return zone;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "AirbnbListing{" +
                ", name='" + name + '\'' +
                ", network='" + network + '\'' +
                ", zone='" + zone + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}

