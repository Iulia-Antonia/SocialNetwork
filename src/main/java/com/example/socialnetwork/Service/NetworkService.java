package com.example.socialnetwork.Service;

import com.example.socialnetwork.Domain.*;
import com.example.socialnetwork.Repository.Repository;
import com.example.socialnetwork.Utils.ChangeEventType;
import com.example.socialnetwork.Utils.FriendshipChangeEvent;
import com.example.socialnetwork.Utils.Observer.Observer;
import com.example.socialnetwork.Utils.Observer.Observable;
import com.example.socialnetwork.Utils.UserChangeEvent;

import java.sql.SQLException;
import java.util.*;

public class NetworkService implements Observable<FriendshipChangeEvent> {
    private Repository<Tuple<Long,Long>, Friendship> repoF;
    private Repository<Long, User> repoU;
    private final List<Observer<FriendshipChangeEvent>> observers = new ArrayList<>();
    public NetworkService(Repository<Tuple<Long,Long>, Friendship> repoF, Repository<Long,User> repoU){
        this.repoF=repoF;
        this.repoU=repoU;
    }
    public boolean existFriendship(Friendship f){
        Iterable<Friendship> lista = repoF.getAll();
        Vector<Friendship> lf = new Vector<>();
        lista.forEach(lf::add);
        for (Friendship friendship : lf) {
            if (friendship.getID().equals(f.getID())) return true;
        }
        return false;
    }

    public void addFriendship(Friendship f){
        if(!existFriendship(f)){
//            User u1 = repoU.getOne(f.getID().getRight()).get();
//            u1.addFriend(repoU.getOne(f.getID().getLeft()).get());
//            User u2 = repoU.getOne(f.getID().getLeft()).get();
//            u2.addFriend(repoU.getOne(f.getID().getRight()).get());

            repoU.getOne(f.getID().getRight()).get().
                    addFriend(repoU.getOne(f.getID().getLeft()).get());
            repoU.getOne(f.getID().getLeft()).get().
                    addFriend(repoU.getOne(f.getID().getRight()).get());
            repoF.save(f);
//            repoU.update(u1);
//            repoU.update(u2);
        }
    }

    public void deleteFriendship(Tuple<Long,Long> id){
        Long id1 = id.getLeft();
        Long id2 = id.getRight();
        repoU.getOne(id1).get().deleteFriend(id2);
        repoU.getOne(id2).get().deleteFriend(id1);
        Friendship f = repoF.getOne(id).get();
        FriendshipChangeEvent friendshipChangeEvent = new FriendshipChangeEvent(ChangeEventType.DELETE,f);
        repoF.delete(id);
        notifyObservers(friendshipChangeEvent);
    }

    public String friendshipToString(Friendship f) {
        return "Friendship{" +
                "date=" + f.getDate() +
                "intre: "+ repoU.getOne(f.getID().getLeft()).get().toString()+
                repoU.getOne(f.getID().getRight()).get().toString()+
                "status: "+repoF.getOne(f.getID()).get().getStatus_prietenie().toString()+
                '}';
    }

    public void deleteInCascade(Long id){
        Iterable<Friendship> lista;
        lista = repoF.getAll();
        Vector<Friendship> lf = new Vector<>();
        lista.forEach(lf::add);
        for(int i=0;i<lf.size();i++){
            if(Objects.equals(lf.get(i).getID().getLeft(), id) ||
                    Objects.equals(lf.get(i).getID().getRight(), id))
            {

                deleteFriendship(lf.get(i).getID());

            }
        }
    }

    public Iterable<Friendship> getAllFriendships()
    {
        return repoF.getAll();
    }

    public User getUser(Long id){
        Iterable<User> lista;
        lista = repoU.getAll();
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
        while (repoU.getOne(id).isPresent()) {
            id = new Random().nextLong(leftLimit, rightLimit);
        }
        u.setId(id);
        return repoU.save(u);
    }

    public Optional<User> deleteUser(Long id){
        return repoU.delete(id);
    }

    public List<User> getAllUsers(){
        List<User> list = new ArrayList<>();
        Iterable<User> ul;
        ul = repoU.getAll();
        ul.forEach(list::add);
        return list;
    }

    public List<Long> getFrinedsList(Long id){
        return repoU.getOne(id).get().getFriends();
    }

    public boolean findUserWithCod(String cod){
        Iterable<User> lista;
        lista = repoU.getAll();
        Vector<User> lu = new Vector<>();
        lista.forEach(lu::add);
        for (User user : lu) {
            if (Objects.equals(user.getCod(), cod)) return true;
        }
        return false;
    }

    public User getUserWithCode(String cod){
        Iterable<User> lista;
        lista = repoU.getAll();
        Vector<User> lu = new Vector<>();
        lista.forEach(lu::add);
        for (User user : lu) {
            if (Objects.equals(user.getCod(), cod)) return user;
        }
        return null;
    }

