package com.example.socialnetwork.Controller;

import com.example.socialnetwork.Domain.*;
import com.example.socialnetwork.Domain.validators.InvalidId;
import com.example.socialnetwork.HelloApplication;
import com.example.socialnetwork.Service.NetworkService;
import com.example.socialnetwork.Utils.FriendshipChangeEvent;
import com.example.socialnetwork.Utils.UserChangeEvent;
import com.example.socialnetwork.Utils.Observer.Observer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class UserPageController implements Observer<FriendshipChangeEvent> {
    User user;

    @FXML
    public Button acceptRequestButton;
    @FXML
    public Button respingeRequestButton;

    @FXML
    public TableView<User> prieteniTableView;
    @FXML
    public TableColumn<User, String> prieteniTableViewNume;

    @FXML
    public TableColumn<User, String> prieteniTableViewPrenume;

    @FXML
    public Button prieteniTableViewdeleteButton;

    @FXML
    public TableView<FriendshipDto> requestTableView;

    @FXML
    public TableColumn<FriendshipDto, String> requestTableViewData;

    @FXML
    public TableColumn<FriendshipDto, String> requestTableViewNume;

    @FXML
    public TableColumn<FriendshipDto, String> requestTableViewPrenume;

    @FXML
    public TableColumn<FriendshipDto, String> requestTableViewStatus;

    @FXML
    public Button sendRequestButton;

    public ObservableList<User> modelFriends = FXCollections.observableArrayList();

    public ObservableList<FriendshipDto> modelRequests = FXCollections.observableArrayList();

    private NetworkService service;
    private AutentificareController autentificareController;
    public void init_Controller(NetworkService service, AutentificareController autentificareController, User user) throws SQLException {
        this.service=service;
        this.autentificareController=autentificareController;
        this.user = user;
        service.addObserver(this);
        initModelFriends();
        initModelRequest();
    }

    @FXML
    public void initialize(){
        prieteniTableViewNume.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        prieteniTableViewPrenume.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        prieteniTableView.setItems(modelFriends);
        requestTableViewNume.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        requestTableViewPrenume.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        requestTableViewData.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        requestTableViewStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        requestTableView.setItems(modelRequests);

    }

    private void initModelFriends()throws SQLException{
        Vector<Long> friends = service.findFriends(this.user);
        List<User> users = new ArrayList<>();
        for(Long id: friends){
            users.add(service.findUser(id));
        }
        modelFriends.setAll(users);
    }

    private void initModelRequest()throws SQLException{
        List<FriendshipDto> friendships = service.findFriendshipsForUser(this.user);
        modelRequests.setAll(friendships);
    }

    public void handleDeleteFriend()throws SQLException, InvalidId{
        User friend = prieteniTableView.getSelectionModel().getSelectedItem();
        Long idFriend = friend.getID();
        if(friend!=null){
//            this.user.deleteFriend(idFriend);
            List<FriendshipDto> friendshipDtos = service.findFriendshipsForUser(this.user).stream().filter(x-> Objects.equals(x.getIdFriend(),idFriend)).toList();
            Tuple<Long,Long> t = new Tuple<>(user.getID(),idFriend);
            service.deleteFriendship(t);
        }
    }

    public void acceptRequest()throws SQLException,InvalidId{
        FriendshipDto friend = requestTableView.getSelectionModel().getSelectedItem();
        if(friend!=null){
            if(Status.valueOf(friend.getStatus()) == Status.in_asteptare){
                Tuple<Long,Long> t = new Tuple<>(friend.getIdFriendship(), friend.getIdFriend());
                service.updateFriendship(t, Status.acceptata);
            }
        }
    }

    public void respingeRequest()throws SQLException,InvalidId{
        FriendshipDto friend = requestTableView.getSelectionModel().getSelectedItem();
        if(friend!=null){
            if(Status.valueOf(friend.getStatus()) == Status.in_asteptare){
                Tuple<Long,Long> t = new Tuple<>(friend.getIdFriendship(), friend.getIdFriend());
                //service.updateFriendship(t, Status.respinsa);
                updatef(service.updateFriendship(t, Status.respinsa));
            }
        }
    }
    @Override
    public void update(FriendshipChangeEvent friendshipChangeEvent) throws SQLException {
        initModelFriends();
        initModelRequest();
    }

    public void updatef(FriendshipChangeEvent friendshipChangeEvent) throws SQLException {
        initModelFriends();
        initModelRequest();
    }

    public void sendRequestButton(){
        URL url = HelloApplication.class.getResource("FriendRequest.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        AnchorPane root = null;
        try{
            root = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        FriendRequestController friendRequestController = loader.getController();
        try{
            friendRequestController.initController(service, this, this.user);
        }catch (SQLException e){
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root, 720,390));
        stage.show();
        Stage thisstage = (Stage) sendRequestButton.getScene().getWindow();
    }
}
