package it.uiip.digitalgarage.ebuonweekend.idao;

import it.uiip.digitalgarage.ebuonweekend.entity.Utente;


public interface  UtenteDAO {

     boolean insert(Utente user);
     boolean update(Utente user);  //Query update che NON aggiorna la pw
     boolean update_pw(Utente user);  //Query update che aggiorna anche la pw
     boolean delete(String nickName);
     boolean authentication(Utente user);
     Utente selectByNick(String nick);
     boolean updateFeedBack(Utente user);
     String[] selectBestUsers();



}
