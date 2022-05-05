import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.scene.shape.TriangleMesh; 

import javax.imageio.ImageIO;
import java.io.IOException;

import java.io.File;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * This class is used to find all minimum contours given an image file's path.
 * It returns a list of all the contours and their coordinates.
 * 
 */
public class ContourFinder
{
    private String imagePath;
    
    //a list containing all "empty" (/white/non-coloured) pixels inside the image
    private CopyOnWriteArrayList<int[]> whitePixels;
    
    private int width;
    private int height;
    
    private int[][] imagePixels;
    
    private ArrayList<TileContour> allTileContours;
    private final Random randomGenerator;
    
    /**
     * Initialises the ContourFinder
     */
    public ContourFinder(String imagePath){
        this.imagePath = imagePath;
        
        randomGenerator = new Random(2003);

        this.allTileContours = new ArrayList<>();
        
        whitePixels = new CopyOnWriteArrayList<>();
        
        this.findColoredPixels();       
        
        this.findTileContours();

    }
    
    /**
     * Returns all the tile contours
     */
    public ArrayList<TileContour> getTileContours(){
        return this.allTileContours;
    }
    
    /**
     * Finds all the tile contours
     */
    public void findTileContours(){
        int id = 1;
        
        while (whitePixels.size() > 0){
            
            int randomIndex = randomGenerator.nextInt(whitePixels.size());
            int[] randomStartingPoint = whitePixels.get( randomIndex );    
            
            TileContour contour = findContour( id, randomStartingPoint, true );
            
            if (contour.isValid()){
                allTileContours.add(contour);
            }
            
            id ++;
            
        }
        
        //test labels
        //String[] labels = new String[] { "A", "B", "C", "D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","0","1","2","3","4","5","6","7","8","9" };
        
        //shortened labels
        //String[] labels = new String[] { "SUTT", "HOUN", "CAMD", "ENFI", "REDB", "HAVE", "STHW", "HARW", "WALT", "CROY", "EALI", "MERT", "GWCH", "RICH", "HRGY", 
        //                               "BREN", "KING", "BROM", "BARN", "BARK", "HAMM", "BEXL", "WSTM", "TOWH", "HACK", "KENS", "LEWS", "WAND", "HILL", "ISLI", "NEWH", "LAMB", "CITY" };
        
        //each label has been hand picked to match the correct contour
        String[] labels = {"Sutton","Hounslow","Camden","Enfield","Redbridge","Havering","Southwark","Harrow","Waltham Forest","Croydon",
                            "Ealing","Merton","Greenwich","Greenwich","Haringey","Brent","Kingston upon Thames","Bromley","Barnet","Barking and Dagenham",
                            "Hammersmith and Fulham","Bexley","Westminster","Tower Hamlets","Hackney","Kensington and Chelsea","Lewisham","Wandsworth","Hillingdon",
                            "Islington","Newham","Lambeth","City of London"};
        
        for (int i = 0; i < labels.length; i++){
            if ( i >= allTileContours.size()) break;
            
            allTileContours.get(i).setLabel(labels[i]);
        }
        
    }
    
