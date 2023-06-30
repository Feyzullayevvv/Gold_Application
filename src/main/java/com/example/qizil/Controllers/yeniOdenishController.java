package com.example.qizil.Controllers;

import com.example.qizil.Data.DataSource;
import com.example.qizil.Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class yeniOdenishController implements Initializable {

    @FXML
    private AnchorPane odenisPanel;
    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer,String> name;

    @FXML
    private TableColumn<Customer,Double> customer585_debt;

    @FXML
    private TableColumn<Customer,Double> customer750_debt;

    @FXML
    private TextField search;

    private DataSource ds;

    private static ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
    private static List<Customer> customers=new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ds = new DataSource();

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        customer585_debt.setCellValueFactory(new PropertyValueFactory<>("borc_585"));
        customer750_debt.setCellValueFactory(new PropertyValueFactory<>("borc_750"));
        initCustomerList();
        customerTable.setItems(customerObservableList);

    }


    public void initCustomerList(){
        ds.open();
        customers.clear();
        customerObservableList.clear();
        customers=ds.queryAllCustomers();
        for (Customer customer:customers){
            customerObservableList.add(customer);
        }
        ds.close();

    }

    private void Search(ObservableList<Customer> customer, String name) {
        customer.clear();
        String lowercaseName = name.toLowerCase();
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getName().toLowerCase().contains(lowercaseName)) {
                customer.add(customers.get(i));
            }
        }
    }


    public void handleKeyReleased(KeyEvent event){
        if (event.getEventType() == KeyEvent.KEY_RELEASED){
            String name = search.getText();

            Search(customerObservableList,name);
        }
    }

    public void handleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (customerTable.getSelectionModel().getSelectedItem() != null) {
                Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initOwner(odenisPanel.getScene().getWindow());
                dialog.setTitle("Edit Contact");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/example/qizil/customerPayment.fxml"));
                try {
                    dialog.getDialogPane().setContent(fxmlLoader.load());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                dialog.initStyle(StageStyle.UNDECORATED);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
                odenishDialog controller = fxmlLoader.getController();
                controller.showContact(selectedCustomer);

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
                    controller.insertPayment(selectedCustomer);
                    refreshCustomerList();


                }


                }
        }
    }


    private void updateOkButtonDisableProperty(Button okButton, String choiceBoxValue, String gramValue) {
        boolean disableButton = choiceBoxValue == null || gramValue.isEmpty();
        okButton.setDisable(disableButton);
    }
    public void refreshCustomerList() {
        initCustomerList();
        customerTable.setItems(customerObservableList);
    }



}
