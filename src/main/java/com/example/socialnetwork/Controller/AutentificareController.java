package com.example.socialnetwork.Controller;

import com.example.socialnetwork.Domain.User;
import com.example.socialnetwork.HelloApplication;
import com.example.socialnetwork.Service.NetworkService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class AutentificareController {
    private NetworkService service;
    public AutentificareController(){}
    public void init_Controller(NetworkService service){this.service = service;}

    public void setService(NetworkService service) {
        this.service = service;
    }

    @FXML
    public TextField codTextField;
    @FXML
    public Label error_signin;
    //void signInButton(ActionEvent event)
    @FXML
    public Button signInButton;

    public void signInButton() {
        if(codTextField.getText().isEmpty() == false){
            if(service.findUserWithCod(codTextField.getText())){
                URL url = HelloApplication.class.getResource("UserPage.fxml");
                FXMLLoader loader = new FXMLLoader(url);
                AnchorPane root = null;
                try{
                    root = loader.load();
                }catch (IOException e){
                    e.printStackTrace();
                }
                UserPageController userPageController = loader.getController();
                try {
                    User user = service.getUserWithCode(codTextField.getText());
                    userPageController.init_Controller(service, this, user);
                }catch (SQLException e){
                    e.printStackTrace();
                }
                Stage primaryStage = new Stage();
                primaryStage.setScene(new Scene(root, 720.0, 400.0));
                primaryStage.show();
                Stage thisStage = (Stage) signInButton.getScene().getWindow();
                thisStage.close();
                error_signin.setText("Succes!");
            }
            else error_signin.setText("Cod incorect!");
        }
        else error_signin.setText("Introduceti un cod!");

    }
}
