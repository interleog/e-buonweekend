package it.uiip.digitalgarage.ebuonweekend.controller;

import it.uiip.digitalgarage.ebuonweekend.entity.GenericReturn;
import it.uiip.digitalgarage.ebuonweekend.entity.Abitazione;
import it.uiip.digitalgarage.ebuonweekend.entity.Wishlist;
import it.uiip.digitalgarage.ebuonweekend.ibe.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WishlistController {

    @Autowired
    WishlistService wishlistService;

    @RequestMapping("/addWishlist")
    public GenericReturn<Boolean> addWishlist(@RequestParam(value = "nick") String nick,
                                     @RequestParam(value = "idHouse") String idHouse){
        //addWishlist?nick=leo&idHouse=43

        Wishlist w = new Wishlist(0, nick, Long.valueOf(idHouse));


        return new GenericReturn<Boolean>(wishlistService.insert(w));
    }

    @RequestMapping("/showMyWishlist")
    public Abitazione[] showWishlist(@RequestParam(value = "nick", defaultValue = "undefined") String nick){
        //showMyWishlist?nick=leo

        if(!nick.equals("undefined"))
            return wishlistService.selectHousesFromWishlist(nick);
        else
            return null;
    }

    @RequestMapping("/deleteWishlist")
    public GenericReturn<Boolean> deleteWishlist(@RequestParam(value = "nick", defaultValue = "undefined") String nick,
                                                 @RequestParam(value = "idHouse", defaultValue = "undefined") String idHouse){
        if(!nick.equals("undefined")){
            return new GenericReturn<>(wishlistService.delete(nick, Long.parseLong(idHouse)));
        }
        else{
            return new GenericReturn<>(false);
        }

    }

}
