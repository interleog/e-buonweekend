package it.uiip.digitalgarage.ebuonweekend.controller;

import com.pusher.rest.Pusher;
import it.uiip.digitalgarage.ebuonweekend.entity.*;
import it.uiip.digitalgarage.ebuonweekend.ibe.MailService;
import it.uiip.digitalgarage.ebuonweekend.ibe.MessaggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.net.www.content.text.Generic;

import java.time.LocalDate;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;


@RestController
public class MessaggioController{

    @Autowired
    MessaggioService messaggioService;
    @Autowired
    MailService mailService;

    private final AtomicLong counterPosta = new AtomicLong();
    private final AtomicLong counterMail = new AtomicLong();


    @RequestMapping("/getSenders")
    public Utente[] getSenders(@RequestParam(value = "receiver", defaultValue = "NO-RECEIVER") String rcv) {
        return messaggioService.selectSendersByRcv(rcv);
    }

    @RequestMapping("/getMessages")
    public Messaggio[] getMessages(@RequestParam(value = "sender", defaultValue = "NO-SENDER") String sdr,
                                   @RequestParam(value = "receiver", defaultValue = "NO-RECEIVER") String rcv) {

        int a[] = null;

        a = messaggioService.setRead(rcv, sdr);

        /*System.out.println("ID messaggi checkati a true:");
        for(int i=0;i<a.length;i++)
            System.out.println("ID: "+a[i]);*/

        if(a != null){
            Pusher pusher = new Pusher("302271", "59110edd1bd1ebce97dd", "eedf99b317809ce0d977");
            pusher.setCluster("eu");
            pusher.setEncrypted(true);

            pusher.trigger("handNumber_" + sdr, "removeNotification", a);
        }

        return messaggioService.selectMsgBySdrRcv(sdr, rcv);
    }

    @RequestMapping("/searchMsg")
    public Messaggio[] searchMsg(@RequestParam(value = "receiver", defaultValue = "NO-RECEIVER") String rcv,
                                 @RequestParam(value = "isHand", defaultValue = "NO-HAND") String isHand) {

        return messaggioService.selectMsgByRcvHand(rcv, isHand);
    }

    @RequestMapping("/getNotifications")
    public GenericReturn<Messaggio[]> getNotifications(@RequestParam(value = "receiver", defaultValue = "NO-RECEIVER") String rcv) {

        return new GenericReturn<>(messaggioService.selectMsgByReceiver(rcv)) ;
    }


    @RequestMapping("/sendMessage")
    public GenericReturn<Boolean> sendMessage(@RequestParam(value = "sender", defaultValue = "NO-SENDER") String sender,
                                 @RequestParam(value = "receiver", defaultValue = "NO-RECEIVER") String receiver,
                                 @RequestParam(value = "message", defaultValue = "NO-RECEIVER") String message,
                                 @RequestParam(value = "isHand", defaultValue = "0") Integer isHand) {

        Messaggio messaggio = new Messaggio(counterPosta.incrementAndGet(), sender, receiver, message, isHand, LocalDate.now(), "",false);

       Boolean messageSent = messaggioService.insert(messaggio);
       if(messageSent){
           Pusher pusher = new Pusher("302271", "59110edd1bd1ebce97dd", "eedf99b317809ce0d977");
           pusher.setCluster("eu");
           pusher.setEncrypted(true);
           /*String generic = messaggioService.countHands(receiver);

           pusher.trigger("handNumber_" + receiver, "updated", String.valueOf(Integer.parseInt(generic) + 1));
            */
           pusher.trigger("handNumber_" + receiver, "newNotification", messaggio);
       }

        return new GenericReturn<>(messageSent);
    }



