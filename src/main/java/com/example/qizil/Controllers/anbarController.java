package com.example.qizil.Controllers;

import com.example.qizil.Data.DataSource;
import com.example.qizil.Model.Anbar;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class anbarController implements Initializable {

    @FXML
    private TextField anbar585;
    @FXML
    private TextField anbar750;

    @FXML
    private AnchorPane anbarPane;


    private DataSource ds;

    private List<Anbar> anbar;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ds= new DataSource();
        init();

    }

    public void handleAddAnbar(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(anbarPane.getScene().getWindow());
        dialog.setTitle("Mal Əlavə etmək");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/example/qizil/addToAnbar.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        anbarDialog controller = fxmlLoader.getController();
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
            controller.insertAnbar();
            init();
        }
    }
    public void handleDecreaseAnbar(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(anbarPane.getScene().getWindow());
        dialog.setTitle("Mal silmək");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/example/qizil/addToAnbar.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        anbarDialog controller = fxmlLoader.getController();
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
            controller.decreaseAnbar();
            init();
        }
    }

    private void updateOkButtonDisableProperty(Button okButton, String choiceBoxValue, String gramValue) {
        boolean disableButton = choiceBoxValue == null || gramValue.isEmpty();
        okButton.setDisable(disableButton);
    }

    private void init(){
        try {
            ds.open();
            anbar = ds.queryAnbar();
            for (Anbar anbar1 : anbar) {
                if (anbar1.getEyar().equals("585")) {
                    anbar585.setText(String.format("%.2f", anbar1.getGram()));
                } else if (anbar1.getEyar().equals("750")) {
                    anbar750.setText(String.format("%.2f", anbar1.getGram()));
                }
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
}

