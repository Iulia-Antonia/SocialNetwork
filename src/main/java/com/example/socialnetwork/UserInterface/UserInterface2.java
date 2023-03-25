package com.example.socialnetwork.UserInterface;



import com.example.socialnetwork.Domain.Friendship;
import com.example.socialnetwork.Domain.Status;
import com.example.socialnetwork.Domain.Tuple;
import com.example.socialnetwork.Domain.User;
import com.example.socialnetwork.Domain.validators.ValidationException;
import com.example.socialnetwork.Service.FriendshipService;
import com.example.socialnetwork.Service.NetworkService;
import com.example.socialnetwork.Service.UserService;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class UserInterface2 {
    private NetworkService service;

    public UserInterface2(NetworkService service){
        this.service=service;
    }

    void menu(){
        System.out.println("1.Adauga utilizator");
        System.out.println("2.Sterge utilizator");
        System.out.println("3.Vezi toti utilizatorii");
        System.out.println("4.Adauga prietenie");
        System.out.println("5.Sterge prietenie");
        System.out.println("6.Vezi toate prieteniile");
        System.out.println("7.Vezi toate prieteniile unui utilizator");
        System.out.println("x.Iesire");
    }

    public void runMenu(){
        String userChoice;
        Scanner cin= new Scanner(System.in);
 //       importEntities();
        boolean go=true;
        while(go){
            menu();
            System.out.println("Dati obtiunea dorita: ");
            userChoice = cin.nextLine();
            if(userChoice.equals("x")){go=false;}
            else if (userChoice.equals("1")) {
                addUser();
            } else if (userChoice.equals("2")) {
                deleteUser();
            } else if (userChoice.equals("3")) {
                showAllU();
            } else if (userChoice.equals("4")) {
                addFriendship();
            } else if (userChoice.equals("5")) {
                deleteFriendship();
            } else if (userChoice.equals("6")) {
                showAllF();
            } else if (userChoice.equals("7")) {
                showAllFforU();
            } else {System.out.println("Nu ati introdus o obtiune corecta!");}
        }
    }

    public void importEntities(){
        try {
            User u1=new User("Cui","Ana");
            User u2=new User("Bota","Marian");
            User u3=new User("Manu","Costel");
            User u4=new User("a","a");
            User u5=new User("z","z");
            User u6=new User("Ciupe","Ina");

            service.addUser(u1);
            service.addUser(u2);
            service.addUser(u3);
            service.addUser(u4);
            service.addUser(u5);
            service.addUser(u6);
            Long id1,id2,id3,id4,id5,id6;
            id1 = u1.getID();
            id2 = u2.getID();
            id3 = u3.getID();
            id4 = u4.getID();
            id5 = u5.getID();
            id6 = u6.getID();;

            Tuple<Long,Long> t1 = new Tuple<>(id1,id2);
            Tuple<Long,Long>t2 = new Tuple<>(id1,id3);
            Tuple<Long,Long>t4 = new Tuple<>(id3,id4);
            Tuple<Long,Long>t5 = new Tuple<>(id5,id6);
            service.addFriendship(new Friendship(t1));
            service.addFriendship(new Friendship(t2));
            service.addFriendship(new Friendship(t4));
            service.addFriendship(new Friendship(t5));
        }
        catch (ValidationException | IllegalArgumentException ve){
            System.out.println(ve.getMessage());
        } catch (Throwable e){
            System.out.println(e.getMessage());
        }
    }

    public void addUser(){
        try {
            Scanner cin = new Scanner(System.in);
            System.out.println("Dati numele de familie:");
            String first_name = cin.nextLine();
            System.out.println("Dati prenumele :");
            String last_name = cin.nextLine();
            System.out.println("Dati codul: ");
            String cod = cin.nextLine();
            System.out.println(service.addUser(new User(first_name, last_name,cod)).toString());
        }
        catch (ValidationException | IllegalArgumentException ve){
            System.out.println(ve.getMessage());
        } catch (Throwable e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteUser(){
        try {
            Iterable<User>lista= service.getAllUsers();
            Vector<User> lu = new Vector<>();
            lista.forEach(user-> lu.add(user));
            for(int i=0;i<lu.size();i++){
                System.out.println(i+"->"+lu.get(i).toString());
            }

            Scanner cin = new Scanner(System.in);
            System.out.println("Dati numarul utilizatorului de sters:");
            String numar = cin.nextLine();
            int numberToDellete = Integer.parseInt(numar);
            if(0<=numberToDellete && numberToDellete<lu.size()){
                service.deleteInCascade(lu.get(numberToDellete).getID());
                System.out.println(service.deleteUser(lu.get(numberToDellete).getID()).toString());
            }
            else System.out.println("Valoarea pe care ati dat o de sters este gresita!");
        }
        catch (NumberFormatException nfe) {
            System.out.println("Valoarea pe care ati dat-o nu reprezinta un numar!");
        }
        catch (ValidationException | IllegalArgumentException ve){
            System.out.println(ve.getMessage());
        }
        catch (Throwable e){
            System.out.println(e.getMessage());
        }

    }
    public void showAllU(){
        try{
            List<User>lista;
            lista= service.getAllUsers();
            for (int i=0;i<lista.size();i++){
                System.out.println(lista.get(i).toString());
            }
        }
        catch (ValidationException | IllegalArgumentException ve){
            System.out.println(ve.getMessage());
        }
        catch (Throwable e){
            System.out.println(e.getMessage());
        }
    }

    public void showAllFforU(){
        try {
            Iterable<User>lista= service.getAllUsers();
            Vector<User> lu = new Vector<>();
            lista.forEach(user-> lu.add(user));
            for(int i=0;i<lu.size();i++){
                System.out.println(i+"->"+lu.get(i).toString());
            }

            Scanner cin = new Scanner(System.in);
            System.out.println("Dati numarul utilizatorului pentru care doriti sa vedeti lista de prieteni:");
            String numar = cin.nextLine();
            int numberInt = Integer.parseInt(numar);
            if(0<=numberInt && numberInt<lu.size()){
                User user = lu.get(numberInt);
                System.out.println(user.getFirstName()+" "+user.getLastName()+" are prietenii:");
                List<Long> friendsid=service.getFrinedsList(user.getID());
                for(int i=0;i<friendsid.size();i++){
                    User u = service.getUser(friendsid.get(i));
                    if(u!=null) System.out.println("- "+u.getFirstName()+" "+u.getLastName());
                }
            }
            else System.out.println("Valoarea pe care ati dat o de sters este gresita!");
        }
        catch (NumberFormatException nfe) {
            System.out.println("Valoarea pe care ati dat-o nu reprezinta un numar!");
        }
        catch (ValidationException | IllegalArgumentException ve){
            System.out.println(ve.getMessage());
        }
        catch (Throwable e){
            System.out.println(e.getMessage());
        }
    }

    public void addFriendship(){
        try {
            List<User>lista;
            lista= service.getAllUsers();
            for (int i=0;i<lista.size();i++){
                System.out.println(i+"-n"+lista.get(i).toString());
            }
            Scanner cin = new Scanner(System.in);
            System.out.println("Dati numarul unuia din prieteni:");
            String nr1 = cin.nextLine();
            System.out.println("Dati numarul celuilalt prieten:");
            String nr2 = cin.nextLine();
            int nr1Int = Integer.parseInt(nr1);
            int nr2Int = Integer.parseInt(nr2);
            if(nr1Int>=0 && nr1Int<lista.size() &&
                    nr2Int>=0 && nr2Int<lista.size()) {
                Long id1 = lista.get(nr1Int).getID();
                Long id2 = lista.get(nr2Int).getID();
                if (!Objects.equals(id1, id2)) {
                    Friendship f = new Friendship(new Tuple<>(id1, id2));
                    String status = "in_asteptare";
                    f.setStatus_prietenie(Status.valueOf(status));
                    service.addFriendship(f);
                }
            }
            else System.out.println("Numele nu sunt corecte!");
        }
        catch (NumberFormatException nfe) {
            System.out.println("Valoarea pe care ati dat-o nu reprezinta un numar!");
        }
        catch(ValidationException | IllegalArgumentException ve){
            System.out.println(ve.getMessage());
        }
        catch(Throwable e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteFriendship(){
        try{
            Iterable<Friendship>lista= service.getAllFriendships();
            Vector<Friendship> lf = new Vector<>();
            lista.forEach(lf::add);
            for(int i=0;i<lf.size();i++){
                System.out.println(i+"->"+service.friendshipToString(lf.get(i)));
            }

            Scanner cin = new Scanner(System.in);
            System.out.println("Dati numarul prieteniei de sters:");
            String numar = cin.nextLine();
            int numberToDellete = Integer.parseInt(numar);

            if(0<=numberToDellete && numberToDellete<lf.size()){
                service.deleteFriendship(lf.get(numberToDellete).getID());

            }
            else System.out.println("Numarul dat nu este corect!");

        }
        catch (NumberFormatException nfe) {
            System.out.println("Valoarea pe care ati dat-o nu reprezinta un numar!");
        }
        catch (ValidationException | IllegalArgumentException ve){
            System.out.println(ve.getMessage());
        }
        catch (Throwable e){
            System.out.println(e.getMessage());
        }
    }

    public void showAllF(){
        try{
            AtomicInteger nr= new AtomicInteger();
            nr.getAndIncrement();
            Iterable<Friendship>lista= service.getAllFriendships();
            Spliterator<Friendship>ls=lista.spliterator();
            ls.forEachRemaining(f->System.out.println(String.valueOf(nr.getAndIncrement())+"-"+service.friendshipToString(f)));
        }
        catch (ValidationException | IllegalArgumentException ve){
            System.out.println(ve.getMessage());
        }
        catch (Throwable e){
            System.out.println(e.getMessage());
        }
    }


}

