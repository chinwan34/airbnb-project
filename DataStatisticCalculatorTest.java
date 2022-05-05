// import static org.junit.jupiter.api.Assertions.*;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import java.util.*;

// /**
 // * The test class DataStatisticCalculatorTest.
 // *
 // * @author  (your name)
 // * @version (a version number or a date)
 // */
// public class DataStatisticCalculatorTest
// {
    // private ArrayList<AirbnbListing> data;
    // private ArrayList<Station> stationData;
    // private DataStatisticCalculator statsWithAllData;
    // private DataStatisticCalculator stats1;
    // private DataStatisticCalculator stats2;
    // private DataStatisticCalculator stats3;
    // /**
     // * Default constructor for test class DataStatisticCalculatorTest
     // */
    // public DataStatisticCalculatorTest(int fromPrice, int toPrice)
    // {
    // }

    // /**
     // * Sets up the test fixture.
     // *
     // * Called before every test case method.
     // */
    // @BeforeEach
    // public void setUp()
    // {
        // data = new AirbnbDataLoader().load();
        // stationData = new AirbnbDataLoader().loadStations();
        // stats1 = new DataStatisticCalculator(statsWithAllData.getListingsInPriceRange(0, 50), stationData);
        // stats2 = new DataStatisticCalculator(statsWithAllData.getListingsInPriceRange(0, 0), stationData);
        // stats3 = new DataStatisticCalculator(statsWithAllData.getListingsInPriceRange(100, 200), stationData);
    // }

    // /**
     // * Tears down the test fixture.
     // *
     // * Called after every test case method.
     // */
    // @AfterEach
    // public void tearDown()
    // {
        
    // }
    
    // @Test
    // public void testgetData()
    // {
        // assertNotNull(stats1.getData());
        // assertNotNull(stats2.getData());
        // assertNotNull(stats3.getData());
    // }
    
    // @Test
    // public void testgetStationData()
    // {
        // assertNotNull(stats1.getStationData());
        // assertNotNull(stats2.getStationData());
        // assertNotNull(stats3.getStationData());
    // }
    
    // @Test
    // public void testgetMaxPrice()
    // {
        // assertNotNull(stats1.getMaxPrice());
        // assertNotNull(stats3.getMaxPrice());
        
        // assertTrue(stats1.getMaxPrice() > 0);
        // assertTrue(stats3.getMaxPrice() > 0);
        // assertSame(0, stats2.getMaxPrice());
    // }
    
    // @Test
    // public void testgetPriceRange()
    // {
       // assertTrue(stats1.getPriceRange().contains(50));
       // assertFalse(stats1.getPriceRange().contains(1000));
       // assertTrue(stats2.getPriceRange().contains(0));
       // assertFalse(stats2.getPriceRange().contains(400));
       // assertTrue(stats3.getPriceRange().contains(0));
       // assertFalse(stats3.getPriceRange().contains(1000));
    // }
    
    // @Test
    // public void testgetListingsInPriceRange()
    // {
        
    // }
    
    // @Test
    // public void testaverageReview() 
    // {
        // assertNotNull(stats1.averageReview());
        // assertNotNull(stats3.averageReview());
        
        // assertTrue(stats1.averageReview() > 0);
        // assertSame(0, stats2.averageReview());
        // assertTrue(stats3.averageReview() > 0);
    // }
    
    // @Test
    // public void testtotalNumberOfProperties()
    // {
        // assertNotNull(stats1.totalNumberOfProperties());
        // assertNotNull(stats3.totalNumberOfProperties());
        
        // assertTrue(stats1.totalNumberOfProperties() > 0);
        // assertSame(0, stats2.totalNumberOfProperties());
        // assertTrue(stats3.totalNumberOfProperties() > 0);
    // }
    
    // @Test
    // public void testtotalNumberOfEntireHomeApt()
    // {
        // assertNotNull(stats1.totalNumberOfEntireHomeApt());
        // assertNotNull(stats3.totalNumberOfEntireHomeApt());
        
        // assertTrue(stats1.totalNumberOfEntireHomeApt() > 0);
        // assertSame(0, stats2.totalNumberOfEntireHomeApt());
        // assertTrue(stats3.totalNumberOfEntireHomeApt() > 0);
    // }
    
    // @Test
    // public void testprivateRoomsPerBorough()
    // {
        // assertNotNull(stats1.privateRoomsPerBorough());
        // assertNotNull(stats3.privateRoomsPerBorough());
        
        // assertTrue(stats1.privateRoomsPerBorough().size() > 0);
        // assertSame(0, stats2.privateRoomsPerBorough().size());
        // assertTrue(stats3.privateRoomsPerBorough().size() > 0);
    // }
    
    // @Test
    // public void testentireHomeAptPerBorough()
    // {
        // assertNotNull(stats1.entireHomeAptPerBorough());
        // assertNotNull(stats3.entireHomeAptPerBorough());
        
        // assertTrue(stats1.entireHomeAptPerBorough().size() > 0);
        // assertSame(0, stats2.entireHomeAptPerBorough().size());
        // assertTrue(stats3.entireHomeAptPerBorough().size() > 0);
    // }
    
    // @Test
    // public void testmostExpensiveBorough()
    // {
        // assertNotNull(stats1.mostExpensiveBorough());
        // assertNotNull(stats3.mostExpensiveBorough());
        
        // assertEquals("", stats2.mostExpensiveBorough());
    // }
    
    // @Test
    // public void testaveragePricePerBorough()
    // {
        // assertNotNull(stats1.averagePricePerBorough());
        // assertNotNull(stats3.averagePricePerBorough());
        
        // assertTrue(stats1.averagePricePerBorough().size() > 0);
        // assertSame(null, stats2.averagePricePerBorough());
        // assertTrue(stats3.averagePricePerBorough().size() > 0);
    // }
    
    // @Test
    // public void testaverageAvailabilityTime()
    // {
        // assertNotNull(stats1.averageAvailabilityTime());
        // assertNotNull(stats3.averageAvailabilityTime());
        
        // assertNotEquals("0 days", stats1.averageAvailabilityTime());
        // assertEquals("0 days", stats2.averageAvailabilityTime());
        // assertNotEquals("0 days", stats3.averageAvailabilityTime());
    // }
    
    // @Test
    // public void testaverageTimeLastReview() throws java.text.ParseException
    // {
        // assertNotNull(stats1.averageAvailabilityTime());
        // assertNotNull(stats3.averageAvailabilityTime());
        
        // assertNotEquals("0 years 0 days", stats1.averageTimeLastReview());
        // assertEquals("0 years 0 days", stats2.averageTimeLastReview());
        // assertNotEquals("0 years 0 days", stats3.averageTimeLastReview());
    // }
    
    // @Test
    // public void testaverageDistanceToTube()
    // {
        // assertNotNull(stats1.averageDistanceToTube());
        // assertNotNull(stats3.averageDistanceToTube());
        
        // assertNotEquals("0 m", stats1.averageDistanceToTube());
        // assertEquals("0 m", stats2.averageDistanceToTube());
        // assertNotEquals("0 m", stats3.averageDistanceToTube());
    // }
    
    // @Test
    // public void testdistanceBetweenCoordinates()
    // {
        // assertSame(0, stats1.distanceBetweenCoordinates(0, 0, 0, 0));
    // }
    
    // @Test
    // public void testmostReviewedHost()
    // {
        // assertNotNull(stats1.mostReviewedHost());
        // assertNotNull(stats3.mostReviewedHost());
        
        // assertNotEquals("Not found", stats1.mostReviewedHost());
        // assertEquals("Not found", stats2.mostReviewedHost());
        // assertNotEquals("Not found", stats3.mostReviewedHost());   
    // }
    
    // @Test
    // public void testlistOfMostReviewedHosts() 
    // {
        // assertNotNull(stats1.listOfMostReviewedHosts());
        // assertNotNull(stats3.listOfMostReviewedHosts());
        
        // assertTrue(stats1.listOfMostReviewedHosts().size() > 0);
        // assertSame(null, stats2.listOfMostReviewedHosts() );
        // assertTrue(stats3.listOfMostReviewedHosts().size() > 0);
    // }
    
    // @Test
    // public void testaverageReviewEachYear() throws java.text.ParseException
    // {
        // assertNotNull(stats1.averageReviewEachYear());
        // assertNotNull(stats3.averageReviewEachYear());
        
        // assertTrue(stats1.averageReviewEachYear().size() > 0);
        // assertSame(null, stats2.averageReviewEachYear() );
        // assertTrue(stats3.averageReviewEachYear().size() > 0);
    // }
    
// }
