package com.example.qizil.Controllers;

import com.example.qizil.Data.DataSource;
import com.example.qizil.Model.Payment;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class odenishController implements Initializable {

    @FXML
    private AnchorPane odenishPane;

    @FXML
    private TableView<Payment> paymentTableView;
    @FXML
    private TableColumn<Payment,String>  date;

    @FXML
    private TableColumn<Payment,String>  customer;
    @FXML
    private TableColumn<Payment,String>  eyar;
    @FXML
    private TableColumn<Payment,String>  qram;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TextField search;
    private AnchorPane Pane;
    @FXML
    private ToggleButton toggleButton;

    private DataSource ds;
    private static ObservableList<Payment> odneishObservableList = FXCollections.observableArrayList();

    private static List<Payment> odenishList = new ArrayList<>();
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ds= new DataSource();

        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        customer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        eyar.setCellValueFactory(new PropertyValueFactory<>("eyar"));
        qram.setCellValueFactory(new PropertyValueFactory<>("gram"));
        initPaymentList();
        paymentTableView.setItems(odneishObservableList);
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> showSalesBetweenDates());
        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> showSalesBetweenDates());
        toggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            showTodaysSales(newValue);
        });
    }

    public void initPaymentList(){
        ds.open();
        odenishList.clear();
        odneishObservableList.clear();
        odenishList =ds.queryAllPayments();
        for (Payment payent : odenishList){
            odneishObservableList.add(payent);
        }
        ds.close();
    }


    public void createNewPayment(){
        try {
            Pane= FXMLLoader.load(getClass().getResource("/com/example/qizil/yeniOdenish.fxml"));
            setNode(Pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void handleKeyPressed(KeyEvent keyEvent){
        Payment selectedItem = paymentTableView.getSelectionModel().getSelectedItem();
        if (selectedItem!=null){
            if (keyEvent.getCode().equals(KeyCode.BACK_SPACE)){
                deleteItem(selectedItem);
            }
        }
    }
    public void  deleteItem(Payment payment){
        try {
            ds.open();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Ödənişi silmək");
            alert.setHeaderText("Müştəri adı: " + payment.getCustomerName() + "\n Əyar: " + payment.getEyar() + "\n Qram:" + payment.getGram());
            alert.setContentText("Əminsiniz?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                ds.deletePayment(payment.getSystemNr());
                ds.increaseCustomerDebt(payment.getCustomerName(), payment.getEyar(), payment.getGram());
                ds.close();
                initPaymentList();
                return;
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
    public void handleClick(MouseEvent event) {
        if (event.getClickCount()==2) {
            try {
                if (paymentTableView.getSelectionModel().getSelectedItem() != null) {
                    Payment selectedSale = paymentTableView.getSelectionModel().getSelectedItem();
                    Dialog<ButtonType> dialog = new Dialog<>();
                    dialog.initOwner(odenishPane.getScene().getWindow());
                    dialog.setTitle("Ödəniş ");
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/com/example/qizil/paymentEditDialog.fxml"));
                    try {
                        dialog.getDialogPane().setContent(fxmlLoader.load());
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                    dialog.initStyle(StageStyle.UNDECORATED);
                    dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
                    paymentEditDialog controller = fxmlLoader.getController();
                    controller.showSale(selectedSale);

                    Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setDisable(true);

                    controller.getChoicebox().valueProperty().addListener((observable, oldValue, newValue) -> {
                        updateOkButtonDisableProperty(okButton, controller.getChoicebox().getValue(), controller.getGram().getText());
                    });
                    controller.getGram().textProperty().addListener((observable, oldValue, newValue) -> {
                        updateOkButtonDisableProperty(okButton, controller.getChoicebox().getValue(), controller.getGram().getText());
                    });

                    Optional<ButtonType> result = dialog.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        controller.editPayment(selectedSale);
                        ds.open();
                        ds.close();
                        initPaymentList();
                    }
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
                Alert  alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Xəta");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }

    }
    private void updateOkButtonDisableProperty(Button okButton, String choiceBoxValue, String gramValue) {
        boolean disableButton = choiceBoxValue == null || gramValue.isEmpty();
        okButton.setDisable(disableButton);
    }
    public void showSalesBetweenDates() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate != null && endDate != null) {
            try {
                Date startDateObj = dateFormatter.parse(startDate.toString());
                Date endDateObj = dateFormatter.parse(endDate.toString());

                List<Payment> filteredSales = odenishList.stream()
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

                odneishObservableList.clear();

                odneishObservableList.addAll(filteredSales);
            } catch (Exception e){
                System.out.println(e.getMessage());
                Alert  alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Xəta");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }


    private void setNode(Node node) {
        odenishPane.getChildren().clear();
        odenishPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }
    public void handleKeyReleased(KeyEvent event){
        if (event.getEventType() == KeyEvent.KEY_RELEASED){
            String name = search.getText();
            Search(odneishObservableList,name);
        }
    }
    private void Search(ObservableList<Payment> satisObservableList, String name) {
        try {
            satisObservableList.clear();
            String lowercaseName = name.toLowerCase();
            for (int i = 0; i < odenishList.size(); i++) {
                if (odenishList.get(i).getCustomerName().toLowerCase().contains(lowercaseName)) {
                    satisObservableList.add(odenishList.get(i));
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            Alert  alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    public void showTodaysSales(boolean showTodaySales) {
        try {
            LocalDate today = LocalDate.now();
            odneishObservableList.clear();
            if (showTodaySales) {
                List<Payment> filteredSales = odenishList.stream()
                        .filter(payment -> {
                            try {
                                Date saleDate = dateFormatter.parse(payment.getDate());
                                LocalDate saleLocalDate = saleDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                return saleLocalDate.isEqual(today);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return false;
                            }
                        })
                        .collect(Collectors.toList());

                odneishObservableList.addAll(filteredSales);
            } else {
                odneishObservableList.addAll(odenishList);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            Alert  alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }








}
