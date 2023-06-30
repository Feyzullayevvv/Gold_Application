package com.example.qizil.Controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;

public class Controller {
    @FXML
    private AnchorPane holdPane;

    @FXML
    private Label adminName;

    private AnchorPane Pane;

    public void showLoggedInUI() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/qizil/main.fxml"));
            Scene scene= new Scene(fxmlLoader.load(),1000,800);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Qızıl");
            stage.setScene(scene);
            stage.show();


        } catch (Exception e){
            System.out.println(e.getMessage());
            Alert  alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    private void setNode(Node node) {
        holdPane.getChildren().clear();
        holdPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }

    public void handleExit(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Çıxmağa Əminsiniz?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get()==ButtonType.OK){
            System.exit(0);
        }
    }

    public void createOdenish(){
        try {
            Pane= FXMLLoader.load(getClass().getResource("/com/example/qizil/odenish.fxml"));
            setNode(Pane);
        } catch (Exception e){
            System.out.println(e.getMessage());
            Alert  alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    public void createSatis(){
        try {
            Pane= FXMLLoader.load(getClass().getResource("/com/example/qizil/satis.fxml"));
            setNode(Pane);
        } catch (Exception e){
            System.out.println(e.getMessage());
            Alert  alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void createMusteriler(){
        try {
            Pane= FXMLLoader.load(getClass().getResource("/com/example/qizil/musteriler.fxml"));
            setNode(Pane);
        } catch (Exception e){
            System.out.println(e.getMessage());
            Alert  alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    public void createUmumiHereket() {
        try {
            Pane = FXMLLoader.load(getClass().getResource("/com/example/qizil/umumiHereket.fxml"));
            setNode(Pane);
        }catch (Exception e){
            System.out.println(e.getMessage());
            Alert  alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void createAnbar() {
        try {
            Pane = FXMLLoader.load(getClass().getResource("/com/example/qizil/anbar.fxml"));
            setNode(Pane);
        } catch (Exception e){
            System.out.println(e.getMessage());
            Alert  alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }



}
