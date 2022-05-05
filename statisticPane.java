import javafx.scene.layout.*; 
import javafx.scene.control.*; 
import java.util.ArrayList;
import javafx.scene.control.Alert;
import javafx.event.*;
import javafx.event.EventHandler;
import java.text.ParseException;
import javafx.collections.ObservableList;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;


/**
 * Represent one statistic pane that displays the statistics within a price range.
 *
 * @author Chin Wan k21016106
 * @version 30/02/2022
 */
public class StatisticPane
{
    private BorderPane pane;
    private Integer index;
    private StatisticGraph graph;
    private ArrayList<Label> allStats;
    private ArrayList<Label> allStatsTitles;
    private ArrayList<Integer> statsIndices;
    private Button graphButton;

    /**
     * Constructor for objects of class statisticPane
     */
    public StatisticPane(int index, ArrayList<Label> allStats, ArrayList<Label> allStatsTitles, ArrayList<Integer> statsIndices, DataStatisticCalculator stats)
    {
        pane = new BorderPane();
        graph = new StatisticGraph(stats);
        graphButton = new Button();
        this.index = (Integer) index;
        this.allStats = allStats;
        this.allStatsTitles = allStatsTitles;
        this.statsIndices = statsIndices;
        
        
        // Style the statistic border pane.
        pane.setPrefSize(530, 350);
        pane.getStyleClass().add("statisticPane");
        
        Insets inset = new Insets(10);
        
        // Style the statistics and the titles.
        pane.setCenter(allStats.get(index));
        allStats.get(index).getStyleClass().add("statisticLabel");
        pane.setMargin(allStats.get(index), inset);
        
        pane.setTop(allStatsTitles.get(index));
        allStatsTitles.get(index).getStyleClass().add("statisticTitle");
        pane.setAlignment(allStatsTitles.get(index), Pos.TOP_CENTER);
        pane.setMargin(allStatsTitles.get(index), inset);
        
        setButtons();
    }
    
    /**
     * Return the border pane of this object.
     * @return The border pane
     */
    public BorderPane getBorderPane() 
    {
        return pane;
    }
    
    /**
     * set the buttons for the pane.
     */
    private void setButtons() {
        Button leftButton = new Button();
        Button rightButton = new Button();
        Button graphButton = new Button();
        
        // Styling the buttons
        leftButton.getStyleClass().add("statisticButton");
        leftButton.getStyleClass().add("centerNode");
        pane.setAlignment(leftButton, Pos.CENTER_LEFT);
        Image imageBackward = new Image(getClass().getResourceAsStream("graphics/left_arrow.png"));
        leftButton.setGraphic(new ImageView(imageBackward));
        
        rightButton.getStyleClass().add("statisticButton");
        rightButton.getStyleClass().add("centerNode");
        pane.setAlignment(rightButton, Pos.CENTER_RIGHT);
        Image imageForward = new Image(getClass().getResourceAsStream("graphics/right_arrow.png"));
        rightButton.setGraphic(new ImageView(imageForward));
        
        graphButton.getStyleClass().add("graphButton");
        graphButton.getStyleClass().add("centerNode");
        graphButton.setText("graph");
        
        pane.setLeft(leftButton);
        pane.setRight(rightButton);
        pane.setBottom(graphButton);
        pane.setAlignment(graphButton, Pos.BOTTOM_RIGHT);
        
        
        leftButton.setOnAction(this::clickLeft);
        rightButton.setOnAction(this::clickRight);
        graphButton.setOnAction(this::checkGraph);
    }
    
    /**
     * The action after the right button is clicked, to switch statistics displayed.
     * @param event An action that is implemented on a GUI object.
     */
    private void clickRight(ActionEvent event)
    {
        int i = statsIndices.indexOf(index);
        
        while (!isIndexValid(index)){
            index = (index + 1) % allStats.size();
        }
        
        statsIndices.set(i, (Integer) index);
        
        pane.setCenter(allStats.get(index));
        pane.setTop(allStatsTitles.get(index));
        
        pane.setAlignment(allStatsTitles.get(index), Pos.TOP_CENTER);
        allStatsTitles.get(index).getStyleClass().add("statisticTitle");
        allStats.get(index).getStyleClass().add("statisticLabel");
        
    }
    
    /**
     * The action after the left button is clicked, to switch statistics displayed.
     * @param event An action that is implemented on a GUI object.
     */
    private void clickLeft(ActionEvent event)
    {
        int i = statsIndices.indexOf(index);
        
        while (!isIndexValid(index)){
            index = (index - 1 + allStats.size()) % allStats.size();
        }
        
        statsIndices.set(i, (Integer) index);
        
        pane.setCenter(allStats.get(index));
        pane.setTop(allStatsTitles.get(index));
        pane.setAlignment(allStatsTitles.get(index), Pos.TOP_CENTER);
        allStatsTitles.get(index).getStyleClass().add("statisticTitle");
        allStats.get(index).getStyleClass().add("statisticLabel");
    }
    
    /**
     * Check if the index is used already.
     * @param index The index for getting the statistics.
     * @return boolean on whether the index is used.
     */
    public boolean isIndexValid(int index)
    {
        return !statsIndices.contains(index);
    }
    
    /**
     * Check which graph is displayed.
     * @param event An action that is implemented on a GUI object.
     */
    private void checkGraph(ActionEvent event)
    {
        switch (index) {
            case (0):
                graph.emptyWindow();
                break;
            case (1):
                graph.emptyWindow();
                break;
            case (2):
                graph.roomTypesCountPerBorough();
                break;
            case (3):
                graph.averagePricePerBorough();
                break;
            case (4):
                graph.emptyWindow();
                break;
            case (5):
                try {
                    graph.reviewsPerYear();
                    break;
                }
                catch (ParseException e) {
                    graph.emptyWindow();
                    break;
                }
            case (6):
                graph.emptyWindow();
                break;
            case (7):
                graph.mostReviewedHosts();
                break;
        }
    }
}
