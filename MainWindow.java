import javafx.application.Application; 
import javafx.stage.Stage; 
import javafx.scene.Scene; 
import javafx.scene.control.*; 
import javafx.scene.layout.*; 

import javafx.scene.paint.Color;
import java.util.ArrayList;
import javafx.geometry.Pos;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.beans.value.ObservableValue;
import javafx.scene.*;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.scene.control.Alert;


/**
 * This is the main window of the application, this holds three panels : the welcome screen, the map panel and the statistics panel.
 * 
 * It allows the user to select a price range and thus select properties within that price range.
 * 
 * 
 * authors
 */
public class MainWindow extends Application 
{ 
    
    private ArrayList<AirbnbListing> listings;
    private ArrayList<Station> stations;
    
    //the pbject containing all the listings' statistics
    private DataStatisticCalculator stats;
    
    private Integer fromPriceChosen;
    private Integer toPriceChosen;
    
    private BorderPane root;
    
    private Stage stage;
    
    //arraylist containing all the panes shown on the center
    private ArrayList<PaneBuilder> centerPaneBuilders;
    private int currentPaneIndex;
    
    private WelcomePaneBuilder welcomePane;
    
    public MainWindow(){
        // set the welcome pane to the center
        this.welcomePane = new WelcomePaneBuilder( 1080, 720);
    }
    
    
    /**
     * Sets up the panes
     */
    @Override
    public void start(Stage stage) 
    { 
        
        AirbnbDataLoader loader = new AirbnbDataLoader();
        
        listings = loader.load();
        stations = loader.loadStations();
        
        stats = new DataStatisticCalculator(listings, stations);

        currentPaneIndex = -1;
        
        fromPriceChosen = 0;
        toPriceChosen = 0;
        
        root = new BorderPane();
        
        BorderPane buttonPane = new BorderPane();        
        GridPane scrollerPane = new GridPane();
        
        setBorder(buttonPane);
        setBorder(scrollerPane);
        
        setBottomButtons(buttonPane);
        setScrollers(scrollerPane);
        
        root.setBottom(buttonPane);
        root.setTop(scrollerPane);
        
        setupCenterPanes();
        root.setCenter( welcomePane.getPane() );
        
        Scene scene = new Scene(root, 1080, 720);
        scene.getStylesheets().add("style.css");
        
        stage.setTitle("AirBnB Viewer"); 
        stage.setScene(scene);
                
        stage.show();
        stage.setMaxHeight(720);
        stage.setMinHeight(720);
        
        stage.setMaxWidth(1100);
        stage.setMinWidth(1100);
    } 

    /**
     * Sets up the map and statistics panes
     */
    private void setupCenterPanes(){
        centerPaneBuilders = new ArrayList<>();
        
        centerPaneBuilders.add( new MapPaneBuilder(stats) );
        
        centerPaneBuilders.add( new StatisticsPaneBuilder(listings, stations) );
    }
    
    /**
     * Changes a pane given an index "change" ( e.g: indexChange = +1 would be going to the next element)
     */
    private void changePanes(int indexChange){
        int size = centerPaneBuilders.size();
        
        currentPaneIndex = ((currentPaneIndex + indexChange) + size) % (size);
        PaneBuilder currentPaneBuilder = centerPaneBuilders.get( currentPaneIndex );
        currentPaneBuilder.actionOnChange();
        
        root.setCenter( currentPaneBuilder.getPane() );        
    }
    
