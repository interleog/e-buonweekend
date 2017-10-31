package it.uiip.digitalgarage.ebuonweekend.entity;

import java.time.LocalDate;

public class Utente {
    private  long id;
    private  String firstName;
    private  String nickName;
    private  String password;
    private  String lastName;
    private  String telephone;
    private  String email;
    private  LocalDate memberSince;
    private  String statoUtente;
    private  String feedTotal;
    private  String numFeed;
    private  String feedValue;
    private  String profilePicture;

    public Utente(long id, String firstName, String nickName, String password, String lastName, String telephone, String email, LocalDate memberSince, String statoUtente, String feedTotal, String numFeed, String feedValue, String profilePicture) {
        this.id = id;
        this.firstName = firstName;
        this.nickName = nickName;
        this.password = password;
        this.lastName = lastName;
        this.telephone = telephone;
        this.email = email;
        this.memberSince = memberSince;
        this.statoUtente = statoUtente;
        this.feedTotal = feedTotal;
        this.numFeed = numFeed;
        this.feedValue = feedValue;
        this.profilePicture = profilePicture;
    }


    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getMemberSince() {
        return memberSince;
    }

    public String getStatoUtente() {
        return statoUtente;
    }

    public String getFeedTotal() {
        return feedTotal;
    }

    public String getNumFeed() {
        return numFeed;
    }

    public String getFeedValue() {
        return feedValue;
    }

    public String getProfilePicture(){ return profilePicture;}

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMemberSince(LocalDate memberSince) {
        this.memberSince = memberSince;
    }

    public void setStatoUtente(String statoUtente) {
        this.statoUtente = statoUtente;
    }

    public void setFeedTotal(String feedTotal) {
        this.feedTotal = feedTotal;
    }

    public void setNumFeed(String numFeed) {
        this.numFeed = numFeed;
    }

    public void setFeedValue(String feedValue) {
        this.feedValue = feedValue;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}