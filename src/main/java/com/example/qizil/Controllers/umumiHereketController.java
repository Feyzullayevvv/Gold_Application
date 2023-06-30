package com.example.qizil.Controllers;

import com.example.qizil.Data.DataSource;
import com.example.qizil.Model.Payment;
import com.example.qizil.Model.Satis;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class umumiHereketController implements Initializable {

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TextField totalPayment_585;
    @FXML
    private TextField totalPayment_750;
    @FXML
    private TextField totalSale_585;
    @FXML
    private TextField totalSale_750;


    private double totalSale585=0;
    private double totalSale750=0;
    private double totalPayment585=0;
    private double totalPayment750=0;

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    private static List<Satis> satisList= new ArrayList<>();

    private static List<Payment> paymentList = new ArrayList<>();

    private DataSource ds;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ds= new DataSource();
        initSatisList();
        initPaymentList();
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> showSalesBetweenDates());
        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> showSalesBetweenDates());
    }
    public void showSalesBetweenDates() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate != null && endDate != null) {
            try {
                Date startDateObj = dateFormatter.parse(startDate.toString());
                Date endDateObj = dateFormatter.parse(endDate.toString());

                List<Satis> filteredSales = satisList.stream()
                        .filter(satis -> {
                            try {
                                Date saleDate = dateFormatter.parse(satis.getDate());
                                return (saleDate.equals(startDateObj) || saleDate.after(startDateObj))
                                        && (saleDate.before(endDateObj) || saleDate.equals(endDateObj));
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return false;
                            }
                        })
                        .collect(Collectors.toList());
                List<Payment> filteredPayments = paymentList.stream()
                        .filter(payment -> {
                            try {
                                Date paymentDate = dateFormatter.parse(payment.getDate());
                                return (paymentDate.equals(startDateObj) || paymentDate.after(startDateObj))
                                        && (paymentDate.before(endDateObj) || paymentDate.equals(endDateObj));
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return false;
                            }
                        })
                        .collect(Collectors.toList());
                totalSale585 = 0;
                totalSale750 = 0;
                totalPayment585 = 0;
                totalPayment750 = 0;

                for (Payment payment : filteredPayments) {
                    double amount = payment.getGram();
                    if (payment.getEyar().equals("585")) {
                        totalPayment585 += amount;
                    } else if (payment.getEyar().equals("750")) {
                        totalPayment750 += amount;
                    }
                }

                for (Satis sale : filteredSales) {
                    double gram = sale.getGram();
                    if (sale.getEyar().equals("585")) {
                        totalSale585 += gram;
                    } else if (sale.getEyar().equals("750")) {
                        totalSale750 += gram;
                    }
                }

                totalSale_585.setText(String.format("%.2f", totalSale585));
                totalSale_750.setText(String.format("%.2f", totalSale750));
                totalPayment_585.setText(String.format("%.2f", totalPayment585));
                totalPayment_750.setText(String.format("%.2f", totalPayment750));


            } catch (Exception e){
                System.out.println(e.getMessage());
                Alert  alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Xəta");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }
    public void initSatisList(){
        try {
            ds.open();
            satisList.clear();
            satisList = ds.queryAllSales();
            ds.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }
    public void initPaymentList(){
        try {
            ds.open();
            paymentList.clear();
            paymentList = ds.queryAllPayments();
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
