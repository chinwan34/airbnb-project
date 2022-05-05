import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

import java.util.Collections;
/**
 * Class that stores data about the contour of a tile.
 */
public class TileContour
{
    private ArrayList<double[]> contourCoordinates;
    private int id;
    
    private boolean isValid;
    private Double[] center;
    
    //box containing the entire tile
    private Double[] boundaries;
    private String label;
    
    public TileContour(int id){
        
        this.id = id;
        this.isValid = true;
        
        this.label = "";
        
        this.contourCoordinates = new ArrayList<>();
        
        this.center = new Double[] {0d, 0d};
        
        //min y, max y, min x, max x
        this.boundaries = new Double[] {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
    }
    
    /**
     * Invalidates the status of the tile
     */
    public void invalidate(){
        this.isValid = false;
    }
    
    /**
     * Returns whether the tile is valid or not
     */
    public boolean isValid(){
        return this.isValid;
    }
    
    /**
     * Returns the tile's id
     */
    public int getId(){
        return this.id;
    }
    
    /**
     * Sets the tile's id
     */
    public void setId(int newId){
        this.id = newId;
    }
    
    /**
     * Sets the tile's label
     */
    public void setLabel(String newLabel){
        this.label = newLabel;
    }
    
    /**
     * Returns the tile's label
     */
    public String getLabel(){
        return this.label;
    }
    
    /**
     * Returns whether the point given is within the boundaries of the tile
     */
    public boolean isPointInsideContour(double[] point){
        if ((point[0] >= boundaries[0] && point[0] <= boundaries[1] ) 
                && (point[1] >= boundaries[2] && point[1] <= boundaries[3])){
                    return true;
        }
        
        return false;
    }
    
    /**
     * Add a contour pixel to the contour coordinates
     */
    public void addPixel(double[] newPixel){
        contourCoordinates.add(newPixel);
            
        updateFields(newPixel);
    }
    
    /**
     * Update the various fields characterizing this object.
     * Namely the center of the object, and it's boundaries.
     */
    private void updateFields(double[] newPixel){
        int size = contourCoordinates.size();

        center[0] = ( center[0]*(size - 1) + newPixel[0] )/size;
        center[1] = ( center[1]*(size - 1) + newPixel[1] )/size;
        
        if (newPixel[0] < boundaries[0]) boundaries[0] = newPixel[0];
        if (newPixel[0] > boundaries[1]) boundaries[1] = newPixel[0];

        if (newPixel[1] < boundaries[2]) boundaries[2] = newPixel[1];
        else if (newPixel[1] > boundaries[3]) boundaries[3] = newPixel[1];        
    }
    
    /**
     * Equality check between two contours is done with their centers
     */
    public boolean equals(TileContour contour){
        
        if (!this.hasCenter() || !contour.hasCenter()) return false;
        
        return ( ( Math.abs(this.center[0] - contour.getCenter()[0]) < 0.1 ) && 
                 ( Math.abs(this.center[1] - contour.getCenter()[1]) < 0.1) );
                 
    }
    
    /**
     * Returns whether the tile has a center
     */
    public boolean hasCenter(){
        return !( this.center[0] == null || this.center[1] == null );
    }
    
    /**
     * Returns the tile's center
     */
    public Double[] getCenter(){
        if (!this.hasCenter()) return null;
        return this.center;
    }
}
