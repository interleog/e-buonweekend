package it.uiip.digitalgarage.ebuonweekend.be;

import com.pusher.rest.Pusher;
import it.uiip.digitalgarage.ebuonweekend.entity.Abitazione;
import it.uiip.digitalgarage.ebuonweekend.entity.Messaggio;
import it.uiip.digitalgarage.ebuonweekend.entity.Utente;
import it.uiip.digitalgarage.ebuonweekend.ibe.MessaggioService;
import it.uiip.digitalgarage.ebuonweekend.idao.AbitazioneDAO;
import it.uiip.digitalgarage.ebuonweekend.idao.MessaggioDAO;
import it.uiip.digitalgarage.ebuonweekend.idao.UtenteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MessaggioServiceImpl implements MessaggioService{

    @Autowired
    MessaggioDAO messaggioDAO;

    @Autowired
    AbitazioneDAO abitazioneDAO;

    @Autowired
    UtenteDAO utenteDAO;

    @Override
    public boolean insert(Messaggio m){
        return messaggioDAO.insert(m);
    }

    @Override
    public Utente[] selectSendersByRcv(String rcv) {
        //PRENDO TUTTI I NICKNAME DEI SENDERS
        String senders[] = messaggioDAO.selectSendersByRcv(rcv);

        Utente u[] = new Utente[senders.length];
        //PRELEVO I SENDERS DAL DB
        for(int i=0;i<u.length;i++){
            u[i] = utenteDAO.selectByNick(senders[i]);
        }

        return u;
    }

    @Override
    public int[] setRead(String sdr, String rcv) { return messaggioDAO.setRead(sdr, rcv);  }

    @Override
    public Messaggio[] selectMsgBySdrRcv(String sdr, String rcv) {

        return messaggioDAO.selectMsgBySdrRcv(sdr, rcv);
    }

    @Override
    public Messaggio[] selectMsgByRcvHand(String rcv, String hand){
        return messaggioDAO.selectMsgByRcvHand(rcv, hand);
    }

    @Override
    public Messaggio[] selectMsgByReceiver(String rcv){ return messaggioDAO.selectMsgByReceiver(rcv);   }


    @Override
    public boolean insertHand(String sdr, String rcv, String idHouseRcv){
        Messaggio m[]=null;
        Messaggio m2[] = null;
        Messaggio m_1[]=null;
        Messaggio m_2[]=null;
        Abitazione house_sender[];
        String msg="null";
        int count = 0;

        //CONTROLLO SE L'HAND è GIà STATO ESEGUITO (HAND=1 O HAND=2)
        //SE C'è ED è PIù VECCHIO DI 7 GIORNI LO AGGIORNO (setto isHand=0) E PROSEGUO
        //ALTRIMENTI NON FACCIO METTERE HAND
        m_1 = messaggioDAO.selectMsgBySdrRcvHandHouse(sdr, rcv, "1", idHouseRcv);
        m_2 = messaggioDAO.selectMsgBySdrRcvHandHouse(sdr, rcv, "2", idHouseRcv);
        if(m_1!=null || m_2!=null){

            if(m_1!=null){
                for(int i=0;i<m_1.length;i++){
                    if(m_1[i].getDataMessage().plusDays(7).isBefore(LocalDate.now()) ){
                        messaggioDAO.archiveHand(m_1[i]);
                        count++;
                    }
                }
                if(count < m_1.length){
                    System.out.println("Hand già eseguito!");
                    return false;
                }
            }
            count=0;
            if(m_2!=null){
                for(int i=0;i<m_2.length;i++){
                    if(m_2[i].getDataMessage().plusDays(7).isBefore(LocalDate.now()) ){
                        messaggioDAO.archiveHand(m_2[i]);
                        count++;
                    }
                }
                if(count < m_2.length){
                    System.out.println("Hand già eseguito!");
                    return false;
                }
            }
        }

        //========================== ADDED ON 24/01/17 =============================================
        //DEVO POTER METTERE HAND SOLO NEL CASO IN CUI I PROPRIETARI VOGLIONO ANDARE NELLA MIA CASA

        //PRELEVO IL RECEIVER DAL DB E PRENDO TUTTI I SUOI TAG (statoUtente)
        Utente obj_rcv = utenteDAO.selectByNick(rcv);
        String tag_rcv[] = new String[3];
        if(obj_rcv != null)
            tag_rcv = obj_rcv.getStatoUtente().split(";", 3);
        else
            tag_rcv = null;


        //PRENDO TUTTE LE ABITAZIONI DEL SENDER
        //house_sender = new Abitazione[abitazioneDAO.selectByUser(sdr).length];
        house_sender = abitazioneDAO.selectByUser(sdr);
        //=======================================================================================

        //OONTROLLO SE L'HAND è RECIPROCO
        m = messaggioDAO.selectMsgBySdrRcvHand(rcv, sdr, "1");
        m2 = messaggioDAO.selectMsgBySdrRcvHand(rcv, sdr, "2");
        if(m!=null || m2!=null){
            //caso in cui l'hand è reciproco
            //m[0]= (messaggioDAO.selectMsgBySdrRcvHand(rcv, sdr, "1"))[0]; //TODO
            System.out.println("Hand reciproco trovato!");

            Abitazione a = abitazioneDAO.selectById(idHouseRcv);
            msg = "Ottimo! Anche " + sdr + " ha messo un HAND nella tua casa di "+a.getCity()+"! Contattalo per concordare le date del vostro viaggio!";
            Messaggio mess = new Messaggio(0, sdr, rcv, msg, 2, LocalDate.now(), "null",false);
            mess.setIdHouseRcv(Long.valueOf(idHouseRcv));

            if(messaggioDAO.insert(mess)){
                if(m != null) {
                    //ora aggiorno il primo messaggio di hand mettendolo a 2
                    messaggioDAO.updateHand(m[0]);   //TODO
                    System.out.println("Hand reciproco aggiornato ed inserito nel DB!");
                }
                return true;
            }else{
                System.out.println("Problema inserimento hand nel DB!");
                return false;
            }


        }
        else {
            //caso in cui l'hand non è reciproco

            //========================ADDED 24/01/17===========================================
            boolean allowed = false;
            //CONTROLLO SE IL SENDER HA UNA CASA DOVE VUOLE ANDARE IL RECEIVER
            if(tag_rcv!=null) {
                for (int i = 0; i < tag_rcv.length; i++) {
                    for (int j = 0; j < house_sender.length; j++) {
                        if (tag_rcv[i].equals(house_sender[j].getTags())) {
                            allowed = true;
                        }
                        if (allowed) break;
                    }
                    if (allowed) break;
                }
            }
            if (allowed) {
                //==================================================================================
                Abitazione a = abitazioneDAO.selectById(idHouseRcv);
                msg = "Ciao, " + rcv + "! " + sdr + " ha inviato un HAND nella tua casa di "+a.getCity()+"!";

                Messaggio mess = new Messaggio(0, sdr, rcv, msg, 1, LocalDate.now(), "null",false);
                mess.setIdHouseRcv(Long.valueOf(idHouseRcv));

                if (messaggioDAO.insert(mess)) {
                    System.out.println("Hand inserito correttamente!");
                    return true;
                } else {
                    System.out.println("Problea inserimento hand!");
                    return false;
                }

            }else {
                System.out.println("IMPOSSIBILE INVIARE HAND: tags non compatibili!");
                return false;
            }
        }

    }

    @Override
    public boolean insertHandshake(String sdr, String rcv) {
        //INSERISCO NEL DB UN NUOVO MESSAGGIO DI HANDSHAKE CON isHand=3
        Messaggio m[] = null;
        String msg = null;
        int count = 0;

        //CONTROLLO SE L'HANDSHAKE è GIà STATO FATTO
        //SE è GIà STATO FATTO ED è PIù VECCHIO DI 7 GIORNI LO AGGIORNO (setto isHand=0) E PROSEGUO
        //ALTRIMENTI NON FACCIO METTERE HANDSHAKE
        m = messaggioDAO.selectMsgBySdrRcvHand(sdr, rcv, "3");
        if(m != null){
            for(int i=0; i<m.length;i++){
                if(m[i].getDataMessage().plusDays(7).isBefore(LocalDate.now()) ){
                    messaggioDAO.archiveHand(m[i]);
                    count++;
                }
            }
            if(count < m.length) {
                System.out.println("Handshake già eseguito!");
                return false;
            }
        }

        //CONTROLLO SE L'HANDSHAKE è RECIPROCO
        if(messaggioDAO.selectMsgBySdrRcvHand(rcv, sdr, "3") != null){
            //caso in cui l'handshake è reciproco
            msg = "GREAT NEWS! Anche " + sdr + " ha fatto HANDSHAKE! Adesso potete scambiarvi la casa!";

        }else{
            //caso in cui l'handshake non è reciproco
            msg = "Ciao, " + rcv + "! " + sdr + " ha appeana fatto HANDSHAKE!\n" +
                    "Clicca anche tu sul tasto HANDSHAKE per completare lo scambio!";

        }

        Messaggio mess = new Messaggio(0, sdr, rcv, msg, 3, LocalDate.now(), "null",false);

        if(messaggioDAO.insert(mess)){
            System.out.println("Handshake inserito correttamente nel DB!");
            return true;
        }else{
            System.out.println("Problema inserimento handshake nel DB!");
            return false;
        }
    }


    @Override
    public Messaggio selectById(Long index){
        return messaggioDAO.selectById(index);
    }

    @Override
    public String countHands(String user) {

        Messaggio m[]=null;
        m=messaggioDAO.selectMsgByRcvHand(user, "1");

        if(m!=null){
            return String.valueOf(m.length);
        }
        else return "0";
    }

    @Override
    public Abitazione[] selectHandedHouses(String sdr, String rcv) {
        Abitazione a[] = null;

        //PRENDO TUTTI I MESSAGGI DOVE HAND=2
        Messaggio m[] = messaggioDAO.selectMsgBySdrRcvHand(sdr, rcv, "2");

        if(m.length>0) {
            a = new Abitazione[m.length];
            for (int i = 0; i < a.length; i++) {
                a[i] = abitazioneDAO.selectById(String.valueOf(m[i].getIdHouseRcv()));
            }
        }

        return a;


    }
}
