package it.uiip.digitalgarage.ebuonweekend.ibe;


import it.uiip.digitalgarage.ebuonweekend.entity.Abitazione;
import it.uiip.digitalgarage.ebuonweekend.entity.Wishlist;

public interface WishlistService {

    boolean insert (Wishlist w);
    boolean delete(String nick, long idHouse);
    Abitazione[] selectHousesFromWishlist(String nick);


}
