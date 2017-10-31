package it.uiip.digitalgarage.ebuonweekend.idao;

import it.uiip.digitalgarage.ebuonweekend.entity.Messaggio;

public interface MessaggioDAO {

    boolean insert(Messaggio m);
    boolean updateHand(Messaggio m);
    boolean archiveHand(Messaggio m);
    boolean updateHandshake(Messaggio m, String feedback);
    boolean deleteRejectedHandshake(String sdr, String rcv);
    String[] selectSendersByRcv(String rcv);
    Messaggio[] selectMsgBySdrRcv(String sdr, String rcv);
    Messaggio[] selectMsgBySdrRcvHand(String sdr, String rcv, String hand);
    Messaggio[] selectMsgBySdrRcvHandHouse(String sdr, String rcv, String hand, String idHouseRcv);
    Messaggio[] selectMsgByRcvHand(String rcv, String hand);
    Messaggio[] selectMsgBySdrHand(String sdr, String hand);
    Messaggio[] selectMsgByReceiver(String rcv);
    int[] setRead(String sender, String rcv);
    Messaggio selectById(Long index);
}
