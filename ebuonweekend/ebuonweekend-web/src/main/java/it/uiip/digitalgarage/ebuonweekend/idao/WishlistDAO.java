package it.uiip.digitalgarage.ebuonweekend.idao;

import it.uiip.digitalgarage.ebuonweekend.entity.Wishlist;

public interface WishlistDAO {

    boolean insert (Wishlist w);
    boolean delete (String nick, long idHouse);
    Wishlist[] selectByNick(String nick);
    Wishlist selectByNickIdHouse(String nick, String idHouse);

}
