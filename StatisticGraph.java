import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.util.List;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.layout.*; 
import javafx.scene.control.*;
import java.awt.event.ActionEvent;


/**
 * The displays of different graphs for different statistics. 
 *
 * @author Chin Wan k21016106
 * @version 30/03/2022
 */
public class StatisticGraph
{
    private DataStatisticCalculator stats;            // statistics after filtered by price range
    /**
     * Constructor for objects of class StatisticGraph
     */
    public StatisticGraph(DataStatisticCalculator stats)
    {
        this.stats = stats;
    }

    /**
     * Create a barchart with the average price per borough.
     */
    public void averagePricePerBorough()
    {
        Stage stage = new Stage();
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        
        final BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
        barChart.setTitle("Average Price Per Borough");
        xAxis.setLabel("Borough");
        yAxis.setLabel("Average Price");
        
        XYChart.Series series = new XYChart.Series();
        series.getData().addAll(stats.getAveragePricePerBoroughData());
        
        
        Scene scene = new Scene(barChart, 800, 800);
        barChart.getData().add(series);
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Create a linechart with the top 20 most reviewed hosts.
     */
    public void mostReviewedHosts() 
    {
        Stage stage = new Stage();
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Host");
        yAxis.setLabel("Amount of reviews");
        
        final LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);
        lineChart.setTitle("Most Reviewed Hosts");
        
        XYChart.Series series = new XYChart.Series();
        series.setName("Amount of reviews");
        List<String> hosts = new ArrayList<>(stats.listOfMostReviewedHosts().keySet());
        for (String host : hosts) {
            series.getData().add(new XYChart.Data(host, stats.listOfMostReviewedHosts().get(host)));
        }
        
        Scene scene = new Scene(lineChart, 800, 800);
        lineChart.getData().add(series);
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Create a barchart with amount of private rooms and entire home or apartment per borough.
     */
    public void roomTypesCountPerBorough()
    {
        Stage stage = new Stage();
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        
        final BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
        barChart.setTitle("Amount Of Each Room Type Per Borough");
        xAxis.setLabel("Borough");
        yAxis.setLabel("Amount of each room type");
        
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Private Room");
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Entire Home/Apartment");
        
        series1.getData().add(stats.getPrivateRoomsPerBoroughData());
        
        series2.getData().add(stats.getEntireRoomsPerBoroughData());
        
        
        Scene scene = new Scene(barChart, 800, 800);
        barChart.getData().addAll(series1, series2);
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Create a line chart for plotting number of reviews per year.
     * @throws ParseException If the string cannot be parsed in date format
     */
    public void reviewsPerYear() throws java.text.ParseException 
    {
        Stage stage = new Stage();
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Year");
        yAxis.setLabel("Amount of Reviews");
        
        final LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);
        lineChart.setTitle("Amount of Reviews Per Year");
        
        XYChart.Series series = new XYChart.Series();
        series.setName("Amount of reviews");
        List<String> years = new ArrayList<>( stats.averageReviewEachYear().keySet() );
        for (String year : years) {
            series.getData().add(new XYChart.Data(year, stats.averageReviewEachYear().get(year)));
        }
        
        Scene scene = new Scene(lineChart, 800, 800);
        lineChart.getData().add(series);
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Create a new window displaying that there is no graph for the statistics.
     */
    public void emptyWindow()
    {
        Stage stage = new Stage();
        BorderPane pane = new BorderPane();
        Label label = new Label("There is no additional graph for this statistic.");
        pane.setCenter(label);
        Scene scene = new Scene(pane, 800, 800);
        stage.setScene(scene);
        stage.show();
    }
}

