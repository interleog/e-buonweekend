package it.uiip.digitalgarage.ebuonweekend.entity;


public class Wishlist {

    private long id;
    private String user;
    private long idHouse;

    public Wishlist(long id, String user, long idHouse) {
        this.id = id;
        this.user = user;
        this.idHouse = idHouse;
    }

    public long getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public long getIdHouse() {
        return idHouse;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setIdHouse(long idHouse) {
        this.idHouse = idHouse;
    }
}
