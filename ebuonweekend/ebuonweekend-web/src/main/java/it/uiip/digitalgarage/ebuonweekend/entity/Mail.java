package it.uiip.digitalgarage.ebuonweekend.entity;



import java.time.LocalDate;

public class Mail {
    private long id;
    private String sender;
    private String object;
    private String text;
    private LocalDate mailDay;

    public Mail(long id, String sender, String object, String text, LocalDate mailDay) {
        this.id = id;
        this.sender = sender;
        this.object = object;
        this.text = text;
        this.mailDay = mailDay;
    }

    public long getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getObject() {
        return object;
    }

    public String getText() {
        return text;
    }

    public LocalDate getMailDay() {
        return mailDay;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setMailDay(LocalDate mailDay) {
        this.mailDay = mailDay;
    }
}
