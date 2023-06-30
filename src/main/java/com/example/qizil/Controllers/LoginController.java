package com.example.qizil.Controllers;

import com.example.qizil.Data.DataSource;
import com.example.qizil.Model.Admin;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class LoginController {
    @FXML
    private TextField userName;

    @FXML
    private PasswordField passWord;


    @FXML
    private Label wronguserinput;

    private List<Admin> admins;

    private DataSource ds;


    @FXML
    public void handleExit(){
        System.exit(0);
    }

    public boolean login(){
        ds= new DataSource();
        ds.open();
        admins=ds.queryUsers();
        String username= userName.getText().trim();
        String password= passWord.getText().trim();
        for (Admin admin : admins){
            if (username.equals(admin.getUserName()) && password.equals(admin.getPassword())){
                Stage loginStage = (Stage) userName.getScene().getWindow();
                loginStage.close();
                Controller controller = new Controller();
                controller.showLoggedInUI();
                ds.close();
                return true;
            }

        }
        applyShakeAnimation(passWord);
        ds.close();
        return false;


    }
    private void applyShakeAnimation(PasswordField field) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(field.translateXProperty(), 0)),
                new KeyFrame(Duration.millis(100), new KeyValue(field.translateXProperty(), -10)),
                new KeyFrame(Duration.millis(200), new KeyValue(field.translateXProperty(), 10)),
                new KeyFrame(Duration.millis(300), new KeyValue(field.translateXProperty(), -10)),
                new KeyFrame(Duration.millis(400), new KeyValue(field.translateXProperty(), 10)),
                new KeyFrame(Duration.millis(500), new KeyValue(field.translateXProperty(), 0))
        );
        timeline.play();
        timeline.setOnFinished(event -> field.getStyleClass().remove("shake"));
        field.getStyleClass().add("shake");
    }
}
