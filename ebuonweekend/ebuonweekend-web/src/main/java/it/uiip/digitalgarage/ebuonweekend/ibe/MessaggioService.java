package it.uiip.digitalgarage.ebuonweekend.ibe;

import it.uiip.digitalgarage.ebuonweekend.entity.Abitazione;
import it.uiip.digitalgarage.ebuonweekend.entity.Messaggio;
import it.uiip.digitalgarage.ebuonweekend.entity.Utente;

public interface MessaggioService {

    boolean insert(Messaggio m);
    boolean insertHand(String sdr, String rcv, String idHouseRcv);
    boolean insertHandshake(String sdr, String rcv);
    int[] setRead(String sdr, String rcv);
    Utente[] selectSendersByRcv(String rcv);
    Messaggio[] selectMsgBySdrRcv(String sdr, String rcv);
    Messaggio[] selectMsgByRcvHand(String rcv, String hand);
    String countHands(String user);
    Messaggio[] selectMsgByReceiver(String rcv);
    Messaggio selectById(Long index);
    Abitazione[] selectHandedHouses(String sdr, String rcv);

}
