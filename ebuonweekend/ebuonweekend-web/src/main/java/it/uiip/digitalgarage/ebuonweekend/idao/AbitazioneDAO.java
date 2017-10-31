package it.uiip.digitalgarage.ebuonweekend.idao;

import it.uiip.digitalgarage.ebuonweekend.entity.Abitazione;

public interface AbitazioneDAO {

    boolean insert(Abitazione a);
    boolean update(Abitazione a);
    boolean delete(Abitazione a);
    Abitazione[] selectAll();
    Abitazione[] selectByCity(String city);
    Abitazione[] selectByLocation(String location);
    Abitazione[] selectByDate(String date);
    Abitazione[] selectByCityDate(String city, String date);
    Abitazione[] selectByCityLocation(String city, String location);
    Abitazione[] selectByLocationDate(String location, String date);
    Abitazione[] selectByCityLocationDate(String city, String location, String date);
    Abitazione[] selectByUser(String user);
    Abitazione selectById(String id);
}
