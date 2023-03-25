package com.example.socialnetwork.Service;


import com.example.socialnetwork.Domain.Friendship;
import com.example.socialnetwork.Domain.Tuple;
import com.example.socialnetwork.Domain.User;
import com.example.socialnetwork.Repository.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.Vector;

public class FriendshipService {
    private Repository<Tuple<Long,Long>, Friendship> repoF;
    private Repository<Long, User> repoU;

    public FriendshipService(Repository<Tuple<Long,Long>, Friendship> repoF, Repository<Long,User> repoU){
        this.repoF=repoF;
        this.repoU=repoU;
    }

    public boolean existFriendship(Friendship f){
        Iterable<Friendship> lista = repoF.getAll();
        Vector<Friendship> lf = new Vector<>();
        lista.forEach(lf::add);
        for (Friendship friendship : lf) {
            if (friendship.equals(f)) return true;
        }
        return false;
    }

    public Optional<Friendship> addFriendship(Friendship f){
        if(!existFriendship(f)){
            repoU.getOne(f.getID().getRight()).get().
                    addFriend(repoU.getOne(f.getID().getLeft()).get());
            repoU.getOne(f.getID().getLeft()).get().
                    addFriend(repoU.getOne(f.getID().getRight()).get());
            return repoF.save(f);
        }
        return Optional.empty();
    }

    public Optional<Friendship> deleteFriendship(Tuple<Long,Long> id){
        Long id1 = id.getLeft();
        Long id2 = id.getRight();
        repoU.getOne(id1).get().deleteFriend(id2);
        repoU.getOne(id2).get().deleteFriend(id1);
        return repoF.delete(id);
    }

    public String friendshipToString(Friendship f) {
        return "Friendship{" +
                "date=" + f.getDate() +
                "intre: "+ repoU.getOne(f.getID().getLeft()).get().toString()+
                repoU.getOne(f.getID().getRight()).get().toString()+
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
}
