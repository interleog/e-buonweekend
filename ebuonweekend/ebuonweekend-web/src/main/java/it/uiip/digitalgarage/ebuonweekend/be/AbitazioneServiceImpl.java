package it.uiip.digitalgarage.ebuonweekend.be;


import it.uiip.digitalgarage.ebuonweekend.entity.Abitazione;
import it.uiip.digitalgarage.ebuonweekend.ibe.AbitazioneService;
import it.uiip.digitalgarage.ebuonweekend.idao.AbitazioneDAO;
import it.uiip.digitalgarage.ebuonweekend.idao.UtenteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AbitazioneServiceImpl implements AbitazioneService{

    @Autowired
    AbitazioneDAO abitazioneDAO;

    @Autowired
    UtenteDAO utenteDAO;

    @Override
    public boolean insert(Abitazione a) {
        return abitazioneDAO.insert(a);
    }

    @Override
    public boolean update(Abitazione a){
        return abitazioneDAO.update(a);
    }

    @Override
    public boolean delete(Abitazione a) { return abitazioneDAO.delete(a); }

    @Override
    public Abitazione[] selectAll(){
        return abitazioneDAO.selectAll();
    }

    @Override
    public Abitazione[] selectByCity(String city) {
        return abitazioneDAO.selectByCity(city);
    }

    @Override
    public Abitazione[] selectByLocation(String location){ return abitazioneDAO.selectByLocation(location); }

    @Override
    public Abitazione[] selectByDate(String date) { return abitazioneDAO.selectByDate(date); }

    @Override
    public Abitazione[] selectByCityLocation(String city, String location) { return abitazioneDAO.selectByCityLocation(city, location); }

    @Override
    public Abitazione[] selectByCityDate(String city, String date) { return abitazioneDAO.selectByCityDate(city, date); }

    @Override
    public Abitazione[] selectByLocationDate(String location, String date) { return abitazioneDAO.selectByLocationDate(location, date); }

    @Override
    public Abitazione[] selectByCityLocationDate(String city, String location, String date) { return abitazioneDAO.selectByCityLocationDate(city, location, date); }

    @Override
    public Abitazione[] selectByUser(String user) {
        return abitazioneDAO.selectByUser(user);
    }

    @Override
    public Abitazione selectById(String id) {
        return abitazioneDAO.selectById(id);
    }

    @Override
    public Abitazione[] selectBestHouses(){
        Abitazione a[] = new Abitazione[5];
        int i = 0;
        String bestUsers[] = utenteDAO.selectBestUsers();

        if(bestUsers == null){
            return a;
        }
        do{
            System.out.println((abitazioneDAO.selectByUser(bestUsers[i]))[0].getUtente());
            a[i]=(abitazioneDAO.selectByUser(bestUsers[i]))[0];
            i++;
        }while(i<a.length);

        return a;
    }

}
