package it.uiip.digitalgarage.ebuonweekend.entity;


public class Viaggio {
    private long id;
    private String user;
    private String houseOwner;
    private String idHouse;
    private String journeyDate;
    private long idExchange;
    private String city;
    private String address;
    private String feedback;

    public Viaggio(long id, String user, String houseOwner, String idHouse, String journeyDate, long idExchange) {
        this.id = id;
        this.user = user;
        this.houseOwner = houseOwner;
        this.idHouse = idHouse;
        this.journeyDate = journeyDate;
        this.idExchange = idExchange;
        this.city = null;
        this.address = null;
        this.feedback = null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHouseOwner() {
        return houseOwner;
    }

    public void setHouseOwner(String houseOwner) {
        this.houseOwner = houseOwner;
    }

    public String getIdHouse() {
        return idHouse;
    }

    public void setIdHouse(String idHouse) {
        this.idHouse = idHouse;
    }

    public String getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(String journeyDate) {
        this.journeyDate = journeyDate;
    }

    public long getIdExchange() {
        return idExchange;
    }

    public void setIdExchange(long idExchange) {
        this.idExchange = idExchange;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
