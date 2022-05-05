import javafx.scene.layout.*; 
import javafx.scene.control.*; 
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

import javafx.scene.shape.*;
import javafx.scene.Group;

import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.animation.*;
import javafx.scene.paint.Color;

import javafx.geometry.Pos;

/**
 * Class building the map pane
 */
public class MapPaneBuilder extends PaneBuilder
{
    private DataStatisticCalculator stats;
    private DataStatisticCalculator boroughStats;
    
    private int WIDTH;
    private int HEIGHT = 600;
    
    private ContourFinder finder;
    private ArrayList<TileContour> allTiles;
    
    private ViewPropertiesWindow propertiesViewer;
    
    private AnchorPane innerPane;
    private AnchorPane tickPane;
    
    public MapPaneBuilder( DataStatisticCalculator stats ){
        
        buildHelpMapPane();
        
        this.stats = stats ;
        pane = new BorderPane();
        
        finder = new ContourFinder("./graphics/london_map.png");
        allTiles = finder.getTileContours();
    
        WIDTH = (int) Math.round( HEIGHT*finder.getAspectRatio() );
        
        drawTiles();

    }
    
    /**
     * Returns whether the position given is inside the map's contour
     */
    private boolean isPointInside(double[] mousePos, TileContour tile){
        //pre-eliminary check
        if ( !tile.isPointInsideContour( mousePos )) return false;
        
        int[] scaledMousePos = finder.scalePos(mousePos);
        
        TileContour newContour = finder.findContour( tile.getId(), scaledMousePos, false );
        if (!newContour.isValid()) return false;
        
        if (tile.equals(newContour)){
            tile.setId( newContour.getId() );
            return true;
        }
        
        return false;
        
    }
    
    /**
     * Simple animation used when the map is clicked
     */
    private void mouseClickAnimation(double[] mousePos){
        
        int radius = 5;
        
        Circle circle = new Circle(mousePos[0], mousePos[1] - 5*radius ,radius);

        circle.setStroke(Color.BLUE);
        circle.setStrokeWidth(2);

        circle.setFill(Color.TRANSPARENT);
        circle.setSmooth(true);
        
        pane.getChildren().add(circle); 
        
        ScaleTransition smallScale = new ScaleTransition(Duration.millis(500));  
        smallScale.setByX(1.3f);  
        smallScale.setByY(1.3f); 
        
        FadeTransition fadeOut = new FadeTransition();  
        fadeOut.setFromValue(1.0);  
        fadeOut.setToValue(0.0);  
        fadeOut.setDuration(Duration.millis(500));
        
        ScaleTransition scale = new ScaleTransition(Duration.millis(750));  
        scale.setByX(3f);  
        scale.setByY(3f);  
        scale.setAutoReverse(true);
        scale.setCycleCount(3);
        
        ParallelTransition fadeAway = new ParallelTransition(circle, scale, fadeOut);
        
        SequentialTransition animation = new SequentialTransition( circle, scale, fadeAway );
        animation.setOnFinished( e -> pane.getChildren().remove(circle));
        
        animation.play();
        
    }
    
    /**
     * Draw the map onto the screen 
     */
    private void drawTiles(){
        
        innerPane = new AnchorPane();
        
        //taken from : 
        //https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.local-government.org.uk%2Flondon.html&psig=AOvVaw0rcRHg-CIlPPDi3E-vDrCV&ust=1648722405189000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCPD-lOPP7fYCFQAAAAAdAAAAABAO
        Image image = new Image("graphics/boroughs_map.png");
        ImageView view = new ImageView(image);
        
        view.setFitWidth(WIDTH);
        view.setFitHeight(HEIGHT);
        
        innerPane.setOnMouseClicked(new EventHandler<MouseEvent>(){
           @Override
           public void handle(MouseEvent event) { 

               double normX = (event.getX() - 1 - (1080 - WIDTH)/2)/WIDTH;
               //due to the way click works
               double normY = (event.getY() - 1 - (650 - HEIGHT)/2)/HEIGHT;
                              
               double[] normMousePos = new double[] {normY, normX};
               double[] mousePos = new double[] { event.getSceneX(), event.getSceneY() };
                              
               //find tile clicked
               for (TileContour tile : allTiles){
                   if (isPointInside( normMousePos, tile )){
                                              
                       mouseClickAnimation( mousePos );
                       
                       //only open one instance of the properties viewer
                       if (propertiesViewer == null) propertiesViewer = new ViewPropertiesWindow( boroughStats.getListingsInBorough( tile.getLabel() ), tile.getLabel() );
                       else {
                           propertiesViewer.setProperties( boroughStats.getListingsInBorough( tile.getLabel() ));
                           propertiesViewer.setLabel(tile.getLabel());
                           
                           propertiesViewer.start();
                           
                       }
                       break;
                   }
               }
           }
        });
                
        innerPane.getChildren().add(view);
        
        AnchorPane.setTopAnchor(view, (double) (650 - HEIGHT)/2);
        AnchorPane.setLeftAnchor(view, (double) (1080 - WIDTH)/2);
        
        ((BorderPane) pane).setCenter( innerPane );
    }
    
    /**
     * Add ticks symbolising the properties densities within a particular borough
     */
    private void addTicks(){
        if (tickPane != null){
            innerPane.getChildren().remove(tickPane);
        }

        tickPane = new AnchorPane();
        
        if (fromPriceChosen == 0 && toPriceChosen == 0) return;
                
        for (TileContour tile : allTiles){
            
            Polygon tick = new Polygon();
            
            ArrayList<AirbnbListing> properties = boroughStats.getListingsInBorough( tile.getLabel() );
            int size = properties.size();
            
            if (size == 0) continue;
            
            double factor = (double) size / 500;
            int redHue = (factor > 1) ? 255 : (int) Math.round((double) 255*factor);
            
            Color color = Color.rgb(redHue, 0, 0, 0.75);

            //base width of the tick            
            double width = 7;
            
            double centerX = Math.round(tile.getCenter()[1]*WIDTH);
            double centerY = Math.round(tile.getCenter()[0]*HEIGHT) - 50;
            
            tick.getPoints().setAll(new Double[] {
                
                    centerX, centerY,
                    centerX + width, centerY - 3*width,
                    centerX - width, centerY - 3*width,
                    
            });
            
            tick.setFill(color);
            tickPane.getChildren().add(tick);
            
            AnchorPane.setTopAnchor( tick, centerY + 30 + (650 - HEIGHT)/2);
            AnchorPane.setLeftAnchor( tick, centerX - 10 + (1080 - WIDTH)/2);
        }   
        
        innerPane.getChildren().add(tickPane);
    }

    /**
     * Build up the help pane for the map
     */
    private void buildHelpMapPane()
    {
        String type = "map";
        HelpScreen hs = new HelpScreen(type);
        helpPane = hs.getPane();
        //((BorderPane) helpPane).setCenter(new Label("help map")); delete
    }
    
    /**
     * When the value of the price range is changed, update the ticks
     */
    public void actionOnChange(){
        boroughStats = new DataStatisticCalculator( stats.getListingsInPriceRange(fromPriceChosen, toPriceChosen), stats.getStationData() ) ;   
        addTicks();
    }
    
}
