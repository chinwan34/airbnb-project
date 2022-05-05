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
 * This class, is used as various Help screens to explain how everything work on the project
 *
 * @author (Ata Senpolat @k20061940)
 */
public class HelpScreen 
{
    private BorderPane pane;
    private int stage;
    
    private ArrayList<Image> helpScreenshots;
    
    private int screenshotIndex;
    
    
    public HelpScreen(String type)
    {
        pane = new BorderPane();
        helpScreenshots = new ArrayList<>();
        
        screenshotIndex = 0;
                
        // depending on the types, assign different screen shots
        switch(type)
        {
            case "welcome":
                helpScreenshots.add(new Image(getClass().getResourceAsStream("screenShots/firstHelpScreen.png")));
                break;
            
            case "map":
                
                helpScreenshots.add(new Image(getClass().getResourceAsStream("screenShots/mapHelp1.jpg")));
                helpScreenshots.add(new Image(getClass().getResourceAsStream("screenShots/mapHelp2.jpg")));
                helpScreenshots.add(new Image(getClass().getResourceAsStream("screenShots/mapHelp3.jpg")));
                break;
                
            case "statistic":
                helpScreenshots.add(new Image(getClass().getResourceAsStream("screenShots/statsHelp1.jpg")));
                break;
        }
        
        setSkipButtons();
        changeScreenshots(0);
        
    }
    
    /**
     * Returns the help pane
     */
    public Pane getPane(){
        return (Pane) this.pane;
    }

    /**
     * When the previous button is pressed, go to previous screenshot
     */
    private void previousButtonPressed(ActionEvent evt)
    {
        changeScreenshots(-1);
        setSkipButtons();
    }
    
    /**
     * When the next button is pressed, go to next screenshot
     */
    private void nextButtonPressed(ActionEvent evt)
    {
        changeScreenshots(+1);
        setSkipButtons();
    }
    
    /**
     * Change screenshots depending on the shift (e.g shift = 1 go to the next screenshot)
     */
    private void changeScreenshots(int shift){
        
        if ( helpScreenshots.size() == 0) return;
        
        screenshotIndex += shift;
        ImageView currentScreenshot = new ImageView(helpScreenshots.get(screenshotIndex));
        
        pane.setCenter( currentScreenshot );
        
        currentScreenshot.setFitWidth(1080);
        currentScreenshot.setFitHeight(600);
    }
    
    /**
     * Sets up the buttons to go to the next screenshot
     */
    private void setSkipButtons()
    {
        BorderPane buttonPane = new BorderPane();
        
        Button nextButton = new Button ("Next");
        Button previousButton = new Button("Previous");
                
        Image imageForward = new Image(getClass().getResourceAsStream("graphics/right_arrow.png"));
        nextButton.setGraphic(new ImageView(imageForward));
        
        Image imageBackward = new Image(getClass().getResourceAsStream("graphics/left_arrow.png"));
        previousButton.setGraphic(new ImageView(imageBackward));
        
        nextButton.setOnAction(this::nextButtonPressed);
        previousButton.setOnAction(this::previousButtonPressed);
        
        //if statement
        if (screenshotIndex != 0) buttonPane.setLeft(previousButton);
        if (screenshotIndex != helpScreenshots.size() - 1) buttonPane.setRight(nextButton);
        
        pane.setTop(buttonPane);
    }
}
    

