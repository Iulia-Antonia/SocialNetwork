package com.example.socialnetwork.Service;


import com.example.socialnetwork.Domain.User;
import com.example.socialnetwork.Repository.Repository;

import java.util.*;

public class UserService {
    private Repository<Long, User> repo;

    public UserService(Repository<Long, User> repo)
    {
        this.repo=repo;
    }

    public User getUser(Long id){
        Iterable<User> lista;
        lista = repo.getAll();
        Vector<User> lu = new Vector<>();
        lista.forEach(lu::add);
        for (User user : lu) {
            if (Objects.equals(user.getID(), id)) return user;
        }
        return null;
    }

    public Optional<User> addUser(User u){
        boolean ok=false;
        long leftLimit = 1L;
        long rightLimit = 100L;
        Long id = new Random().nextLong(leftLimit, rightLimit);
        while (repo.getOne(id).isPresent()) {
            id = new Random().nextLong(leftLimit, rightLimit);
        }
        u.setId(id);
        return repo.save(u);
    }

    public Optional<User> deleteUser(Long id){
        return repo.delete(id);
    }

    public List<User> getAllUsers(){
        List<User> list = new ArrayList<>();
        Iterable<User> ul;
        ul = repo.getAll();
        ul.forEach(list::add);
        return list;
    }

    public List<Long> getFrinedsList(Long id){
        return repo.getOne(id).get().getFriends();
    }
}
