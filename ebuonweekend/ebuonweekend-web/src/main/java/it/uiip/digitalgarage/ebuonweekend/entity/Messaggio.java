package it.uiip.digitalgarage.ebuonweekend.entity;

import java.time.LocalDate;

public class Messaggio {

    private long id;
    private String sender;
    private String receiver;
    private String message;
    private Integer isHand;
    private LocalDate dataMessage;
    private String feedback;
    private Boolean checkedFlag;
    private long idHouseRcv;

    public Messaggio(long id, String sender, String receiver, String message, Integer isHand, LocalDate dataMessage,String feedback,Boolean checkedFlag) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isHand = isHand;
        this.dataMessage = dataMessage;
        this.feedback = feedback;
        this.checkedFlag = checkedFlag;
        this.idHouseRcv = 0;
    }

    public Boolean getCheckedFlag() {
        return checkedFlag;
    }

    public void setCheckedFlag(Boolean checkedFlag) {
        this.checkedFlag = checkedFlag;
    }

    public long getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public Integer getIsHand() {
        return isHand;
    }

    public LocalDate getDataMessage() {
        return dataMessage;
    }

    public String getFeedback(){ return feedback;}

    public void setId(long id) {
        this.id = id;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setIsHand(Integer isHand) {
        this.isHand = isHand;
    }

    public void setDataMessage(LocalDate dataMessage) {
        this.dataMessage = dataMessage;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public long getIdHouseRcv() {
        return idHouseRcv;
    }

    public void setIdHouseRcv(long idHouseRcv) {
        this.idHouseRcv = idHouseRcv;
    }
}