    @RequestMapping("/sendHand")
    public GenericReturn<Boolean> sendHand(@RequestParam(value = "sender", defaultValue = "NO-SENDER") String sender,
                                           @RequestParam(value = "receiver", defaultValue = "NO-RECEIVER") String receiver,
                                           @RequestParam(value = "idHouseRcv", defaultValue ="NO-ID" ) String idHouseRcv) {

        if(!sender.equals("NO-SENDER") && !receiver.equals("NO-RECEIVER") && !idHouseRcv.equals("NO-ID") && !sender.equals(receiver)) {

            Boolean handSuccess = messaggioService.insertHand(sender, receiver, idHouseRcv);

            if( handSuccess) {
                Pusher pusher = new Pusher("302271", "59110edd1bd1ebce97dd", "eedf99b317809ce0d977");
                pusher.setCluster("eu");
                pusher.setEncrypted(true);
                //String generic = messaggioService.countHands(receiver);

               // pusher.trigger("handNumber_" + receiver, "updated", String.valueOf(Integer.parseInt(generic) + 1));
                Messaggio mess = new Messaggio(0, sender, receiver,  "Ciao, " + receiver + "! " + sender + " ha inviato un hand in una delle tue case!", 1, LocalDate.now(), "null",false);
                pusher.trigger("handNumber_" + receiver, "newNotification", mess);

            }
            return new GenericReturn<Boolean>(handSuccess);
        }
        else
            return new GenericReturn<>(false);
    }

    @RequestMapping("/sendHandshake")
    public GenericReturn<Boolean> sendHandshake(@RequestParam(value = "sender", defaultValue = "NO-SENDER") String sender,
                                                @RequestParam(value = "receiver", defaultValue = "NO-RECEIVER") String receiver) {
        //INSERISCO NEL DB UN NUOVO MESSAGGIO DI HANDSHAKE CON isHand=3

        if(!sender.equals("NO-SENDER") && !receiver.equals("NO-RECEIVER") && !sender.equals(receiver)) {

            Boolean handshakeSuccess = messaggioService.insertHandshake(sender, receiver);

            if( handshakeSuccess) {
                Pusher pusher = new Pusher("302271", "59110edd1bd1ebce97dd", "eedf99b317809ce0d977");
                pusher.setCluster("eu");
                pusher.setEncrypted(true);
                //String generic = messaggioService.countHands(receiver);

                // pusher.trigger("handNumber_" + receiver, "updated", String.valueOf(Integer.parseInt(generic) + 1));
                Messaggio mess = new Messaggio(0, sender, receiver,  "Handshake complete!", 3, LocalDate.now(), "null",false);
                pusher.trigger("handNumber_" + receiver, "newNotification", mess);

            }
            return new GenericReturn<>(handshakeSuccess);
        }
        else
            return new GenericReturn<>(false);
    }


    @RequestMapping("/newMail")
    public Mail newMail(@RequestParam(value = "sender", defaultValue = "@unregistered") String sender,
                        @RequestParam(value = "object", defaultValue = "@unregistered") String object,
                        @RequestParam(value = "text", defaultValue = "@unregistered") String text) {
        //http://localhost:8080/newMail?sender=luca&object=ciao&text=blal

        if (!sender.equals("@unregistered") && !object.equals("@unregistered") && !text.equals("@unregistered")) {
            Mail mail = new Mail(counterMail.incrementAndGet(), sender, object, text, LocalDate.now());
            mailService.insert(mail);
            return mail;
        }
        else{
            return null;
        }
    }



    //METODO PER CONTARE GLI HAND DI UN UTENTE
    @RequestMapping("/countHands")
    public GenericReturn<String> countHands(@RequestParam(value = "user", defaultValue = "undefined") String user){
        if(!user.equals("undefined")) {


            String generic = messaggioService.countHands(user);
          //  pusher.trigger("my-channel", "my-event", Collections.singletonMap("message", generic));
            return new GenericReturn<>(generic);
        }
        else
            return new GenericReturn<>(null);
    }

    @RequestMapping("/getHandedHouses")
    public GenericReturn<Abitazione[]> getHandedHouses(@RequestParam(value= "sender", defaultValue = "undefined") String sdr,
                                                       @RequestParam(value = "receiver", defaultValue = "undefined") String rcv){
        if(!sdr.equals("undefined") && !rcv.equals("undefined") && !sdr.equals(rcv)){
            Abitazione a[] = messaggioService.selectHandedHouses(sdr, rcv);
            if(a != null){
                return new GenericReturn<>(a);
            }
            else {
                return new GenericReturn<>(null);
            }
        }
        else{
            return new GenericReturn<>(null);
        }

    }

}