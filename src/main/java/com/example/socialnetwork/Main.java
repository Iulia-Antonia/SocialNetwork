package com.example.socialnetwork;


import com.example.socialnetwork.Domain.Friendship;
import com.example.socialnetwork.Domain.Tuple;
import com.example.socialnetwork.Domain.User;
import com.example.socialnetwork.Domain.validators.FriendshipValidator;
import com.example.socialnetwork.Domain.validators.UserValidator;
import com.example.socialnetwork.Domain.validators.Validator;
import com.example.socialnetwork.Repository.RepoFriendshipDB;
import com.example.socialnetwork.Repository.RepoInMemory;
import com.example.socialnetwork.Repository.RepoUserDB;
import com.example.socialnetwork.Repository.Repository;
import com.example.socialnetwork.Service.FriendshipService;
import com.example.socialnetwork.Service.NetworkService;
import com.example.socialnetwork.Service.UserService;
import com.example.socialnetwork.UserInterface.UserInterface;
import com.example.socialnetwork.UserInterface.UserInterface2;

public class Main {
    public static void main(String[] args) {
        String username = "postgres";
        String pasword = "iulia2002";
        String url="jdbc:postgresql://localhost:5432/SocialNetwork";

        Validator<User> validatorU = new UserValidator();
        Validator<Friendship> validatorF = new FriendshipValidator();

        Repository<Long,User> repoUserDB = new RepoUserDB(url,username,pasword,validatorU);
        Repository<Tuple<Long,Long>,Friendship> repoFriendshipDB = new RepoFriendshipDB(url,username,pasword,validatorF);

        Repository<Long, User> repoUser = new RepoInMemory<Long, User>(validatorU);
        Repository<Tuple<Long,Long>,Friendship> repoFriendship = new RepoInMemory<Tuple<Long, Long>, Friendship>(validatorF);

//        UserService userService = new UserService(repoUser);
//        FriendshipService friendshipService = new FriendshipService(repoFriendship,repoUser);
//
        NetworkService service = new NetworkService(repoFriendshipDB,repoUserDB);

        UserInterface2 ui = new UserInterface2(service);
        ui.runMenu();
    }
}