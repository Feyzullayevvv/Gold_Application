package com.example.qizil.Controllers;

import com.example.qizil.Data.DataSource;
import com.example.qizil.Model.Customer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class satisDialog implements Initializable {

    private Customer customer;

    @FXML
    private ChoiceBox<String> choicebox;

    @FXML
    private TextField customerName;

    @FXML
    private TextField gram;

    @FXML
    private TextField borc_585;

    @FXML
    private TextField borc_750;





    private DataSource ds;


    private String[] eyar = {"585", "750"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ds = new DataSource();
        choicebox.getItems().addAll(eyar);

    }

    public void showContact(Customer customer) {
        this.customer = customer;
        customerName.setText(customer.getName());
        borc_585.setText(String.valueOf(customer.getBorc_585()));
        borc_585.setEditable(false);
        borc_750.setText(String.valueOf(customer.getBorc_750()));
        borc_750.setEditable(false);

    }

    public void insertSatis(Customer customer){
        try {
            ds.open();
            ds.insertSatis(customer.getName(), choicebox.getValue(), Double.parseDouble(gram.getText()));
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
