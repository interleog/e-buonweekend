package it.uiip.digitalgarage.ebuonweekend.idao;


import it.uiip.digitalgarage.ebuonweekend.entity.Viaggio;

public interface ViaggioDAO {

    boolean insert(Viaggio v);
    boolean updateIDExchange(Viaggio v, int idExchange);
    boolean deleteByUserHouseownerIDExchange(String user, String houseOwner, int idExchange);
    Viaggio selectByUserIDHouseDate(String user, String IDHouse, String journeyDate);
    Viaggio[] selectByUserHouseownerIDExchange(String user, String houseOwner, int idExchange);
    Viaggio[] selectByUserHouseownerDateIDExchange(String user, String houseOwner, String date, int idExchange);
    Viaggio[] selectCompletedJourney(String user);
}
