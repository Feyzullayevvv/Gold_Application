package com.example.qizil.Controllers;

import com.example.qizil.Data.DataSource;
import com.example.qizil.Model.Customer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class anbarDialog implements Initializable {

    @FXML
    private ChoiceBox<String> choicebox;

    @FXML
    private TextField gram;

    private DataSource ds;
    private String[] eyar = {"585", "750"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ds = new DataSource();
        choicebox.getItems().addAll(eyar);
    }
    public void insertAnbar() {
        try {
            ds.open();
            ds.increaseAnbar(choicebox.getValue(), Double.parseDouble(gram.getText()));
            ds.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
            Alert  alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void decreaseAnbar() {
        try {
            ds.open();
            ds.decreaseAnbar(choicebox.getValue(), Double.parseDouble(gram.getText()));
            ds.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public ChoiceBox<String> getChoicebox(){
        return this.choicebox;
    }
    public TextField getGram(){
        return this.gram;
    }
}
