package com.example.socialnetwork.Domain;

import java.time.LocalDate;

public class Friendship extends Entity<Tuple<Long,Long>>{
    LocalDate date;
    Status status_prietenie;
    public Friendship(){
        date = LocalDate.now();
    }
    public Friendship(Tuple<Long,Long> t){
        this.setId(t);
        date = LocalDate.now();
        status_prietenie = Status.in_asteptare;
    }

    public Friendship(LocalDate data, Tuple<Long,Long> t){
        this.setId(t);
        date = data;
    }
    public Friendship(LocalDate data, Tuple<Long,Long> t, Status status){
        this.setId(t);
        date = data;
        this.status_prietenie=status;
    }
    public LocalDate getDate(){return date;}

    public Status getStatus_prietenie() {
        return status_prietenie;
    }

    public void setStatus_prietenie(Status status_prietenie) {
        this.status_prietenie = status_prietenie;
    }
}
