package UI;

import Client.Client;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;



public class Rect extends Rectangle {

    private Label label;
    private boolean needsCard;
    private Button button;

    public Rect(float width, float height, Label label, Button button, Client client){
        super();
        //Setting the properties of the rectangle
        this.setWidth(width);
        this.setHeight(height);

        //Setting the height and width of the arc
        this.setArcWidth(30.0);
        this.setArcHeight(20.0);

        //Setting text label.
        this.label = label;
        this.needsCard = true;

        //Button for submission
        this.button = button;
        this.button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                client.submitCard(label.getText(), label);
            }
        });

    }

    public String clearCard(){
        this.needsCard = true;
        return label.getText();
    }

    public void setCard(String s){
        this.needsCard = false;
        this.label.setText(s);
    }

    public boolean needsCard(){
        return needsCard;
    }

}
