import javafx.scene.layout.*; 
import javafx.scene.control.*; 
import java.util.ArrayList;
import java.text.ParseException;
import javafx.geometry.Insets;

/**
 * Create the pane to hold the statistic pane, and the content
 * of each statistic pane.
 * 
 * @author Chin Wan k21016106
 * @version 30/03/2022
 */
public class StatisticsPaneBuilder extends PaneBuilder
{
    private GridPane gridPane;                        // Pane to hold all statistic panes                
    private ArrayList<Label> allStats;                // Labels containing statistics
    private ArrayList<Label> allStatsTitles;          // Lables containing statistics' title
    private ArrayList<Integer> indices;               // The indices of statistics label that are currently displayed
    private DataStatisticCalculator stats;            // statistics after filtered by price range
    private DataStatisticCalculator statsWithAllData; // statistics with all property listings
    private ArrayList<BorderPane> statisticsPane;     // Panes to hold the statistics
    
    public StatisticsPaneBuilder( ArrayList<AirbnbListing> data, ArrayList<Station> stations ) {
        buildHelpStatisticPane();
        
        allStats = new ArrayList<>();
        allStatsTitles = new ArrayList<>();
        statsWithAllData = new DataStatisticCalculator(data, stations);

        gridPane = new GridPane();
        // Setting the size of gridpane.
        gridPane.setPrefSize(1000, 600);
        gridPane.getColumnConstraints().add(new ColumnConstraints(500)); 
        gridPane.getColumnConstraints().add(new ColumnConstraints(500)); 
        gridPane.setHgap(50);
        gridPane.setVgap(35);
        gridPane.getStyleClass().add("gridPane");
        
        indices = new ArrayList<>();
        statisticsPane = new ArrayList<>();
        // Adding the indices that is currently displayed on statistic panes.
        indices.add(0);
        indices.add(1);
        indices.add(2);
        indices.add(3);
        
    }
    
    /**
     * Return the pane that holds all statistic panes.
     * @return Pane that holds all statistic panes.
     */
    public Pane getPane(){
        return (Pane) this.gridPane;
    }
    
    /**
     * Create the statistic panes and store into the grid pane.
     */
    private void createStatisticBoxes() {
        addStats();
        addStatsTitle();
        
        StatisticPane statistic1 = new StatisticPane(0, allStats, allStatsTitles, indices, stats);
        StatisticPane statistic2 = new StatisticPane(1, allStats, allStatsTitles, indices, stats);
        StatisticPane statistic3 = new StatisticPane(2, allStats, allStatsTitles, indices, stats);
        StatisticPane statistic4 = new StatisticPane(3, allStats, allStatsTitles, indices, stats);
        
        if (statisticsPane.size() == 0){
            statisticsPane.add(statistic1.getBorderPane());
            statisticsPane.add(statistic2.getBorderPane());
            statisticsPane.add(statistic3.getBorderPane());
            statisticsPane.add(statistic4.getBorderPane());
        }
        
        gridPane.getChildren().addAll( statistic1.getBorderPane(), statistic2.getBorderPane(), statistic3.getBorderPane(), statistic4.getBorderPane());
        
        gridPane.setConstraints(statistic1.getBorderPane(), 0, 0);
        gridPane.setConstraints(statistic2.getBorderPane(), 1, 0);
        gridPane.setConstraints(statistic3.getBorderPane(), 0, 1);
        gridPane.setConstraints(statistic4.getBorderPane(), 1, 1);
        
        // Styling the statistic panes
        Insets inset = new Insets(10);
        gridPane.setMargin(statistic1.getBorderPane(), inset);
        gridPane.setMargin(statistic2.getBorderPane(), inset);
        gridPane.setMargin(statistic3.getBorderPane(), inset);
        gridPane.setMargin(statistic4.getBorderPane(), inset);
        
        
    }
    
    /**
     * To call the method to create statistic panes.
     */
    public void actionOnChange(){
        createStatisticBoxes();
    }

    /**
     * Create labels for the statistics.
     */
    private void addStats() {
        
        ArrayList<AirbnbListing> list = new ArrayList<>();
        // Filter the listings with the price range chosen.
        list = statsWithAllData.getListingsInPriceRange(fromPriceChosen, toPriceChosen);
        stats = new DataStatisticCalculator(list, statsWithAllData.getStationData());
        
        stats.loadStatistics();
        
        Label label1 = new Label("" + stats.getAverageReview());
        Label label2 = new Label("" + stats.getTotalNumberOfListings());
        Label label3 = new Label("" + stats.getTotalNumberOfEntireHomeApt());
        Label label4 = new Label("" + stats.getMostExpensiveBorough());
        Label label5 = new Label("" + stats.getAverageAvailabilityTime());
        Label label6 = new Label("" + stats.getAverageTimeLastReview());
        Label label7 = new Label("" + stats.getAverageDistanceToTube());
        Label label8 = new Label("" + stats.mostReviewedHost());
        
        if (allStats.size() == 0){
            allStats.add(label1);
            allStats.add(label2);
            allStats.add(label3);
            allStats.add(label4);
            allStats.add(label5);
            allStats.add(label6);
            allStats.add(label7);
            allStats.add(label8);
        }
    }
    
    /**
     * Create the labels for the statistics' titles.
     */
    private void addStatsTitle() {
        Label label1 = new Label("Average reviews");
        Label label2 = new Label("# of properties");
        Label label3 = new Label("# of entire home/apt");
        Label label4 = new Label("Most expensive borough");
        Label label5 = new Label("Average availability time");
        Label label6 = new Label("Average time since last review");
        Label label7 = new Label("Average distance to tube");
        Label label8 = new Label("Most reviewed host");
        if (allStatsTitles.size() == 0){
            allStatsTitles.add(label1);
            allStatsTitles.add(label2);
            allStatsTitles.add(label3);
            allStatsTitles.add(label4);
            allStatsTitles.add(label5);
            allStatsTitles.add(label6);
            allStatsTitles.add(label7);
            allStatsTitles.add(label8);
        }
    }

    
    private void buildHelpStatisticPane()
    {
        String type = "statistic";
        HelpScreen hs = new HelpScreen(type);
        helpPane = hs.getPane();
        //((BorderPane) helpPane).setCenter(new Label("help map")); delete
    }
}
