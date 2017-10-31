package it.uiip.digitalgarage.ebuonweekend.controller;

import it.uiip.digitalgarage.ebuonweekend.entity.GenericReturn;
import it.uiip.digitalgarage.ebuonweekend.entity.Viaggio;
import it.uiip.digitalgarage.ebuonweekend.ibe.MessaggioService;
import it.uiip.digitalgarage.ebuonweekend.ibe.ViaggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class ViaggioController {

    @Autowired
    ViaggioService viaggioService;

    @Autowired
    MessaggioService messaggioService;

    @RequestMapping("/insertJourney")
    public GenericReturn<Boolean> insertJourney(@RequestParam(value = "user", defaultValue = "undefined") String user,
                                                @RequestParam(value = "houseOwner", defaultValue = "undefined") String houseOwner,
                                                @RequestParam(value = "idHouse", defaultValue = "undefined") String idHouse,
                                                @RequestParam(value = "journeyDate", defaultValue = "undefined") String journeyDate){

        if(!user.equals("undefined") && !houseOwner.equals("undefined") && !idHouse.equals("undefined") && !journeyDate.equals("undefined") && !user.equals(houseOwner)){


            /*String newFormat = "dd-MM-yyyy";
            String oldFormat = "yyyy-MM-dd";
            String data = null;
            SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
            try{
                Date d = sdf.parse(journeyDate);
                sdf.applyPattern(newFormat);
                data = sdf.format(d);
            }catch(Exception e){
                return new GenericReturn<>(false);
            }
            Viaggio v = new Viaggio(0, user, houseOwner, idHouse, data, 0);*/


            Viaggio v = new Viaggio(0, user, houseOwner, idHouse, journeyDate, 0);

            if(messaggioService.insertHandshake(user, houseOwner)){
                //se l'inserimento dell'handshake è andato a buon fine, inserisco journey nel db

                return new GenericReturn<>(viaggioService.insert(v));

            }
            else return new GenericReturn<>(false);
        }
        else{
            return new GenericReturn<>(false);
        }

    }

    @RequestMapping("/getSuspendedJourneys")
    public GenericReturn<Viaggio> getSuspendedJourneys(@RequestParam(value = "user", defaultValue = "undefined") String user,
                                                       @RequestParam(value = "houseOwner", defaultValue = "undefined") String houseOwner){

        Viaggio v[] = viaggioService.selectJourneysByUserHouseownerIDExchange(user, houseOwner, 0);
        if(v!=null)
            return new GenericReturn<>(v[0]);
        else
            return new GenericReturn<>(null);

    }

    @RequestMapping("/deleteSuspendedJourney")
    public GenericReturn<Boolean> deleteSuspendedJourney(@RequestParam(value = "user", defaultValue = "undefined") String user,
                                                         @RequestParam(value = "houseOwner", defaultValue = "undefined") String houseOwner){

        return new GenericReturn<>(viaggioService.deleteByUserHouseownerIDExchange(user, houseOwner, 0));
    }

    @RequestMapping("/getCompletedJourney")
    public GenericReturn<Viaggio[]> getCompletedJourney(@RequestParam(value = "user", defaultValue = "undefined") String user){

        if(!user.equals("undefined")){
            return new GenericReturn<>(viaggioService.selectCompletedJourney(user));

        }
        else return new GenericReturn<>(null);
    }

}