    public Vector<Long> findFriends(User u){
        Iterable<Friendship> lista;
        lista = repoF.getAll();
        Vector<Long> listaId = new Vector<>();
        Vector<Friendship> lu = new Vector<>();
        lista.forEach(lu::add);
        for (Friendship friendship : lu) {
            if((friendship.getStatus_prietenie().toString() == "acceptata") &&
            friendship.getID().getRight().equals(u.getID()))listaId.add(friendship.getID().getLeft());
            else if (friendship.getStatus_prietenie().toString()=="acceptata" &&
            friendship.getID().getLeft().equals(u.getID()))listaId.add(friendship.getID().getRight());

            }
        return listaId;
    }

    public User findUser(Long id){
        return repoU.getOne(id).get();
    }

    public void acceptaCerere(Friendship f){
        if(!existFriendship(f)){
            String status = "acceptata";
            f.setStatus_prietenie(Status.valueOf(status));
            repoF.update(f);
        }
    }
    public void respingeCerere(Friendship f){
        if(!existFriendship(f)){
            String status = "respinsa";
            f.setStatus_prietenie(Status.valueOf(status));
            repoF.update(f);
        }
    }

    public FriendshipChangeEvent updateFriendship(Tuple<Long,Long> t, Status status){
        Friendship f = repoF.getOne(t).get();
        f.setStatus_prietenie(status);
        repoF.update(f);
        FriendshipChangeEvent friendshipChangeEvent = new FriendshipChangeEvent(ChangeEventType.UPDATE,f);
        notifyObservers(friendshipChangeEvent);
        return friendshipChangeEvent;
    }

//    public void updateFriendship(Tuple<Long,Long> t, Status status){
//        Friendship f = repoF.getOne(t).get();
//        f.setStatus_prietenie(status);
//        repoF.update(f);
//        notifyObservers(new FriendshipChangeEvent(ChangeEventType.UPDATE,f));
//    }
    @Override
    public void addObserver(Observer<FriendshipChangeEvent> e) {
        observers.add(e);
    }
    @Override
    public void removeObserver(Observer<FriendshipChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendshipChangeEvent t) {
        observers.stream().forEach(x-> {
            try {
                x.update(t);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
//    public List<User> getUsersWithNames(String nume){
//        List<User> list = new ArrayList<>();
//        List<User> users = new ArrayList<>();
//        Iterable<User> ul;
//        ul = repoU.getAll();
//        ul.forEach(list::add);
//        String[] nameSeparated = nume.split(" ");
//        if(nameSeparated.length == 2) {
//            for (User user : list) {
//                if ((user.getFirstName() == nameSeparated[0] && user.getLastName() == nameSeparated[1])
//                     || (user.getFirstName() == nameSeparated[1] && user.getLastName() == nameSeparated[0])) users.add(user);
//            }
//        }
//        return users;
//    }
    public List<User> getUsersWithNames(String nume, String prenume){
        List<User> list = new ArrayList<>();
        List<User> users = new ArrayList<>();
        Iterable<User> ul;
        ul = repoU.getAll();
        ul.forEach(list::add);
        if(nume == null && prenume == null)return list;
        if(nume == null && prenume != null){
            for(User user: list){
                if(user.getLastName().equals(prenume))users.add(user);
            }
            return users;
        }
        if(nume != null && prenume == null){
            for(User user: list){
                if(user.getFirstName().equals(nume))users.add(user);
            }
            return users;
        }
        for(User user: list){
            if(user.getFirstName().equals(nume) && user.getLastName().equals(prenume))users.add(user);
        }
        return users;
    }
    public List<FriendshipDto> findFriendshipsForUser(User u){
        Iterable<Friendship> lista = repoF.getAll();
        Vector<Friendship> lf = new Vector<>();
        lista.forEach(lf::add);
        List<FriendshipDto> friendshipDtos = new ArrayList<>();
        for (Friendship friendship : lf) {
            if (friendship.getID().getLeft().equals(u.getID())) {
                User friend = repoU.getOne(friendship.getID().getRight()).get();
                friendshipDtos.add(new FriendshipDto(u.getID(), friend.getID(),friend.getFirstName(),friend.getLastName(),
                        friendship.getStatus_prietenie().toString(),friendship.getDate().toString()));
            }
            else if (friendship.getID().getRight().equals(u.getID())) {
                User friend = repoU.getOne(friendship.getID().getLeft()).get();
                friendshipDtos.add(new FriendshipDto(u.getID(), friend.getID(),friend.getFirstName(),friend.getLastName(),
                        friendship.getStatus_prietenie().toString(),friendship.getDate().toString()));
            }
            }
        return friendshipDtos;
    }
}

