package com.example.qizil.Controllers;

import com.example.qizil.Data.DataSource;
import com.example.qizil.Model.Customer;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class musteriController implements Initializable {

    @FXML
    private AnchorPane musterilerPanel;
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

    @FXML
    private TextField total585borc;
    @FXML
    private TextField total750borc;


    private double total585=0;
    private double total750=0;

    private DataSource ds;
    private AnchorPane Pane;

    private static ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
    private static List<Customer> customers=new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ds = new DataSource();

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        customer585_debt.setCellValueFactory(new PropertyValueFactory<>("borc_585"));
        customer750_debt.setCellValueFactory(new PropertyValueFactory<>("borc_750"));
        initCustomerList();
        total585borc.setText(String.valueOf(total585));
        total750borc.setText(String.valueOf(total750));
        customerTable.setItems(customerObservableList);

    }
    @FXML
    public void handlePrintButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Müştərilər Data");

                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Name");
                headerRow.createCell(1).setCellValue("Debt 585");
                headerRow.createCell(2).setCellValue("Debt 750");

                int rowIndex = 1;
                for (Customer customer : customerObservableList) {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(customer.getName());
                    row.createCell(1).setCellValue(customer.getBorc_585());
                    row.createCell(2).setCellValue(customer.getBorc_750());
                }

                for (int i = 0; i < 3; i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Print");
                alert.setHeaderText(null);
                alert.setContentText("Table data printed to Excel successfully.");
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while printing table data to Excel.");
                alert.showAndWait();
            }
        }
    }



    public void initCustomerList(){
        ds.open();
        customers.clear();
        customerObservableList.clear();
        customers=ds.queryAllCustomers();
        for (Customer customer:customers){
            total585+=customer.getBorc_585();
            total750+=customer.getBorc_750();
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
                customerInfo customerInfo = new customerInfo();
                customerInfo.setCustomer(selectedCustomer);
                createCustomerInfo(customerInfo);
            }
        }

    }

    public void createNewCustomer(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(musterilerPanel.getScene().getWindow());
        dialog.setTitle("Yeni Müştəri");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/example/qizil/yeniMusteri.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        newCustomerController controller = fxmlLoader.getController();

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        controller.getCustomerName().textProperty().addListener((observable, oldValue, newValue) -> {
            updateOkButtonDisableProperty(okButton,controller.getCustomerName().getText(),controller.getCustomer585Borc().getText(), controller.getCustomer750Borc().getText());
        });
        controller.getCustomer585Borc().textProperty().addListener((observable, oldValue, newValue) -> {
            updateOkButtonDisableProperty(okButton,controller.getCustomerName().getText(),controller.getCustomer585Borc().getText(), controller.getCustomer750Borc().getText());
        });
        controller.getCustomer750Borc().textProperty().addListener((observable, oldValue, newValue) -> {
            updateOkButtonDisableProperty(okButton,controller.getCustomerName().getText(),controller.getCustomer585Borc().getText(), controller.getCustomer750Borc().getText());
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.insertCustomer();
            initCustomerList();
        }
    }

    private void updateOkButtonDisableProperty(Button okButton, String customerName, String borc585,String borc750) {
        boolean disableButton = customerName.isEmpty() || borc585.isEmpty() || borc750.isEmpty();
        okButton.setDisable(disableButton);
    }


    public void createCustomerInfo(customerInfo controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/qizil/customerInfo.fxml"));
            loader.setController(controller);
            Parent root = loader.load();
            setNode(root);
        } catch (Exception e){
            System.out.println(e.getMessage());
            Alert  alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    private void setNode(Node node) {
        musterilerPanel.getChildren().clear();
        musterilerPanel.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }


}
