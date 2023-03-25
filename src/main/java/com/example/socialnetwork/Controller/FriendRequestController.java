package com.example.socialnetwork.Controller;

import com.example.socialnetwork.Domain.*;
import com.example.socialnetwork.Service.NetworkService;
import com.example.socialnetwork.Utils.Observer.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class FriendRequestController {
    private NetworkService service;
    private com.example.socialnetwork.Domain.User user;
    private UserPageController userPageController;
    public FriendRequestController(){}
    @FXML
    public Label messageLabel;
    @FXML
    public Button adaugaButton;

    @FXML
    public TextField cautaNumeField;
    @FXML
    public TextField cautaPrenumeField;

    @FXML
    public TableView<User> usersTableView;

    @FXML
    public TableColumn<User, String> usersTableViewNume;

    @FXML
    public TableColumn<User, String> usersTableViewPrenume;

    public ObservableList<User> modelUtilizatori = FXCollections.observableArrayList();

    public ObservableList<User> modelSearch = FXCollections.observableArrayList();

    public ObservableList<User> modelFriends = FXCollections.observableArrayList();

    public void initController(NetworkService service,UserPageController userPageController,User user) throws SQLException {
        this.service = service;
        this.userPageController = userPageController;
        this.user = user;
        initModelUtilizatori();
        initModelFriends();
        initModelSearch(null,null);
    }

    @FXML
    public void initialize(){
        usersTableViewNume.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        usersTableViewPrenume.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        usersTableView.setItems(modelSearch);

    }

    private void initModelUtilizatori()throws SQLException{
        List<User> users = service.getAllUsers();
        modelUtilizatori.setAll(users);
    }
    private void initModelSearch(String nume, String prenume){
        List<User> users = new ArrayList<>();
        users = service.getUsersWithNames(nume,prenume);
        modelSearch.setAll(users);
    }

    private void initModelFriends()throws SQLException{
        Vector<Long> friends = service.findFriends(this.user);
        List<User> users = new ArrayList<>();
        for(Long id: friends){
            users.add(service.findUser(id));
        }
        modelFriends.setAll(users);
    }

    public void handleCauta(){
        String nume = cautaNumeField.getText();
        String prenume = cautaPrenumeField.getText();
        if(nume.isEmpty())nume = null;
        if(prenume.isEmpty())prenume = null;
        initModelSearch(nume,prenume);
    }

    public void addFriend()throws SQLException{
        User friendToAdd = usersTableView.getSelectionModel().getSelectedItem();
        List<FriendshipDto> friends = service.findFriendshipsForUser(this.user);
        FriendshipDto friendship = new FriendshipDto();
        boolean ok = false;
        for(FriendshipDto friendshipDto: friends){
            if(Objects.equals(friendshipDto.getIdFriend(),friendToAdd.getID())){
                ok = true;
                friendship = friendshipDto;
               // break;
            }
        }
        if(friendToAdd.getID().equals(user.getID()))messageLabel.setText("Nu puteți să vă trimiteți dumneavoastră o cerere!");
        else if(!ok){
            Tuple<Long,Long> t = new Tuple<>(user.getID(),friendToAdd.getID());
            service.addFriendship(new Friendship(t));
            messageLabel.setText("Cererea dumneavoastră a fost trimisă!");
        }
        else if(friendship.getStatus().equals("acceptata")) messageLabel.setText("Sunteți prieteni deja!");
        else if(friendship.getStatus().equals("in_asteptare"))messageLabel.setText("Ați trimis deja o cerere!");
        else if(friendship.getStatus().equals("respinsa"))messageLabel.setText("Utilizatorul selectat nu dorește să fiți prieteni!");
    }
}