    /**
     * Finds a contour inside a map.
     * It fills up an area with a "color" ( changes the white id (-1) to the tile's ( id ) ) until there are no more white pixels.
     * Along the filling up of the area, it adds all the contour coordinates it can find.
     * 
     * The parameter ignoresColored, states whether the program treats certain pixel that are already "colored" as white or not.
     * 
     */
    public TileContour findContour(int id, int[] startingPoint, boolean ignoresColored){
        
        id = (ignoresColored) ? id : id + allTileContours.size();
        TileContour contour = new TileContour(id);
        
        removePixel( whitePixels, startingPoint );
        
        this.imagePixels[ startingPoint [0] ][ startingPoint[1] ] = id;
        
        //all the pixels to be colored
        ArrayList<int[]> exploredPixels = new ArrayList<>();
        ArrayList<int[]> newlyExploredPixels;
        
        exploredPixels.add(startingPoint);
        
        boolean contourIsValid = false;
        
        while (exploredPixels.size() > 0){
            
            newlyExploredPixels = new ArrayList<>();
            
            for ( int[] pixel : exploredPixels){
                for ( int xShift = -1; xShift <= 1; xShift++ ){
                    for (int yShift = -1; yShift <= 1; yShift++){
                        if (!(xShift == 0 && yShift == 0)){
        
                            int row = pixel[0] + yShift;
                            int col = pixel[1] + xShift;
                            
                            if ( ( row < 0 || col < 0 ) || ( row >= imagePixels.length || col >= imagePixels[0].length) ){
                                contour.invalidate();
                                continue;
                            }
                            
                            int pixelId = this.imagePixels[row][col];
                            int[] pixelCoords = new int[] {row, col};
                            
                            //if pixel is white, "color" it and then call this function on the new colored pixel
                            //also avoids the white escaping the contour
                            if ( (pixelId == -1 || (!ignoresColored && pixelId == id - allTileContours.size() )) && (xShift == 0 ^ yShift == 0) ){
                                contourIsValid = true;
                                
                                this.imagePixels[row][col] = id;
                                removePixel( whitePixels, pixelCoords );
                                
                                newlyExploredPixels.add(pixelCoords);
                            //if it's a border then add it to the contour, but normalize it before adding it to the contour
                            }else if (pixelId == 0){
                                contour.addPixel( new double[] {(double) pixelCoords[0]/height, (double) pixelCoords[1]/width} );
                            }
                        }
                    }
                }
            }

            exploredPixels = new ArrayList<>( newlyExploredPixels );
            
        }
        
        if ( !contourIsValid ) contour.invalidate(); 
        
        return contour;
    }
    
    /**
     * Remove pixel coordinate from a list
     */
    private void removePixel( CopyOnWriteArrayList<int[]> list, int[] pixel ){
        for ( int[] e : list){
            if (e[0] == pixel[0] && e[1] == pixel[1]){
                list.remove(e);
            }
        }
    }
    
    /**
     * Returns the aspect ration of the original image
     */
    public double getAspectRatio(){
        return width/height;
    }
    
    /**
     * Scale up the position given
     */
    public int[] scalePos( double[] pos ){
        int[] newPos = new int[2];
        
        newPos[0] = (int) Math.round(pos[0] * height);
        newPos[1] = (int) Math.round(pos[1] * width);
        
        return newPos;
    }
    
    /**
     *  Load in a matrix of pixels, and store them in an array such that every white pixel corresponds to -1, and everything else corresponds to 0.
     */
    private void findColoredPixels(){
        BufferedImage mapImage;

        try {
            
            File mapImageFile = new File(this.imagePath);
            mapImage = ImageIO.read(mapImageFile);
        
        } catch (IOException e) {
           System.out.println("Error loading the map image. Check that such file isn't corrupt.");
           this.imagePixels = null;
           return;
        }
        
        //part of this code was taken from
        //https://stackoverflow.com/questions/6524196/java-get-pixel-array-from-image
        byte[] pixels = ((DataBufferByte) mapImage.getRaster().getDataBuffer()).getData();
        
        width = mapImage.getWidth();
        height = mapImage.getHeight();
        final boolean hasAlphaChannel = mapImage.getAlphaRaster() != null;

        imagePixels = new int[height][width];
        final int pixelLength =  (hasAlphaChannel) ? 4 : 3;
        
        for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
            int argb = 0;
            
            argb += (hasAlphaChannel) ? (((int) pixels[pixel] & 0xff) << 24) : -16777216; // alpha
            argb += ((int) pixels[pixel + 1] & 0xff); // blue
            argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
            argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
            
            //if it isn't white, then assign it 0, otherwise assign -1 for white
            if (argb != -1){
                imagePixels[row][col] = 0;
            }else{
                imagePixels[row][col] = -1;
                whitePixels.add( new int[] { row, col } );
            }
            
            col = (col+1)%width;
            row += (col == 0) ? 1 : 0;
            
        }
    }
}   