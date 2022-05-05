import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;   
import java.util.Date; 
import java.util.concurrent.TimeUnit;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Iterator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.text.*;
import javafx.scene.chart.XYChart;

/**
 * The calculations to derive the statistics for the properties
 * in a specific price range.
 *
 * @author Chin Wan k21016106
 * @version 30/03/2022
 */

public class DataStatisticCalculator
{
    // All the data of the properties.
    private ArrayList<AirbnbListing> data;
    // All the data of the tube/train station.
    private ArrayList<Station> stationData;
    
    private int totalNumberOfListings;
    private int numberOfEntireHomeApt;
    private int averageReview;
    
    private String mostExpensiveBorough;
    
    private String averageTimeSinceLastReview;
    private String averageAvailabilityTime;
    
    private HashMap<String, BoroughData> allBoroughData;
    
    private String mostExpensivePropertyBorough;
    private String averageDistanceToTube;
    
    private boolean statisticsLoaded;

    public DataStatisticCalculator( ArrayList<AirbnbListing> data, ArrayList<Station> stationData ){
        this.data = data;
        this.stationData = stationData;
        
        this.allBoroughData = new HashMap<>();
        this.setupBoroughData();
        
        this.statisticsLoaded = false;
        
        this.totalNumberOfListings = data.size();
        this.numberOfEntireHomeApt = 0;
        this.averageAvailabilityTime = "0 days";
        
        this.averageReview = 0;
                
        this.mostExpensivePropertyBorough = "";
        this.mostExpensiveBorough = "";
        this.averageTimeSinceLastReview = "0 years 0 days";
        this.averageDistanceToTube = "0.000m";
    }
    
    public void setupBoroughData(){
        String[] allBoroughs =  new String[] {"Sutton","Hounslow","Camden","Enfield","Redbridge","Havering","Southwark","Harrow","Waltham Forest","Croydon",
                            "Ealing","Merton","Greenwich","Greenwich","Haringey","Brent","Kingston upon Thames","Bromley","Barnet","Barking and Dagenham",
                            "Hammersmith and Fulham","Bexley","Westminster","Tower Hamlets","Hackney","Kensington and Chelsea","Lewisham","Wandsworth","Hillingdon",
                            "Islington","Newham","Lambeth","City of London"};
        
                            
                            
        for (String boroughName : allBoroughs){
            this.allBoroughData.put(boroughName, new BoroughData());
        }
    }
    
    /**
     * Get the list of data of the properties.
     * @return Data of the properties
     */
    public ArrayList<AirbnbListing> getData() {
        return data;
    }
    
    /**
     * Get the list of data of the tube/train station.
     * @return Data of the tube/train station.
     */
    public ArrayList<Station> getStationData() {
        return stationData;
    }
    
    /**
     * Get the maximum price out of all the properties.
     * @return The maximum price out of all properties
     */
    public int getMaxPrice(){
        //or could just be 0 as no price can be negative
        int maxPrice = -2147483647;
        
        for (AirbnbListing listing : data){
            if (listing != null) {
                if (listing.getPrice() > maxPrice){
                    maxPrice = listing.getPrice();
                    mostExpensivePropertyBorough = listing.getNeighbourhood();
                }
            }
        }
        
        return maxPrice;
    }
    
    /**
     * Get the range of the prices in between 0 and the maximum price.
     * @return An arrayList of prices with interval of 50
     */
    public ArrayList<String> getPriceRange(){
        int sectionWidth = 50;
        ArrayList<String> priceRange = new ArrayList<>();
        
        int maxPrice = this.getMaxPrice();
        
        int deltaPrice = -sectionWidth;
        
        while (deltaPrice < maxPrice - sectionWidth){
            deltaPrice += sectionWidth;
            priceRange.add(deltaPrice + "£");
        }
        
        priceRange.add(maxPrice + "£");
        
        return priceRange;
    }
    
    /**
     * Get the property listings in between the price range
     * @param fromPrice The lowest price selected
     * @param toPrice The highest price selected
     */
    public ArrayList<AirbnbListing> getListingsInPriceRange(int fromPrice, int toPrice){
        if (data.size() == 0) return new ArrayList<AirbnbListing>();

        List<AirbnbListing> listingsInPriceRange = data.stream()
                                                     .filter(  listing -> 
                                                     (listing.getPrice() > fromPrice) && (listing.getPrice() < toPrice))
                                                     .collect(Collectors.toList());
                                                     
        return new ArrayList<AirbnbListing>(listingsInPriceRange);
    }
    
    public ArrayList<AirbnbListing> getListingsInBorough(String boroughName){
        
        if (data.size() == 0) return new ArrayList<AirbnbListing>();
        
        List<AirbnbListing> listingsInBorough = data.stream()
                                                     .filter(  listing -> 
                                                     (listing.getNeighbourhood().equals( boroughName )))
                                                     .collect(Collectors.toList());
                                                     
        return new ArrayList<AirbnbListing>(listingsInBorough);
    }
    
