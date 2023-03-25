package com.example.socialnetwork.Domain;

public class FriendshipDto {
    private Long idUser;
    private Long idFriend;
    private String firstName;
    private String lastName;
    private String status;
    private String requestDate;

    public FriendshipDto(){}
    public FriendshipDto(Long id, Long idFriend, String firstName, String lastName, String status, String requestDate){
        this.idUser = id;
        this.idFriend = idFriend;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.requestDate = requestDate;
    }

    public Long getIdFriendship() {
        return idUser;
    }

    public Long getIdFriend(){return this.idFriend;}

    public void setIdFriend(Long id){this.idFriend = id;}

    public void setIdFriendship(Long id) {
        this.idUser = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getRequestDate(){return this.requestDate;}
    public void setRequestDate(String date){this.requestDate = date;}

}
