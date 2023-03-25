package com.example.socialnetwork.Repository;

import com.example.socialnetwork.Domain.Friendship;
import com.example.socialnetwork.Domain.Status;
import com.example.socialnetwork.Domain.Tuple;
import com.example.socialnetwork.Domain.validators.Validator;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RepoFriendshipDB implements Repository<Tuple<Long,Long>, Friendship> {
    private String url;
    private String userName;
    private String password;
    private Validator<Friendship> validator;

    public RepoFriendshipDB(String url, String userName, String password, Validator<Friendship> validator){
        this.url=url;
        this.userName=userName;
        this.password=password;
        this.validator=validator;
    }

    @Override
    public Optional<Friendship> getOne(Tuple<Long,Long> id){
        try(Connection connection = DriverManager.getConnection(url,userName,password);
            PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
            ResultSet resultSet = statement.executeQuery();){
            while(resultSet.next()){
                Long id1 = resultSet.getLong("idfriend1");
                Long id2 = resultSet.getLong("idfriend2");
                Status status = Status.valueOf(resultSet.getString("status"));
                Tuple<Long,Long> f = new Tuple<>(id1,id2);
                if(f.equals(id)){
                    LocalDate data = resultSet.getDate("date").toLocalDate();
                    Friendship friendship = new Friendship(data,f,status);
                    return Optional.ofNullable(friendship);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public Iterable<Friendship> getAll(){
        Set<Friendship> friendships = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url,userName,password);
            PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
            ResultSet resultSet = statement.executeQuery()){

            while(resultSet.next()){
                Long id1 = resultSet.getLong("idfriend1");
                Long id2 = resultSet.getLong("idfriend2");
                Status status = Status.valueOf(resultSet.getString("status"));
                Tuple<Long,Long> f = new Tuple<>(id1,id2);
                LocalDate data = resultSet.getDate("date").toLocalDate();
                Friendship friendship = new Friendship(data,f,status);
                friendships.add(friendship);
            }
            return friendships;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return friendships;
    }
    @Override
    public Optional<Friendship> save(Friendship entity){
        String sql = "insert into friendships(idfriend1, idfriend2, date, status) values (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url,userName,password);
             PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setLong(1,entity.getID().getLeft());
            ps.setLong(2,entity.getID().getRight());
            ps.setDate(3, Date.valueOf(entity.getDate()));
            ps.setString(4,"in_asteptare");

            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public Optional<Friendship> delete(Tuple<Long,Long> id){
        String sql = "DELETE from friendships where (idfriend1 = ? and idfriend2 = ?) or (idfriend2 = ? and idfriend1 = ?)";
        try (Connection connection = DriverManager.getConnection(url,userName,password);
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setLong(1,id.getRight());
            statement.setLong(2,id.getLeft());
            statement.setLong(3,id.getRight());
            statement.setLong(4,id.getLeft());
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public Optional<Friendship> update(Friendship entity){
        String sql = "UPDATE friendships SET status = ? WHERE (idfriend1 = ? and idfriend2 = ?) or (idfriend2 = ? and idfriend1 = ?)";
        try (Connection connection = DriverManager.getConnection(url,userName,password);
             PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1,entity.getStatus_prietenie().toString());
            ps.setLong(2,entity.getID().getRight());
            ps.setLong(3,entity.getID().getLeft());
            ps.setLong(4,entity.getID().getRight());
            ps.setLong(5,entity.getID().getLeft());

            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public int getSize() {
        Set<Friendship> friendships = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url,userName,password);
            PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
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
}

