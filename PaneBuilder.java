import javafx.scene.layout.*; 

/**
 * Abstract class used as template to build other panes
 */
public abstract class PaneBuilder
{
    
    protected Pane pane;
    protected Pane helpPane;
    protected boolean isHelping = false;
    
    protected int fromPriceChosen = 0;
    protected int toPriceChosen = 0;
    
    /**
     * Returns the main pane
     */
    public Pane getPane(){
        if (isHelping){
            this.isHelping = false;
            return (Pane) this.helpPane;
        }
        
        return (Pane) this.pane;
    }
    
    /**
     * Used to toggle the is helping field
     */
    public void showHelp(){
        this.isHelping = true;
    }
    
    /**
     * Action performed when the price range is changes
     */
    public void actionOnChange(){}
    
    /**
     * Set the from price chosen field
     */
    public void setFromPriceChosen(Integer newPriceChosen){
        this.fromPriceChosen = newPriceChosen;
    }
    
    /**
     * Sets the to price chosen field
     */
    public void setToPriceChosen(Integer newPriceChosen){
        this.toPriceChosen = newPriceChosen;
    }    
}
