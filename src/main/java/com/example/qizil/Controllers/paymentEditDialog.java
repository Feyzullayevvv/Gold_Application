package com.example.qizil.Controllers;

import com.example.qizil.Data.DataSource;
import com.example.qizil.Model.Payment;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class paymentEditDialog implements Initializable {

    private Payment payment;

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

    public void showSale(Payment odenish) {
        this.payment = odenish;
        customerName.setText(odenish.getCustomerName());
        gram.setText(String.valueOf(odenish.getGram()));
    }

    public void editPayment(Payment payment){
        try {
            ds.open();
            ds.deletePayment(payment.getSystemNr());
            ds.increaseCustomerDebt(payment.getCustomerName(), payment.getEyar(), payment.getGram());
            ds.insertPayment(payment.getCustomerName(), choicebox.getValue(), Double.parseDouble(gram.getText()));
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
