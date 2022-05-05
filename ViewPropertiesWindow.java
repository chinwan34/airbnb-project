import javafx.stage.Stage; 
import javafx.scene.Scene; 
import javafx.scene.control.*; 
import javafx.scene.layout.*; 
import javafx.event.ActionEvent;
import javafx.scene.Node;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Show a list of properties inside a borough to the viewer
 */
public class ViewPropertiesWindow
{ 
    private ArrayList<AirbnbListing> listings;  //listings to be sorted by borough
    private String boroughName;
    
    private BorderPane root;
    private VBox rootProperties;
    private Stage stage;
    
    private boolean isShowing;
    
    private int indexOfFirstProperty;   //the index of the first property shown
    
    private final int propertiesPerPage = 100;
        
    //default is descending order
    private int sortOrder;
        
    enum SortType {
        NONE,
        PRICE,
        REVIEWS,
        ALPHABETICAL
    }
    
    private SortType currentSortType;
    
    public ViewPropertiesWindow(ArrayList<AirbnbListing> listings, String boroughName){
        this.listings = listings;
        this.boroughName = boroughName;
        
        this.isShowing = false;
        
        this.sortOrder = 1;
        this.currentSortType = SortType.NONE;
        
        this.indexOfFirstProperty = 0;
        
        this.start();
    }
    
    /**
     * Set up all the properties to the new ones given
     */
    public void setProperties(ArrayList<AirbnbListing> newListings){
        this.listings = newListings;
    }
    
    /**
     * Set the new borough label to the one given
     */
    public void setLabel( String newBoroughName ){
        this.boroughName = newBoroughName;
    }
    
    public void start(){
        
        //only one instance open at all times
        if (stage == null) {
            stage = new Stage();
            stage.setOnCloseRequest( e -> isShowing = true );

            isShowing = true;
        }
        
        root = new BorderPane();
        rootProperties = new VBox();
                
        setupMenu();
        setupBottomButtons();
        setPropertiesOnPanel();
        
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(rootProperties); 
        scroll.setFitToWidth(true);    
        
        root.setCenter(scroll);
        
        Scene scene = new Scene(root, 480, 720);
        scene.getStylesheets().add("style.css");

        
        stage.setTitle(boroughName + "'s Properties"); 
        stage.setScene(scene);
    
        if (isShowing) {
            stage.show();
            isShowing = false;
        }
    }
    
    /**
     * Setup the properties onto the panel
     */
    private void setPropertiesOnPanel(){
        
        ViewPropertyWindow viewer = new ViewPropertyWindow(null);
        
        for (int i = 0; (i < propertiesPerPage) && (i + indexOfFirstProperty <  listings.size()); i++){
            AirbnbListing listing = listings.get(i + indexOfFirstProperty);
            VBox listingBox = new PropertyInformationViewer(listing, false, viewer).getPropertyBox();
    
            rootProperties.getChildren().add(listingBox);
        }
    }
    
    /**
     * Setup the menu used to sort through the properties shown
     */
    private void setupMenu(){
        Menu sortByMenu = new Menu("Sort By");
        Menu sortType = new Menu("Sort Type");
        
        MenuItem priceItem = new MenuItem("Price");
        MenuItem reviewsItem = new MenuItem("Reviews Number");
        MenuItem alphabeticalItem = new MenuItem("Alphabetical Order of Host Name");

        priceItem.setOnAction(this::sortByPrice);
        reviewsItem.setOnAction(this::sortByReviews);
        alphabeticalItem.setOnAction(this::sortByAlphabeticalOrder);

        sortByMenu.getItems().addAll(priceItem, reviewsItem, alphabeticalItem);

        MenuItem ascendingItem = new MenuItem("Ascending");
        MenuItem descendingItem = new MenuItem("Descending");
        
        ascendingItem.setOnAction(this::ascendingSort);
        descendingItem.setOnAction(this::descendingSort);
        
        sortType.getItems().addAll(ascendingItem, descendingItem);
        
        MenuBar menuBar = new MenuBar();
        
        menuBar.getMenus().addAll(sortByMenu, sortType);
        
        root.setTop(menuBar);
    }
    
    /**
     * Sort properties by price
     */
    private void sortByPrice(ActionEvent evt){
        currentSortType = SortType.PRICE;
        
        Collections.sort(listings, new Comparator<AirbnbListing>()
        {
               public int compare(AirbnbListing listing1, AirbnbListing listing2){
                  return (listing1.getPrice() - listing2.getPrice())*sortOrder;
               }
        });
                
        start();
    }
    
