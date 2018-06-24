package UI;


import javafx.scene.control.ChoiceDialog;

import java.util.List;
import java.util.Optional;

public class ChooseQuestion extends ChoiceDialog<String> {


    public ChooseQuestion(List<String> choices){
        super(choices.get(0), choices);
        this.setTitle("Select Response");
        this.setHeaderText("Here be the judge");
        this.setContentText("Choose the winning card");
    }

    public String queryQuestion(){
        Optional<String> results = this.showAndWait();
        while(!results.isPresent()){
            results = this.showAndWait();
        }

        return results.get();
    }



}
