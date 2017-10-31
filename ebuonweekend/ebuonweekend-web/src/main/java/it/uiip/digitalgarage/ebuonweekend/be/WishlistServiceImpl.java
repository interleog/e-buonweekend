package it.uiip.digitalgarage.ebuonweekend.be;


import it.uiip.digitalgarage.ebuonweekend.entity.Abitazione;
import it.uiip.digitalgarage.ebuonweekend.entity.Wishlist;
import it.uiip.digitalgarage.ebuonweekend.ibe.WishlistService;
import it.uiip.digitalgarage.ebuonweekend.idao.AbitazioneDAO;
import it.uiip.digitalgarage.ebuonweekend.idao.WishlistDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishlistServiceImpl implements WishlistService{

    @Autowired
    WishlistDAO wishlistDAO;

    @Autowired
    AbitazioneDAO abitazioneDAO;

    @Override
    public boolean insert(Wishlist w) {
        //DEVO PRIMA CONTROLLARE SE L'UTENTE HA GIà INSERITO QUELLA CASA NELLA SUA WISHLIST
        if(wishlistDAO.selectByNickIdHouse(w.getUser(), String.valueOf(w.getIdHouse())) != null ||
                abitazioneDAO.selectById(String.valueOf(w.getIdHouse())).getUtente().equals(w.getUser())){
            System.out.println("Impossibile inserire nella wishlist!");
            return false;
        }
        
        return wishlistDAO.insert(w);
    }

    @Override
    public boolean delete(String nick, long idHouse) {
        return wishlistDAO.delete(nick, idHouse);
    }

    @Override
    public Abitazione[] selectHousesFromWishlist(String nick) {
        //QUI DEVO PRENDERE GLI ID DELLE CASE NELLA WISHLIST DELL'UTENTE E POI PRELEVARE LE VARIE CASE DAL DB TRAMITE QUERY
        Wishlist w[] = wishlistDAO.selectByNick(nick);
        if(w == null){
            System.out.println("Nessuna casa presente nella wishlist di "+nick);
            return null;
        }
        Abitazione a[]= new Abitazione[w.length];

        for(int i=0;i<a.length;i++){
            a[i]=abitazioneDAO.selectById(String.valueOf(w[i].getIdHouse()));
        }

        return a;
    }


}
