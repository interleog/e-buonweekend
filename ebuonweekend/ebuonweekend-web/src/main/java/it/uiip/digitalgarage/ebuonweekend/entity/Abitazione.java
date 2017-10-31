package it.uiip.digitalgarage.ebuonweekend.entity;
import java.time.LocalDate;

public class Abitazione {
    private long id;
    private String city;
    private String address;
    private String zip;
    private String mq;
    private String utente;
    private LocalDate addedSince;
    private String pathimg;
    private String tags;
    private String descr;
    private boolean isAvailable;
    private String availableDates;
    private String userPicture;
    private String userFeed;


    public Abitazione(long id, String city, String address, String zip, String mq, String utente, LocalDate addedSince,String pathimg, String tags, String descr, boolean isAvailable, String availableDates) {
        this.id = id;
        this.city = city;
        this.address = address;
        this.zip = zip;
        this.mq = mq;
        this.utente = utente;
        this.addedSince = addedSince;
        this.pathimg = pathimg;
        this.tags = tags;
        this.descr = descr;
        this.isAvailable = isAvailable;
        this.availableDates = availableDates;
        this.userPicture = null;
        this.userFeed = null;
    }

    public long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getZip() {
        return zip;
    }

    public String getMq() {
        return mq;
    }

    public String getUtente() {
        return utente;
    }

    public LocalDate getAddedSince() {
        return addedSince;
    }

    public String getPathimg() {
        return pathimg;
    }

    public String getTags() {
        return tags;
    }

    public String getDescr() { return descr; }

    public void setId(long id) {
        this.id = id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setMq(String mq) { this.mq = mq;  }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public void setAddedSince(LocalDate addedSince) {
        this.addedSince = addedSince;
    }

    public void setPathimg(String pathimg) {
        this.pathimg = pathimg;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean available) {
        isAvailable = available;
    }

    public String getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(String availableDates) {
        this.availableDates = availableDates;
    }


    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public String getUserFeed() {
        return userFeed;
    }

    public void setUserFeed(String userFeed) {
        this.userFeed = userFeed;
    }
}