    public int getAverageReview(){
        return this.averageReview;
    }
    
    public int getTotalNumberOfEntireHomeApt(){
        return this.numberOfEntireHomeApt;
    }
    
    public String getMostExpensivePropertyBorough(){
        return this.mostExpensivePropertyBorough;
    }
    
    public int getTotalNumberOfListings(){
        return this.totalNumberOfListings;
    }
    
    public String getAverageAvailabilityTime(){
        return this.averageAvailabilityTime;
    }
    
    public String getMostExpensiveBorough(){
        
        return this.mostExpensiveBorough;
    }
    
    public String getAverageTimeLastReview(){
        return this.averageTimeSinceLastReview;
    }
    
    public String getAverageDistanceToTube(){
        return this.averageDistanceToTube;
    }
    
    public ArrayList<XYChart.Data> getAveragePricePerBoroughData(){
        ArrayList<XYChart.Data> prices = new ArrayList<>();
        for ( String borough : allBoroughData.keySet() ){
            prices.add( new XYChart.Data( borough, allBoroughData.get(borough).getAveragePricePerBorough()) );
        }
        
        return prices;
    }
    
    public ArrayList<XYChart.Data> getPrivateRoomsPerBoroughData(){
        ArrayList<XYChart.Data> privateRooms = new ArrayList<>();
        for ( String borough : allBoroughData.keySet() ){
            privateRooms.add( new XYChart.Data( borough, allBoroughData.get(borough).getPrivateRoomsNumber()) );
        }
        
        return privateRooms;
    }
    
    public ArrayList<XYChart.Data> getEntireRoomsPerBoroughData(){
        ArrayList<XYChart.Data> entireRooms = new ArrayList<>();
        for ( String borough : allBoroughData.keySet() ){
            entireRooms.add( new XYChart.Data( borough, allBoroughData.get(borough).getEntireRoomsNumber()) );
        }
        
        return entireRooms;
    }
    
    public void loadStatistics(){
        if (this.statisticsLoaded) return;
                                    
        int totalAvailabilityTime = 0;
        int totalReviewNumber = 0;
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        int totalDaysDifference = 0;
        int countOfReviewsDays = 0;
        
        double distanceAddUp = 0;
        int maxPrice = -2147483647;
        
        for (AirbnbListing listing : data ){
            if (listing != null){
                String listingBorough = listing.getNeighbourhood();
                BoroughData currentBorough = this.allBoroughData.get( listingBorough );
                
                if (listing.getRoom_type().equals("Entire home/apt")){
                    this.numberOfEntireHomeApt++;
                    if (currentBorough != null)  currentBorough.incrementEntireRoomsNumber();

                }else if (listing.getRoom_type().equals("Private room")){
                    if (currentBorough != null)  currentBorough.incrementPrivateRoomsNumber();
                }
                
                totalAvailabilityTime += listing.getAvailability365();
                totalReviewNumber += listing.getNumberOfReviews();
                
                int price = listing.getPrice() / listing.getMinimumNights();
                
                if (price > maxPrice){
                    maxPrice = price;
                    this.mostExpensiveBorough = listingBorough;
                }
                
                if (currentBorough != null) currentBorough.addAveragePricePerBorough( listing.getPrice() );
                
                if (!listing.getLastReview().equals("")){
                    Date listingDate;
                
                    //if there's an error, we ignore the date
                    try{
                        listingDate = formatter.parse(listing.getLastReview());
                    }catch (java.text.ParseException e){
                        listingDate = currentDate;
                    }
                    
                    long timeDifference = currentDate.getTime() - listingDate.getTime();
                    long differenceDays = TimeUnit.MILLISECONDS.toDays(timeDifference) % 365;
                    long differenceYears = TimeUnit.MILLISECONDS.toDays(timeDifference) / 365;
                    
                    totalDaysDifference += (differenceDays + differenceYears*365);
                    countOfReviewsDays++;
                }
                
                
                double shortestDistance = Double.POSITIVE_INFINITY;
                for (Station station: stationData) {
                    double distance = distanceBetweenCoordinates(listing.getLatitude(), listing.getLongitude(), 
                                                                   station.getLatitude(), station.getLongitude());
                                                                   
                    if (distance < shortestDistance) {
                        shortestDistance = distance;
                    }
                }
                
                distanceAddUp += shortestDistance;
            }
        }
        
        for ( String borough : allBoroughData.keySet()){
            this.allBoroughData.get(borough).averageOutPricePerBorough();
        }
        
        if (this.totalNumberOfListings > 0) {
            this.averageAvailabilityTime = totalAvailabilityTime / totalNumberOfListings + " days";
            this.averageReview = totalReviewNumber / totalNumberOfListings;
            
            double distanceValue = distanceAddUp / totalNumberOfListings;
            DecimalFormat df = new DecimalFormat("#.###");
            
            this.averageDistanceToTube = df.format(distanceValue) + "m";
        }
        
        if ( countOfReviewsDays != 0 ){
            totalDaysDifference /= countOfReviewsDays;
            this.averageTimeSinceLastReview = totalDaysDifference / 365 + " years " + totalDaysDifference % 365 +" days ";
        }
        
        this.statisticsLoaded = true;
    }

