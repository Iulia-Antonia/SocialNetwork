package com.example.socialnetwork.Repository;


import com.example.socialnetwork.Domain.User;
import com.example.socialnetwork.Domain.validators.Validator;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;

public class RepoUserDB implements Repository<Long, User>{
    private String url;
    private String userName;
    private String password;
    private Validator<User> validator;

    public RepoUserDB(String url, String userName, String password, Validator<User> validator){
        this.url=url;
        this.userName=userName;
        this.password=password;
        this.validator=validator;
    }

    @Override
    public Optional<User> getOne(Long id){
        try(Connection connection = DriverManager.getConnection(url,userName,password);
            PreparedStatement statement = connection.prepareStatement("SELECT * from users");
            ResultSet resultSet = statement.executeQuery();){
            while(resultSet.next()){
                Long id_user = resultSet.getLong("id");
                if(id_user.equals(id)){
                    String firstName = resultSet.getString("firstname");
                    String lastName = resultSet.getString("lastname");
                    String cod = resultSet.getString("cod");

                    User u = new User(firstName,lastName,cod);
                    u.setId(id_user);
                    return Optional.ofNullable(u);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public Iterable<User> getAll(){
        Set<User> users = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url,userName,password);
            PreparedStatement statement = connection.prepareStatement("SELECT * from users");
            ResultSet resultSet = statement.executeQuery()){

            while(resultSet.next()){
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                String cod = resultSet.getString("cod");

                User user = new User(firstName,lastName,cod);
                user.setId(id);
                users.add(user);
            }
            return users;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }
    @Override
    public Optional<User> save(User entity){
        String sql = "insert into users(firstname,lastname,cod) values (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url,userName,password);
             PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1,entity.getFirstName());
            ps.setString(2,entity.getLastName());
            ps.setString(3,entity.getCod());

            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public Optional<User> delete(Long id){
        String sql = "DELETE from users where id = ?";
        try (Connection connection = DriverManager.getConnection(url,userName,password);
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setLong(1,id);
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public Optional<User> update(User entity){
        String sql = "UPDATE users SET friends = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url,userName,password);
             PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setArray(1, (Array) entity.getFriends());
            ps.setLong(2,entity.getID());

            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }
//    public Optional<User> update(User entity){
//        String sql1 = "UPDATE users SET firstname = ? WHERE id = ?";
//        String sql2 = "UPDATE users SET lastname = ? WHERE id = ?";
//        try (Connection connection = DriverManager.getConnection(url,userName,password);
//             PreparedStatement ps1 = connection.prepareStatement(sql1);
//             PreparedStatement ps2 = connection.prepareStatement(sql2)){
//
//            ps1.setString(1,entity.getFirstName());
//            ps1.setLong(2,entity.getID());
//            ps2.setString(1,entity.getFirstName());
//            ps2.setLong(2,entity.getID());
//
//            ps1.executeUpdate();
//            ps2.executeUpdate();
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//        return Optional.empty();
//    }

    @Override
    public int getSize() {
        Set<User> users = new HashSet<>();
//        String sql = "SELECT COUNT(Id) FROM Users";
//        try(Connection connection = DriverManager.getConnection(url,userName,password);
//            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(Id) FROM Users");
//            int nr = statement.getUpdateCount()){
        try(Connection connection = DriverManager.getConnection(url,userName,password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = statement.executeQuery()){

            int nr=0;
            if(resultSet.next()) {
                while(resultSet.next()){nr++;}
                return nr;
            }
            return nr;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }


    public Vector<Long> findFriends(User u) {
        Vector<Long> users = new Vector<>();
        String sql = "select idfriend1 as id from friendships where idfriend2 = ? and status=? union select idfriend2 as id from friendships where idfriend1 = ? and status = ?";
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, u.getID());
            ps.setString(2, "acceptata");
            ps.setLong(3, u.getID());
            ps.setString(4, "acceptata");

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    users.add(id);
                }
            }

            return users;

        } catch (SQLException sq) {
            System.out.println(sq.getMessage());
        }
        return users;
    }
}

