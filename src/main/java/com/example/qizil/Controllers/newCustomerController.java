package com.example.qizil.Controllers;

import com.example.qizil.Data.DataSource;
import com.example.qizil.Model.Customer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class newCustomerController implements Initializable {
    private Customer customer;

    @FXML
    private TextField customerName;
    @FXML
    private TextField customer585Borc;
    @FXML
    private TextField customer750Borc;

    private DataSource ds;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ds= new DataSource();
    }

    public TextField getCustomerName() {
        return customerName;
    }

    public TextField getCustomer585Borc() {
        return customer585Borc;
    }

    public TextField getCustomer750Borc() {
        return customer750Borc;
    }

    public void insertCustomer(){
        try {
            ds.open();
            ds.insertCustomer(customerName.getText(),Double.parseDouble(customer585Borc.getText()),Double.parseDouble(customer750Borc.getText()));
            ds.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
            Alert  alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
