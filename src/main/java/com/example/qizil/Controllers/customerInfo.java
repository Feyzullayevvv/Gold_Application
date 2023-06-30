package com.example.qizil.Controllers;

import com.example.qizil.Data.DataSource;
import com.example.qizil.Model.Customer;
import com.example.qizil.Model.Payment;
import com.example.qizil.Model.Satis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class customerInfo implements Initializable {
    @FXML
    private TableView<Payment> odenisTable;
    @FXML
    private TableColumn<Payment,String> odenisDate;

    @FXML
    private TableColumn<Payment,String>  odenisCustomer;
    @FXML
    private TableColumn<Payment,String>  odenisEyar;
    @FXML
    private TableColumn<Payment,String>  odenisQram;

    @FXML
    private TableView<Satis> satisTable;
    @FXML
    private TableColumn<Satis,String>  satisData;

    @FXML
    private TableColumn<Satis,String>  satisCustomer;
    @FXML
    private TableColumn<Satis,String>  satisEyar;
    @FXML
    private TableColumn<Satis,String>  satisQram;

    @FXML
    private TextField customerName;
    @FXML
    private TextField customer585_debt;
    @FXML
    private TextField customer750_debt;
    @FXML
    private TextField totalSale_585;
    @FXML
    private TextField totalSale_750;
    @FXML
    private TextField totalPayment_585;
    @FXML
    private TextField totalPayment_750;


    private DataSource ds;

    private Customer customer;

    private double customerTotal_585Sale=0;
    private double customerTotal_750Sale=0;
    private double customerTotal_585Payment=0;
    private double customerTotal_750Payment=0;



    private static ObservableList<Payment> odneishObservableList = FXCollections.observableArrayList();

    private static List<Payment> odenishList = new ArrayList<>();
    private static ObservableList<Satis> satisObservableList= FXCollections.observableArrayList();

    private static List<Satis> satisList= new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ds= new DataSource();
        odenisDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        odenisCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        odenisEyar.setCellValueFactory(new PropertyValueFactory<>("eyar"));
        odenisQram.setCellValueFactory(new PropertyValueFactory<>("gram"));
        initPaymentList();
        odenisTable.setItems(odneishObservableList);
        satisData.setCellValueFactory(new PropertyValueFactory<>("date"));
        satisCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        satisEyar.setCellValueFactory(new PropertyValueFactory<>("eyar"));
        satisQram.setCellValueFactory(new PropertyValueFactory<>("gram"));
        initSatisList();
        satisTable.setItems(satisObservableList);
        customerName.setText(customer.getName());
        customer585_debt.setText(String.valueOf(customer.getBorc_585()));
        customer750_debt.setText(String.valueOf(customer.getBorc_750()));
        totalSale_585.setText(String.format("%.2f", customerTotal_585Sale));
        totalSale_750.setText(String.format("%.2f", customerTotal_750Sale));
        totalPayment_585.setText(String.format("%.2f", customerTotal_585Payment));
        totalPayment_750.setText(String.format("%.2f", customerTotal_750Payment));

    }

    public void initSatisList(){
        try {
            ds.open();
            satisList.clear();
            satisObservableList.clear();
            satisList = ds.getSatisCustomer(customer.getName());
            for (Satis satis : satisList) {
                if (satis.getEyar().equals("585")) {
                    customerTotal_585Sale += satis.getGram();
                } else if (satis.getEyar().equals("750")) {
                    customerTotal_750Sale += satis.getGram();
                }
                satisObservableList.add(satis);
            }
            ds.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
            Alert  alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }



    public void initPaymentList(){
        try {
            ds.open();
            odenishList.clear();
            odneishObservableList.clear();
            odenishList = ds.getPaymentCustomer(customer.getName());
            for (Payment payent : odenishList) {
                if (payent.getEyar().equals("585")) {
                    customerTotal_585Payment += payent.getGram();
                } else if (payent.getEyar().equals("750")) {
                    customerTotal_750Payment += payent.getGram();
                }
                odneishObservableList.add(payent);
            }
            ds.close();
        }catch (Exception e){
                System.out.println(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Xəta");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
    }

    public void setCustomer(Customer customer){
        this.customer=customer;
    }

}
