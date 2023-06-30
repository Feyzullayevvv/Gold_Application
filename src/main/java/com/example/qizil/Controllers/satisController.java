package com.example.qizil.Controllers;

import com.example.qizil.Data.DataSource;
import com.example.qizil.Model.Customer;
import com.example.qizil.Model.Satis;
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

public class satisController implements Initializable {

    @FXML
    private AnchorPane satisPane;

    @FXML
    private TableView<Satis> saleTable;
    @FXML
    private TableColumn<Satis,String>  date;

    @FXML
    private TableColumn<Satis,String>  customer;
    @FXML
    private TableColumn<Satis,String>  eyar;
    @FXML
    private TableColumn<Satis,String>  qram;

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
    private static ObservableList<Satis> satisObservableList= FXCollections.observableArrayList();

    private static List<Satis> satisList= new ArrayList<>();
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ds= new DataSource();
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        customer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        eyar.setCellValueFactory(new PropertyValueFactory<>("eyar"));
        qram.setCellValueFactory(new PropertyValueFactory<>("gram"));
        initSatisList();
        saleTable.setItems(satisObservableList);
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> showSalesBetweenDates());
        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> showSalesBetweenDates());
        toggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            showTodaysSales(newValue);
        });
    }

    public void initSatisList(){
        ds.open();
        satisList.clear();
        satisObservableList.clear();
        satisList=ds.queryAllSales();
        for (Satis satis : satisList){
            satisObservableList.add(satis);
        }
        ds.close();
    }


    public void createNewSatis(){
        try {
            Pane= FXMLLoader.load(getClass().getResource("/com/example/qizil/yeniSatis.fxml"));
            setNode(Pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

                satisObservableList.clear();

                satisObservableList.addAll(filteredSales);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    private void setNode(Node node) {
        satisPane.getChildren().clear();
        satisPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent){
        Satis selectedItem = saleTable.getSelectionModel().getSelectedItem();
        if (selectedItem!=null){
            if (keyEvent.getCode().equals(KeyCode.BACK_SPACE)){
                deleteItem(selectedItem);
            }
        }
    }

    public void  deleteItem(Satis satis){
        ds.open();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Satışı silmək");
        alert.setHeaderText("Müştəri adı: " + satis.getCustomerName() + "\n Əyar: " + satis.getEyar() +"\n Qram:" + satis.getGram());
        alert.setContentText("Əminsiniz?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get()==ButtonType.OK)){
            ds.deleteSale(satis.getSystemNr());
            ds.decreaseCustomerDebt(satis.getCustomerName(),satis.getEyar(),satis.getGram());
            ds.increaseAnbar(satis.getEyar(),satis.getGram());
            ds.close();
            initSatisList();
            return;
        }
        ds.close();
    }

    public void handleKeyReleased(KeyEvent event){
        if (event.getEventType() == KeyEvent.KEY_RELEASED){
            String name = search.getText();
            Search(satisObservableList,name);
        }
    }
    private void Search(ObservableList<Satis> satisObservableList, String name) {
        satisObservableList.clear();
        String lowercaseName = name.toLowerCase();
        for (int i = 0; i < satisList.size(); i++) {
            if (satisList.get(i).getCustomerName().toLowerCase().contains(lowercaseName)) {
                satisObservableList.add(satisList.get(i));
            }
        }
    }
    public void showTodaysSales(boolean showTodaySales) {
        LocalDate today = LocalDate.now();

        satisObservableList.clear();

        if (showTodaySales) {
            List<Satis> filteredSales = satisList.stream()
                    .filter(satis -> {
                        try {
                            Date saleDate = dateFormatter.parse(satis.getDate());
                            LocalDate saleLocalDate = saleDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            return saleLocalDate.isEqual(today);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return false;
                        }
                    })
                    .collect(Collectors.toList());

            satisObservableList.addAll(filteredSales);
        } else {
            satisObservableList.addAll(satisList);
        }
    }


    public void handleClick(MouseEvent event) {
        if (event.getClickCount()==2) {
            if (saleTable.getSelectionModel().getSelectedItem() != null) {
                Satis selectedSale = saleTable.getSelectionModel().getSelectedItem();
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initOwner(satisPane.getScene().getWindow());
                dialog.setTitle("Edit Contact");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/example/qizil/saleEditDialog.fxml"));
                try {
                    dialog.getDialogPane().setContent(fxmlLoader.load());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                dialog.initStyle(StageStyle.UNDECORATED);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
                saleEditDialog controller = fxmlLoader.getController();
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
                    controller.insertSatis(selectedSale);
                    ds.open();
                    ds.close();
                    initSatisList();
                }
            }
        }

    }
    private void updateOkButtonDisableProperty(Button okButton, String choiceBoxValue, String gramValue) {
        boolean disableButton = choiceBoxValue == null || gramValue.isEmpty();
        okButton.setDisable(disableButton);
    }
}
