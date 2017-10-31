package it.uiip.digitalgarage.ebuonweekend.ibe;

import it.uiip.digitalgarage.ebuonweekend.entity.Messaggio;
import it.uiip.digitalgarage.ebuonweekend.entity.Utente;

public interface UtenteService {

    boolean insert(Utente user);
    boolean update(Utente user);
    boolean delete(String nickName);
    boolean authentication(Utente user);
    Utente selectByNick(String nick);
    String selectFeedByNick(String nick);
    boolean insertFeedback(String sdr, String owner, String feed);
    Messaggio[] pendingFeedbacks(String nick);
    Messaggio[] selectMyJourneys(String nick);
}
