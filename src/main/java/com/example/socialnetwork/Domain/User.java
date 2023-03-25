package com.example.socialnetwork.Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long>{
    private String firstName;
    private String lastName;
    private String cod;
    private List<Long> friends = new ArrayList<>();

    public User(String firstName, String lastName){
        this.firstName=firstName;
        this.lastName=lastName;
        this.friends = new ArrayList<>();
    }
    public User(String firstName, String lastName, String cod){
        this.firstName=firstName;
        this.lastName=lastName;
        this.cod=cod;
    }

    public void setFriends(List<Long> friends) {
        this.friends = friends;
    }

    public String getFirstName(){return firstName;}
    public void setFirstName(String firstName){this.firstName=firstName;}

    public String getLastName(){return lastName;}
    public void setLastName(String lastName){this.lastName=lastName;}

    public List<Long> getFriends(){return friends;}

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(friends, user.friends);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, friends);
    }

    @Override
    public String toString(){
        return "Utilizator{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", cod='" + cod + '\'' +
                '}';
        //", friends=" + friendsToString() +
    }
    public void addFriend(User u){
        friends.add(u.getID());
    }
    public void deleteFriend(Long u){
        friends.remove(u);
    }
}
