import javafx.scene.layout.*; 
import javafx.scene.control.*; 

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class builds an instance of the pane used as a welcome screen
 */
public class WelcomePaneBuilder extends PaneBuilder
{
    
    public WelcomePaneBuilder(int width, int height)
    {
        pane = new BorderPane();
        
        
        Image welcomeScreen = new Image(getClass().getResourceAsStream("screenShots/welcomeScreenMain.png"));
        ImageView welcomeView = new ImageView(welcomeScreen);
        ((BorderPane) pane).setCenter(welcomeView);
        
        welcomeView.setFitWidth(width);
        welcomeView.setFitHeight(600);
                        
        buildHelpWelcomePane();
    }
    
    /**
     * Build up the help pane of the welcome screen
     */
    private void buildHelpWelcomePane()
    {
        String type = "welcome";
        HelpScreen hs = new HelpScreen(type);
        helpPane = hs.getPane();
        //((BorderPane) helpPane).setCenter(new Label("help map")); delete
    }
}
