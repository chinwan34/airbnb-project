import javafx.scene.control.*; 
import javafx.scene.layout.*; 
import javafx.scene.Node;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import javafx.scene.paint.Color;

/**
 * Show the information of a property onto a VBox panel
 */
public class PropertyInformationViewer
{
    private AirbnbListing property;
    private boolean isShowingAllInfo; // whether we show the totality of the VBox
    
    private VBox propertyBox;
    
    private ViewPropertyWindow propertyViewer;
    
    public PropertyInformationViewer(AirbnbListing property, boolean isShowingAllInfo, ViewPropertyWindow propertyViewer)
    {
        this.property = property;
        this.isShowingAllInfo = isShowingAllInfo;

        this.propertyViewer = propertyViewer;
        
        setupVBox();
    }
    
    /**
     * Returns the VBox pane
     */
    public VBox getPropertyBox(){
        return this.propertyBox;
    }
    
    /**
     * Creates the VBox pane
     */
    private void setupVBox(){
        propertyBox = new VBox();
        propertyBox.getStyleClass().add("propertyBox");
            
        Label hostNameLabel = new Label("Name : " + property.getHost_name());
        Label priceLabel = new Label("Price : " + property.getPrice() + "£");
        Label reviewsLabel = new Label("Number of reviews : " + property.getNumberOfReviews());
        Label minNightsLabel = new Label("Minimum nights for each stay : " + property.getMinimumNights());
        
        //do smth else
        if (isShowingAllInfo){
           Label propertyNameLabel = new Label("Property Name : " + property.getName());
           Label roomTypeLabel = new Label("Room Type : " + property.getRoom_type());
           Label monthlyReviewsLabel = new Label("Number of monthly reviews : " + property.getReviewsPerMonth());
           Label availabilityLabel = new Label("Availability 365 : " + property.getAvailability365());
           
           propertyBox.getChildren().addAll( hostNameLabel, propertyNameLabel, priceLabel, roomTypeLabel, 
                                             reviewsLabel, monthlyReviewsLabel, minNightsLabel, availabilityLabel );
        }

        else propertyBox.getChildren().addAll( hostNameLabel ,priceLabel, reviewsLabel, minNightsLabel );
        
        for ( Node label : propertyBox.getChildren()){
            label.getStyleClass().add("propertyBoxText");
        }
        
        propertyBox.setOnMouseClicked(new EventHandler<MouseEvent>(){
           @Override
           public void handle(MouseEvent event) { 
                propertyViewer.setProperty(property);
                propertyViewer.start();
           }
        });
    }
}
