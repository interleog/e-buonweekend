package it.uiip.digitalgarage.ebuonweekend.ibe;


import it.uiip.digitalgarage.ebuonweekend.entity.Viaggio;

public interface ViaggioService {

    boolean insert(Viaggio v);
    boolean deleteByUserHouseownerIDExchange(String user, String houseOwner, int idExchange);
    Viaggio[] selectJourneysByUserHouseownerIDExchange(String user, String houseOwner, int idExchange);
    Viaggio[] selectCompletedJourney(String user);
}
