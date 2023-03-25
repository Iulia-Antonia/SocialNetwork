package com.example.socialnetwork;

import com.example.socialnetwork.Controller.AutentificareController;
import com.example.socialnetwork.Domain.Friendship;
import com.example.socialnetwork.Domain.Tuple;
import com.example.socialnetwork.Domain.User;
import com.example.socialnetwork.Domain.validators.FriendshipValidator;
import com.example.socialnetwork.Domain.validators.UserValidator;
import com.example.socialnetwork.Domain.validators.Validator;
import com.example.socialnetwork.Repository.RepoFriendshipDB;
import com.example.socialnetwork.Repository.RepoUserDB;
import com.example.socialnetwork.Repository.Repository;
import com.example.socialnetwork.Service.NetworkService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String username = "postgres";
        String pasword = "iulia2002";
        String url="jdbc:postgresql://localhost:5432/SocialNetwork";

        Validator<User> validatorU = new UserValidator();
        Validator<Friendship> validatorF = new FriendshipValidator();

        Repository<Long, User> repoUserDB = new RepoUserDB(url,username,pasword,validatorU);
        Repository<Tuple<Long,Long>, Friendship> repoFriendshipDB = new RepoFriendshipDB(url,username,pasword,validatorF);

        NetworkService service = new NetworkService(repoFriendshipDB,repoUserDB);


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Autentificare.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 660, 400);
        AutentificareController autentificareController = fxmlLoader.getController();
        autentificareController.setService(service);
        stage.setScene(scene);
        stage.setTitle("Log in!");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}