    /**
     * Sort properties by review number
     */
    private void sortByReviews(ActionEvent evt){
        currentSortType = SortType.REVIEWS;
        
        Collections.sort(listings, new Comparator<AirbnbListing>()
        {
               public int compare(AirbnbListing listing1, AirbnbListing listing2){
                  return (listing1.getNumberOfReviews() - listing2.getNumberOfReviews())*sortOrder;
               }
        });
                
        start();
    }
    
    /**
     * Sort properties by alphabetical order of the host name
     */
    private void sortByAlphabeticalOrder(ActionEvent evt){
        currentSortType = SortType.ALPHABETICAL;
        
        Collections.sort(listings, new Comparator<AirbnbListing>()
        {
               public int compare(AirbnbListing listing1, AirbnbListing listing2){
                  return compareAlphabetically(listing1.getHost_name(), listing2.getHost_name());
               }
        });
                
        start();
    }
    
    /**
     * Compare two strings alphabetically.
     */
    //if 1 is returned, then bigger < smaller, else bigger > smaller
    private int compareAlphabetically(String a, String b){
        
        //if they're the same, then it doesn't matter which order they're in
        if (a.equals(b)) return 1;
        
        //here we choose which string is compared, the smaller one comes before the bigger one alphabetically
        String smaller = ( sortOrder > 0 ) ? b : a;
        String bigger = ( sortOrder > 0 ) ? a : b;
        
        for ( int i = 0; i < bigger.length(); i++){
            //if the bigger string is longer than the other one, then it does indeed come after the other one alphabetically
            if ( i >= smaller.length() ) return 1;
            
            if ( bigger.charAt(i) < smaller.charAt(i) ) return 1;
            else if ( bigger.charAt(i) > smaller.charAt(i) ) return -1;
            
        }
        
        //if the smaller string is longer than the other one, then it comes after the other one alphabetically
        return -1;
        
    }
    
    /**
     * Sets the sort type to ascending
     */
    private void ascendingSort(ActionEvent evt){
        sortOrder = -1;
        this.showUpdatedSort();
    }
    
    /**
     * Sets the sort type to descending
     */
    private void descendingSort(ActionEvent evt){
        sortOrder = 1;
        this.showUpdatedSort();
    }
    
    /**
     * Show to the user the updated version of the window
     */
    private void showUpdatedSort(){
        switch (currentSortType){
            case PRICE:
                this.sortByPrice(null);
                break;
                
            case REVIEWS:
                this.sortByReviews(null);
                break;
                
            case ALPHABETICAL:
                this.sortByAlphabeticalOrder(null);
                break;
                
            case NONE:
                start();
                break;
        }
    }
    
    /**
     * Setup the button buttons used to navigate through pages of properties.
     */
    private void setupBottomButtons(){
        BorderPane buttonPane = new BorderPane();
        
        Button previousPageButton = new Button();
        Button nextPageButton = new Button();
        
        
        Image imageBackward = new Image(getClass().getResourceAsStream("graphics/left_arrow.png"));
        previousPageButton.setGraphic(new ImageView(imageBackward));
        
        Image imageForward = new Image(getClass().getResourceAsStream("graphics/right_arrow.png"));
        nextPageButton.setGraphic(new ImageView(imageForward));
        
        previousPageButton.setOnAction(this::previousPageChange);
        nextPageButton.setOnAction(this::nextPageChange);  
        
        if ( indexOfFirstProperty - propertiesPerPage >= 0 ) buttonPane.setLeft(previousPageButton);
        if ( indexOfFirstProperty + propertiesPerPage < this.listings.size() ) buttonPane.setRight(nextPageButton);
        
        for ( Node button : buttonPane.getChildren() ){
            button.getStyleClass().add("selectorButton");
            button.getStyleClass().add("centerNode");
        }
        
        root.setBottom( buttonPane );
        
    }
    
    /**
     * Go to the previous page of properties
     */
    private void previousPageChange(ActionEvent evt){
        indexOfFirstProperty -= propertiesPerPage;
        this.showUpdatedSort();
    }
    
    /**
     * Go to the next page of properties
     */
    private void nextPageChange(ActionEvent evt){
        indexOfFirstProperty += propertiesPerPage;
        this.showUpdatedSort();
    }
    
}