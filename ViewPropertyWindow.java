import javafx.stage.Stage; 
import javafx.scene.Scene; 
import javafx.scene.control.*; 
import javafx.scene.layout.*; 

/**
 * This class shows a single property and its information on a new window
 */
public class ViewPropertyWindow
{
    private AirbnbListing property;
    private Stage stage;
    
    private boolean isShowing;
    
    public ViewPropertyWindow( AirbnbListing property )
    {
        this.property = property;
        this.isShowing = false;

        if (this.property != null) this.start();
    }
    
    /**
     * Sets the property to the one given
     */
    public void setProperty( AirbnbListing property ){
        this.property = property;
    }
    
    public void start(){
        
        //only one window open at all times
        if (stage == null){
            stage = new Stage();
            stage.setOnCloseRequest( e -> isShowing = true );
            
            isShowing = true;
        }
        
        VBox root = new VBox();                

        Scene scene = new Scene(root);
        scene.getStylesheets().add("style.css");
        
        VBox listingBox = new PropertyInformationViewer(property, true, this).getPropertyBox();
        root.getChildren().add(listingBox);
        
        
        stage.setTitle( property.getHost_name() + "'s Property"); 
        stage.setScene(scene);
                    
        if (isShowing) {
            stage.show();
            isShowing = false;
        }
    }
}
