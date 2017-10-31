package it.uiip.digitalgarage.ebuonweekend.be;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import it.uiip.digitalgarage.ebuonweekend.entity.Viaggio;
import it.uiip.digitalgarage.ebuonweekend.ibe.ViaggioService;
import it.uiip.digitalgarage.ebuonweekend.idao.MessaggioDAO;
import it.uiip.digitalgarage.ebuonweekend.idao.ViaggioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ViaggioServiceImpl implements ViaggioService {

    @Autowired
    ViaggioDAO viaggioDAO;

    @Autowired
    MessaggioDAO messaggioDAO;

    @Override
    public boolean insert(Viaggio v) {
        if(viaggioDAO.selectByUserIDHouseDate(v.getUser(), v.getIdHouse(), v.getJourneyDate()) == null) {

            //CONTROLLO SE L'ALTRO UTENTE HA GIà INSERITO IL JOURNEY (CIOè HA GIà SELEZIONATO DATA ED INVIATO HANDSHAKE)
            Viaggio viaggio[] = viaggioDAO.selectByUserHouseownerDateIDExchange(v.getHouseOwner(), v.getUser(), v.getJourneyDate(), 0);

            if(viaggio != null){
                //CASO IN CUI HO TROVATO UN JOURNEY IN SOSPESO
                v.setIdExchange(viaggio[0].getId());
                viaggioDAO.insert(v);

                viaggioDAO.updateIDExchange(viaggio[0], (int) v.getId());

                return true;
            }
            else{
                //CASO IN CUI NON HO TROVATO JOURNEY IN SOSPESO
                return viaggioDAO.insert(v);
            }
        }
        else{
            return false;
        }
    }

    @Override
    public boolean deleteByUserHouseownerIDExchange(String user, String houseOwner, int idExchange) {

        //OLTRE A CANCELLARE IL JOURNEY "IN SOSPESO" DEVO ANCHE CANCELLARE IL MESSAGGIO DI HANDSHAKE DAL DB
        if(messaggioDAO.deleteRejectedHandshake(user, houseOwner)) {
            return viaggioDAO.deleteByUserHouseownerIDExchange(user, houseOwner, idExchange);
        }
        else{
            return false;
        }
    }

    @Override
    public Viaggio[] selectJourneysByUserHouseownerIDExchange(String user, String houseOwner, int idExchange) {
        return viaggioDAO.selectByUserHouseownerIDExchange(user, houseOwner, idExchange);
    }

    @Override
    public Viaggio[] selectCompletedJourney(String user) {
        return viaggioDAO.selectCompletedJourney(user);
    }
}