    /**
     * Sets a border style to a pane
     */
    private void setBorder( Pane Pane ){
        Pane.setBorder(new Border(new BorderStroke(Color.BLACK, 
            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }
    
    /**
     * Sets up the bottom buttons used to go through panes
     */
    private void setBottomButtons( BorderPane Pane ){
        Button forwardButton = new Button();
        Button backwardButton = new Button();
        
        HBox helpButtons = new HBox();
        
        Button helpButton = new Button();
        Button exitHelp = new Button();
        
        double padding = 5.0;
        
        Image imageForward = new Image(getClass().getResourceAsStream("graphics/right_arrow.png"));
        forwardButton.setGraphic(new ImageView(imageForward));
        
        Image imageBackward = new Image(getClass().getResourceAsStream("graphics/left_arrow.png"));
        backwardButton.setGraphic(new ImageView(imageBackward));
        
        helpButton.setText("?");
        exitHelp.setText("X");
        
        forwardButton.setOnAction(this::forwardButtonPressed);
        backwardButton.setOnAction(this::backwardButtonPressed);
        helpButton.setOnAction(this::helpButtonPressed);
        exitHelp.setOnAction(this::exitHelpButtonPressed);
        
        Pane.setLeft(backwardButton);
        Pane.setRight(forwardButton);
        
        helpButtons.getChildren().addAll( helpButton, exitHelp );
        helpButtons.setAlignment(Pos.CENTER);
        
        for ( Node button : helpButtons.getChildren() ){
            button.getStyleClass().add("selectorButton");
            button.getStyleClass().add("centerNode");
        }
        
        Pane.setCenter(helpButtons);
        
        for ( Node button : Pane.getChildren() ){
            button.getStyleClass().add("selectorButton");
            button.getStyleClass().add("centerNode");
        }
        
    }
    
    /**
     * When called, the next pane is shown, only when the price range is valid
     */
    private void forwardButtonPressed(ActionEvent evt){
        if ( toPriceChosen - fromPriceChosen  > 0){
            changePanes(1);
        }else {
            showPriceAlert(evt);
        }
    }

    /**
     * When called, the previous pane is shown, only when the price range is valid
     */
    private void backwardButtonPressed(ActionEvent evt){
        if ( toPriceChosen - fromPriceChosen  > 0){
            changePanes(-1);
        }else {
            showPriceAlert(evt);
        }
    }
    
    /**
     * Sets up the help pane instead of the usual pane in the center
     */
    private void helpButtonPressed(ActionEvent evt){
        if (currentPaneIndex >= 0) {
            centerPaneBuilders.get(currentPaneIndex).showHelp();
            changePanes(0);
        }
        else {
            welcomePane.showHelp();
            root.setCenter(welcomePane.getPane());
        }        
    }
    
    private void exitHelpButtonPressed(ActionEvent evt){
        if (currentPaneIndex >= 0) {
            changePanes(0);
        }
        else {
            root.setCenter(welcomePane.getPane());
        }   
    }
    
    /**
     * Alert that pops up when we input an invalid range of prices for the listings
     */
    private void showPriceAlert(ActionEvent event) 
    { 
        Alert alert = new Alert(Alert.AlertType.INFORMATION); 
        alert.setTitle("About the price range chosen"); 
        alert.setHeaderText(null); 
        alert.setContentText("Choose a suitable price range."); 
        alert.showAndWait(); 
    }
    
    /**
     * Set up the scrollers to choose the prices
     */
    private void setScrollers(GridPane Pane){
        
        ArrayList<String> priceRanges = stats.getPriceRange();
        
        Pane fromPriceScroller = makeScroller("From :", priceRanges);
        GridPane toPriceScroller = makeScroller("To :", priceRanges);
                        
        Pane.setAlignment(Pos.CENTER_RIGHT);
        
        ChoiceBox<String> toPriceChoiceBox = (ChoiceBox<String>) toPriceScroller.getChildren().get(1);
    
        //both code is similar try to make it one function
        
        toPriceChoiceBox.getSelectionModel()
        .selectedItemProperty()
        .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            this.toPriceChosen = priceStringToInt(newValue);
            
            //if invalid range
            if (toPriceChosen < fromPriceChosen){
                toPriceChoiceBox.setValue(oldValue);
                this.toPriceChosen = priceStringToInt(oldValue);

                showPriceAlert(null);
            }
            
            setNewPricesChosen();
        });
        
        ChoiceBox<String> fromPriceChoiceBox = (ChoiceBox<String>) fromPriceScroller.getChildren().get(1);
                
        fromPriceChoiceBox.getSelectionModel()
        .selectedItemProperty()
        .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            this.fromPriceChosen = priceStringToInt(newValue);
            
            //if invalid range
            if (toPriceChosen < fromPriceChosen){
                fromPriceChoiceBox.setValue(oldValue);
                this.fromPriceChosen = priceStringToInt(oldValue);
                
                showPriceAlert(null);
            }
        });
        
        Pane.add(fromPriceScroller, 0, 0);
        Pane.add(toPriceScroller, 1, 0);

    }
    
    /**
     * Update the new price ranges to all panes
     */
    private void setNewPricesChosen(){
        for (PaneBuilder centerPane : centerPaneBuilders){
            centerPane.setFromPriceChosen(this.fromPriceChosen);
            centerPane.setToPriceChosen(this.toPriceChosen);
            centerPane.actionOnChange();
        }
    }
    
    /**
     * Converts a string with a "£" at the end to an integer
     */
    private Integer priceStringToInt(String priceString){
        return Integer.parseInt(priceString.split("£")[0]);
    }
    
    /**
     * Create a scroller given an arraylist of choices to put inside the scroller
     */
    private GridPane makeScroller(String label, ArrayList<String> scrollerChoices){
        
        GridPane scroller = new GridPane();
        scroller.setAlignment(Pos.CENTER);
        
        Label scrollerLabel = new Label(label);
        scrollerLabel.getStyleClass().add("centerNode");

        ChoiceBox<String> choiceBox = new ChoiceBox();
        choiceBox.getStyleClass().add("centerNode");

        choiceBox.getItems().addAll(scrollerChoices);
        choiceBox.setValue(scrollerChoices.get(0));
            
        scroller.add(scrollerLabel, 0,0);
        scroller.add(choiceBox, 1, 0);
        
        return scroller;
    }
}