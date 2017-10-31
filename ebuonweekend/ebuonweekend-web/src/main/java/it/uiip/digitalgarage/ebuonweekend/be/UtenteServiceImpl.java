package it.uiip.digitalgarage.ebuonweekend.be;

import it.uiip.digitalgarage.ebuonweekend.entity.Messaggio;
import it.uiip.digitalgarage.ebuonweekend.entity.Utente;
import it.uiip.digitalgarage.ebuonweekend.ibe.UtenteService;
import it.uiip.digitalgarage.ebuonweekend.idao.MessaggioDAO;
import it.uiip.digitalgarage.ebuonweekend.idao.UtenteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UtenteServiceImpl implements UtenteService {

    @Autowired
    UtenteDAO utenteDAO;

    @Autowired
    MessaggioDAO messaggioDAO;


    @Override
    public boolean insert(Utente user) {
        if (utenteDAO.selectByNick(user.getNickName()) != null) {
            System.out.println("Nickname già esistente!");
            return false;
        } else {
            return utenteDAO.insert(user);
        }
    }

    @Override
    public boolean update(Utente user) {
        if(user.getPassword().equals("") || user.getPassword().equals("undefined"))
            return utenteDAO.update(user);

        else
            return utenteDAO.update_pw(user);

    }

    @Override
    public boolean delete(String nickName) {
        return utenteDAO.delete(nickName);
    }

    @Override
    public boolean authentication(Utente user){ return utenteDAO.authentication(user); }

    @Override
    public Utente selectByNick(String nick){ return utenteDAO.selectByNick(nick); }

    @Override
    public boolean insertFeedback(String sdr, String owner, String feed) {

        //CONTROLLO SE IL FEEDBACK è GIà STATO DATO
        if(messaggioDAO.selectMsgBySdrRcvHand(sdr, owner, "4") != null){
            System.out.println("Feedback già inviato!");
            return false;
        }

        //CONTROLLO SE I DUE UTENTI HANNO GIà SCAMBIATO L'HANDSHAKE
        Messaggio m1[] = messaggioDAO.selectMsgBySdrRcvHand(sdr, owner, "3");
        Messaggio m2[] = messaggioDAO.selectMsgBySdrRcvHand(owner, sdr, "3");
        Messaggio m3[] = messaggioDAO.selectMsgBySdrRcvHand(owner, sdr, "4");

        if(m1 == null || (m2 == null && m3 == null) ){
            System.out.println("Handshake non ancora scambiato!");
            return false;
        }

        //PRELEVO L'HOUSEOWNER DAL DB E AGGIORNO FEEDBACK
        Utente obj_owner = utenteDAO.selectByNick(owner);

        int old_feedTotal = Integer.parseInt(obj_owner.getFeedTotal());
        int new_feedTotal=Integer.parseInt(feed)+old_feedTotal;
        int new_numFeed = Integer.parseInt(obj_owner.getNumFeed()) + 1;
        int new_feedValue= new_feedTotal/new_numFeed;

        obj_owner.setFeedTotal(String.valueOf(new_feedTotal));
        obj_owner.setNumFeed(String.valueOf(new_numFeed));
        obj_owner.setFeedValue(String.valueOf(new_feedValue));

        utenteDAO.updateFeedBack(obj_owner);

        //ORA AL MSG DI HANDSHAKE AGGIUNGO IL VALORE DI FEEDBACK DATO E AGGIORNO isHand=4
        messaggioDAO.updateHandshake(m1[0], feed);  //TODO

        /*Messaggio m = new Messaggio(0, sdr, owner, "FEEDBACK INVIATO CORRETTAMENTE!", 4, LocalDate.now(), "null");
        messaggioDAO.insert(m);*/

        return true;
    }

    @Override
    public String selectFeedByNick(String nick) {

        Utente u = utenteDAO.selectByNick(nick);

        if(u!=null){
            return u.getFeedValue();

        }
        else return null;

    }

    @Override
    public Messaggio[] pendingFeedbacks(String nick) {
        //PRELEVO L'UTENTE DAL DB
        Utente u = utenteDAO.selectByNick(nick);
        int i = 0, j=0;
        String nomi[];
        Messaggio m[] = null;

        if(u==null){
            return null;
        }

        //PRELEVO TUTTI GLI HANDSHAKE INVIATI DALL'UTENTE
        Messaggio hs[] = messaggioDAO.selectMsgBySdrHand(u.getNickName(), "3");

        if(hs == null){
            return null;
        }

        nomi = new String[hs.length];
        for(j=0; j<nomi.length;j++)
            nomi[j]="";
        j = 0;

        //CONTROLLO SE L'HANDSHAKE è STATO RICAMBIATO, SE è STATO RICAMBIATO SALVO IL NOME DELL'UTENTE IN UN ARRAY
        do{
            if(messaggioDAO.selectMsgBySdrRcvHand(hs[i].getReceiver(), u.getNickName(), "3")!= null ||
                    messaggioDAO.selectMsgBySdrRcvHand(hs[i].getReceiver(), u.getNickName(), "4")!= null     ) {
                nomi[j]=hs[i].getReceiver();
                j++;
            }
            i++;
        }while(i<hs.length);

        //PRELEVO ORA DAL DB I MESSAGGI DI HANDSHAKE COMPLETATI
        m = new Messaggio[j];
        for(i=0;i<m.length;i++){
            m[i] = messaggioDAO.selectMsgBySdrRcvHand(u.getNickName(), nomi[i], "3")[0];
        }

        return m;
    }

    @Override
    public Messaggio[] selectMyJourneys(String nick) {
        //PRELEVO L'UTENTE DAL DB
        Utente u = utenteDAO.selectByNick(nick);
        int i = 0, j=0;
        String nomi[];
        Messaggio m[] = null;

        if(u==null){
            return null;
        }

        //PRELEVO TUTTI I FEEDBACK INVIATI DALL'UTENTE
        m = messaggioDAO.selectMsgBySdrHand(u.getNickName(), "4");

        if(m!=null){
            return m;
        }
        else{
            return null;
        }
    }
}
