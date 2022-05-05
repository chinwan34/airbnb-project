
/**
 * Write a description of class BoroughData here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class BoroughData
{
    
    private double averagePricePerBorough;
    
    private int privateRoomsNumber;
    private int entireRoomsNumber;
    
    private int totalNumberOfProperties;
    
    public BoroughData()
    {
        averagePricePerBorough = 0;
        privateRoomsNumber = 0;
        entireRoomsNumber = 0;
        totalNumberOfProperties = 0;
    }
    
    public double getAveragePricePerBorough(){
        return this.averagePricePerBorough;
    }
    
    public int getPrivateRoomsNumber(){
        return this.privateRoomsNumber;
    }
    
    public int getEntireRoomsNumber(){
        return this.entireRoomsNumber;
    }
    
    public int getTotalNumberOfProperties(){
        return this.totalNumberOfProperties;
    }
    
    public void addAveragePricePerBorough(int price){
        this.averagePricePerBorough += price;
    }
    
    public void averageOutPricePerBorough(){
        if (privateRoomsNumber == 0) return;
        
        this.averagePricePerBorough /= privateRoomsNumber;
    }
    
    public void incrementPrivateRoomsNumber(){
        this.privateRoomsNumber++;
    }
    
    public void incrementEntireRoomsNumber(){
        this.entireRoomsNumber++;
    }
    
    public void incrementTotalPropertiesNumber(){
        this.totalNumberOfProperties++;
    }
}