    /**
     * Calculate the distance between two coordinates.
     * @return the distance between two coordinates.
     */
    public double distanceBetweenCoordinates(double p_lat, double p_lon, double s_lat, double s_lon) 
    {
        final int R = 6371000; // Radius of the earth
        
        double latDifference = Math.toRadians(p_lat - s_lat);
        double lonDifference = Math.toRadians(p_lon - s_lon);
        double a = Math.pow(Math.sin(latDifference/2),2)
                 + Math.cos(s_lat) * Math.cos(p_lat)
                 * Math.pow(Math.sin(lonDifference/2),2);
        
        double c = 2 * Math.asin(Math.sqrt(a));
        double finalDistance = R * c; 
        
        return finalDistance;
    }
    
    /**
     * Get the host with the most reviews of his/her properties
     * @return The host with the most review
     */
    public String mostReviewedHost()
    {
        Map<String, Integer> allHostReview = data.stream().collect(
                Collectors.groupingBy(AirbnbListing::getHost_name, Collectors.summingInt(AirbnbListing::getNumberOfReviews)));
        
        Map.Entry<String, Integer> mostReviewed = null;
        for (Map.Entry<String, Integer> entry : allHostReview.entrySet())
        {
            if (mostReviewed == null || entry.getValue().compareTo(mostReviewed.getValue()) > 0)
            {
                mostReviewed = entry;
            }
        }
        if (mostReviewed != null) {
            return mostReviewed.getKey();
        }
        return "Not found";
    }
    
    /**
     * Create a map containing the top 20 most reviewed hosts
     * @return A map with hosts'names as keys, number of reviews as values
     */
    public Map<String, Integer> listOfMostReviewedHosts() 
    {
        Map<String, Integer> allHostReview = data.stream().collect(
                Collectors.groupingBy(AirbnbListing::getHost_name, Collectors.summingInt(AirbnbListing::getNumberOfReviews)));
        
        Queue<Integer> allReviews = new PriorityQueue<Integer>(
            new Comparator<Integer>() {
                public int compare(Integer first, Integer second) {
                    if (first < second) {
                        return +1;
                     }
                    else if (first.equals(second)){ 
                        return 0;
                    }
                    else {   
                        return -1;
                    }
                }
            }
        );
            
        for (Map.Entry<String, Integer> entry: allHostReview.entrySet()) {
            allReviews.add(entry.getValue());
        }
        
        int count = 0;
        Iterator iterator = allReviews.iterator();
        Map<String, Integer> maxHostReviews = new HashMap<>();
        while (iterator.hasNext() && count < 21) {
            Integer value = (Integer) iterator.next();
            for (Map.Entry<String, Integer> entry : allHostReview.entrySet()) {
                if (value == entry.getValue()) {
                     maxHostReviews.put(entry.getKey(), value);
                     count++;
                }
            }
        }
        return maxHostReviews;
    }  
    
    /**
     * To create a map containing the amount of reviews per year
     * @return A map with years keys, amount of reviews that year as values
     * @throws ParseException If the string cannot be parsed in date format
     */
    public Map<String, Integer> averageReviewEachYear() throws java.text.ParseException 
    {
        List<Integer> allReviewYears = new ArrayList<>();
        for (AirbnbListing listing : data){
            if (listing != null) {
                if (!listing.getLastReview().equals("")){
                    int year = new SimpleDateFormat("dd/MM/yyyy").parse(listing.getLastReview()).getYear()+1900;
                    if (!allReviewYears.contains(year)) {
                        allReviewYears.add(year);
                    }
                }
            }
        }
        Map<String, Integer> reviewPerYear = new HashMap<>();
        for (Integer year : allReviewYears) {
            int count = 0;
            for (AirbnbListing listing : data) {
                if (listing != null) {
                    if (!listing.getLastReview().equals("")){
                        if (year == new SimpleDateFormat("dd/MM/yyyy").parse(listing.getLastReview()).getYear()+1900){
                            count++;
                        }
                    }
                    reviewPerYear.put("" + year, count);
                }
            }
        }
        return reviewPerYear;
    }
}