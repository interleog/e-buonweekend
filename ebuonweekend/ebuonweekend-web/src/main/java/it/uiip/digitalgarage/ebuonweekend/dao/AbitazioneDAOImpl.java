package it.uiip.digitalgarage.ebuonweekend.dao;

import com.mysql.jdbc.Statement;
import it.uiip.digitalgarage.ebuonweekend.entity.Abitazione;
import it.uiip.digitalgarage.ebuonweekend.idao.AbitazioneDAO;
import it.uiip.digitalgarage.ebuonweekend.utils.DateUtil;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.LocalDate;

import static it.uiip.digitalgarage.ebuonweekend.dao.DBController.*;

@Component
public class AbitazioneDAOImpl implements AbitazioneDAO {

    private static final String INSERT = "INSERT INTO abitazioni (city, address, zip, mq, utente, addedSince, pathImg, tags, descr, isAvailable, availableDates) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE abitazioni SET city=?, address=?, zip=? , mq=?, pathimg=?, tags=?, descr=?, isAvailable=?, availableDates=? WHERE id=?";
    private static final String DELETE = "DELETE FROM abitazioni WHERE id=?";
    private static final String SELECT_ALL="SELECT abitazioni.*, utenti.profilePicture, utenti.feedValue FROM (buonweekend.abitazioni JOIN buonweekend.utenti ON abitazioni.utente = utenti.nickName) WHERE isAvailable = 'true'";
    private static final String SELECT_BY_CITY="SELECT abitazioni.*, utenti.profilePicture, utenti.feedValue FROM (buonweekend.abitazioni JOIN buonweekend.utenti ON abitazioni.utente = utenti.nickName) WHERE isAvailable = 'true' AND city=?";
    private static final String SELECT_BY_DATE = "SELECT abitazioni.*, utenti.profilePicture, utenti.feedValue FROM (buonweekend.abitazioni JOIN buonweekend.utenti ON abitazioni.utente = utenti.nickName) WHERE isAvailable = 'true' AND availableDates LIKE ?";
    private static final String SELECT_BY_LOCATION = "SELECT abitazioni.*, utenti.profilePicture, utenti.feedValue FROM (buonweekend.abitazioni JOIN buonweekend.utenti ON abitazioni.utente = utenti.nickName) WHERE isAvailable = 'true' AND tags=?";
    private static final String SELECT_BY_CITY_DATE= "SELECT abitazioni.*, utenti.profilePicture, utenti.feedValue FROM (buonweekend.abitazioni JOIN buonweekend.utenti ON abitazioni.utente = utenti.nickName) WHERE isAvailable = 'true' AND city=? AND availableDates LIKE ?";
    private static final String SELECT_BY_CITY_LOCATION =  "SELECT abitazioni.*, utenti.profilePicture, utenti.feedValue FROM (buonweekend.abitazioni JOIN buonweekend.utenti ON abitazioni.utente = utenti.nickName) WHERE isAvailable = 'true' AND city=? AND tags=?";
    private static final String SELECT_BY_DATE_LOCATION = "SELECT abitazioni.*, utenti.profilePicture, utenti.feedValue FROM (buonweekend.abitazioni JOIN buonweekend.utenti ON abitazioni.utente = utenti.nickName) WHERE isAvailable = 'true' AND availableDates LIKE ? AND tags=?";
    private static final String SELECT_BY_CITY_DATE_LOCATION = "SELECT abitazioni.*, utenti.profilePicture, utenti.feedValue FROM (buonweekend.abitazioni JOIN buonweekend.utenti ON abitazioni.utente = utenti.nickName) WHERE isAvailable = 'true' AND city=? AND tags=? AND availableDates LIKE ?";
    private static final String SELECT_BY_USER="SELECT abitazioni.*, utenti.profilePicture, utenti.feedValue FROM (buonweekend.abitazioni JOIN buonweekend.utenti ON abitazioni.utente = utenti.nickName) WHERE abitazioni.utente=?";
    private static final String SELECT_BY_ID="SELECT abitazioni.*, utenti.profilePicture, utenti.feedValue FROM (buonweekend.abitazioni JOIN buonweekend.utenti ON abitazioni.utente = utenti.nickName) WHERE abitazioni.id=?";

    @Override
    public boolean insert(Abitazione a){
        try {
            if(!connectDB(INSERT))
                return false;
            else {
                stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, a.getCity());
                stmt.setString(2, a.getAddress());
                stmt.setString(3, a.getZip());
                stmt.setString(4, a.getMq());
                stmt.setString(5, a.getUtente());
                stmt.setString(6, DateUtil.format(a.getAddedSince()));
                stmt.setString(7, a.getPathimg());
                stmt.setString(8, a.getTags());
                stmt.setString(9, a.getDescr());
                stmt.setString(10, String.valueOf(a.getIsAvailable()));
                stmt.setString(11, a.getAvailableDates());
                int result = stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();

                if (rs.next()) {
                    a.setId(rs.getInt(1));
                }
                System.out.println("INSERT su abitazioni eseguito!");
                return true;
            }
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }

    }

    @Override
    public boolean update(Abitazione a) {
        try {
            if(!connectDB(UPDATE))
                return false;
            else {
                stmt = conn.prepareStatement(UPDATE, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, a.getCity());
                stmt.setString(2, a.getAddress());
                stmt.setString(3, a.getZip());
                stmt.setString(4, a.getMq());
                stmt.setString(5, a.getPathimg());
                stmt.setString(6, a.getTags());
                stmt.setString(7, a.getDescr());
                stmt.setString(8, String.valueOf(a.getIsAvailable()));
                stmt.setString(9, a.getAvailableDates());
                stmt.setString(10, ""+a.getId());
                int result = stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();

                if (rs.next()) {
                    disconnectDB();
                    System.out.println("UPDATE su abitazioni eseguito!");
                    return true;
                }
                else{
                    disconnectDB();
                    return false;
                }

            }
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
    }

    @Override
    public boolean delete(Abitazione a) {

        boolean returned = false;
        try {

            if (!connectDB(DELETE))
                return false;
            else {
                stmt.setString(1, String.valueOf(a.getId()));
                int rs = stmt.executeUpdate();
                if (rs == 0) {
                    System.out.println("House not found!");
                    disconnectDB();
                    return false;
                } else {
                    System.out.println("House deleted from DB!");
                    returned = true;
                }
            }

        } catch(Exception e){
            System.out.println("Problema nella query: \n" + DELETE + "\n" + e);
            disconnectDB();
            return false;
        } finally{
            //chiusura connessione db
            disconnectDB();
        }
        return  returned;

    }

    public Abitazione[] selectAll(){
        Abitazione a[]=null;
        try {
            if (!connectDB(SELECT_ALL)){
                return a;
            }
            stmt = conn.prepareStatement(SELECT_ALL);
            rs = stmt.executeQuery();

            if(!rs.next()){
                System.out.println("Nessuna casa trovata!");
                disconnectDB();
                return a;
            }
            rs.last();
            a = new Abitazione[rs.getRow()];
            rs.first();
            int i=0;
            do{
                a[i] = new Abitazione(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), LocalDate.parse(rs.getString(7)), rs.getString(8), rs.getString(9), rs.getString(10), rs.getBoolean(11), rs.getString(12));
                a[i].setUserPicture(rs.getString(13));
                a[i].setUserFeed(rs.getString(14));
                i++;
            }while(rs.next());

        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        System.out.println("Query SELECTALL eseguita con successo!");
        return a;

    }

    @Override
    public Abitazione[] selectByCity(String city){
        Abitazione a[]=null;
        try {
            if (!connectDB(SELECT_BY_CITY)){
                return a;
            }
            stmt = conn.prepareStatement(SELECT_BY_CITY);
            stmt.setString(1, city);
            rs = stmt.executeQuery();

            if(!rs.next()){
                System.out.println("Nessuna casa trovata!");
                disconnectDB();
                return a;
            }
            rs.last();
            a = new Abitazione[rs.getRow()];
            rs.first();
            int i=0;
            do{
                a[i] = new Abitazione(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), LocalDate.parse(rs.getString(7)), rs.getString(8), rs.getString(9), rs.getString(10), rs.getBoolean(11), rs.getString(12));
                a[i].setUserPicture(rs.getString(13));
                a[i].setUserFeed(rs.getString(14));
                i++;
            }while(rs.next());

        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        System.out.println("Query eseguita con successo!");
        return a;

    }

    @Override
    public Abitazione[] selectByLocation(String location) {
        //SELECT * FROM abitazioni WHERE isAvailable = 'true' AND tags=?
        Abitazione a[]=null;
        try {
            if (!connectDB(SELECT_BY_LOCATION)){
                return a;
            }
            stmt = conn.prepareStatement(SELECT_BY_LOCATION);
            stmt.setString(1, location);
            rs = stmt.executeQuery();

            if(!rs.next()){
                System.out.println("Nessuna casa trovata!");
                disconnectDB();
                return a;
            }
            rs.last();
            a = new Abitazione[rs.getRow()];
            rs.first();
            int i=0;
            do{
                a[i] = new Abitazione(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), LocalDate.parse(rs.getString(7)), rs.getString(8), rs.getString(9), rs.getString(10), rs.getBoolean(11), rs.getString(12));
                a[i].setUserPicture(rs.getString(13));
                a[i].setUserFeed(rs.getString(14));
                i++;
            }while(rs.next());

        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        System.out.println("Query eseguita con successo!");
        return a;
    }

    @Override
    public Abitazione[] selectByDate(String date) {
        //SELECT * FROM abitazioni WHERE isAvailable = 'true' AND availableDates LIKE ?
        Abitazione a[]=null;
        try {
            if (!connectDB(SELECT_BY_DATE)){
                return a;
            }
            stmt = conn.prepareStatement(SELECT_BY_DATE);
            stmt.setString(1, "%"+date+"%");
            rs = stmt.executeQuery();

            if(!rs.next()){
                System.out.println("Nessuna casa trovata!");
                disconnectDB();
                return a;
            }
            rs.last();
            a = new Abitazione[rs.getRow()];
            rs.first();
            int i=0;
            do{
                a[i] = new Abitazione(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), LocalDate.parse(rs.getString(7)), rs.getString(8), rs.getString(9), rs.getString(10), rs.getBoolean(11), rs.getString(12));
                a[i].setUserPicture(rs.getString(13));
                a[i].setUserFeed(rs.getString(14));
                i++;
            }while(rs.next());

        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        System.out.println("Query eseguita con successo!");
        return a;
    }

    @Override
    public Abitazione[] selectByCityLocation(String city, String location) {
        //SELECT * FROM abitazioni WHERE isAvailable = 'true' AND city=? AND tags=?
        Abitazione a[]=null;
        try {
            if (!connectDB(SELECT_BY_CITY_LOCATION)){
                return a;
            }
            stmt = conn.prepareStatement(SELECT_BY_CITY_LOCATION);
            stmt.setString(1, city);
            stmt.setString(2, location);
            rs = stmt.executeQuery();

            if(!rs.next()){
                System.out.println("Nessuna casa trovata!");
                disconnectDB();
                return a;
            }
            rs.last();
            a = new Abitazione[rs.getRow()];
            rs.first();
            int i=0;
            do{
                a[i] = new Abitazione(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), LocalDate.parse(rs.getString(7)), rs.getString(8), rs.getString(9), rs.getString(10), rs.getBoolean(11), rs.getString(12));
                a[i].setUserPicture(rs.getString(13));
                a[i].setUserFeed(rs.getString(14));
                i++;
            }while(rs.next());

        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        System.out.println("Query eseguita con successo!");
        return a;
    }

    @Override
    public Abitazione[] selectByCityDate(String city, String date) {
        //SELECT * FROM abitazioni WHERE isAvailable = 'true' AND city=? AND availableDates LIKE ?
        Abitazione a[]=null;
        try {
            if (!connectDB(SELECT_BY_CITY_DATE)){
                return a;
            }
            stmt = conn.prepareStatement(SELECT_BY_CITY_DATE);
            stmt.setString(1, city);
            stmt.setString(2, "%"+date+"%");
            rs = stmt.executeQuery();

            if(!rs.next()){
                System.out.println("Nessuna casa trovata!");
                disconnectDB();
                return a;
            }
            rs.last();
            a = new Abitazione[rs.getRow()];
            rs.first();
            int i=0;
            do{
                a[i] = new Abitazione(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), LocalDate.parse(rs.getString(7)), rs.getString(8), rs.getString(9), rs.getString(10), rs.getBoolean(11), rs.getString(12));
                a[i].setUserPicture(rs.getString(13));
                a[i].setUserFeed(rs.getString(14));
                i++;
            }while(rs.next());

        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        System.out.println("Query eseguita con successo!");
        return a;
    }

    @Override
    public Abitazione[] selectByLocationDate(String location, String date) {
        //SELECT * FROM abitazioni WHERE isAvailable = 'true' AND availableDates LIKE ? AND tags=?
        Abitazione a[]=null;
        try {
            if (!connectDB(SELECT_BY_DATE_LOCATION)){
                return a;
            }
            stmt = conn.prepareStatement(SELECT_BY_DATE_LOCATION);
            stmt.setString(1, "%"+date+"%");
            stmt.setString(2, location);
            rs = stmt.executeQuery();

            if(!rs.next()){
                System.out.println("Nessuna casa trovata!");
                disconnectDB();
                return a;
            }
            rs.last();
            a = new Abitazione[rs.getRow()];
            rs.first();
            int i=0;
            do{
                a[i] = new Abitazione(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), LocalDate.parse(rs.getString(7)), rs.getString(8), rs.getString(9), rs.getString(10), rs.getBoolean(11), rs.getString(12));
                a[i].setUserPicture(rs.getString(13));
                a[i].setUserFeed(rs.getString(14));
                i++;
            }while(rs.next());

        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        System.out.println("Query eseguita con successo!");
        return a;
    }

    @Override
    public Abitazione[] selectByCityLocationDate(String city, String location, String date) {
        //SELECT * FROM abitazioni WHERE isAvailable = 'true' AND city=? AND tags=? AND availableDates LIKE ?
        Abitazione a[]=null;
        try {
            if (!connectDB(SELECT_BY_CITY_DATE_LOCATION)){
                return a;
            }
            stmt = conn.prepareStatement(SELECT_BY_CITY_DATE_LOCATION);
            stmt.setString(1, city);
            stmt.setString(2, location);
            stmt.setString(3, "%"+date+"%");
            rs = stmt.executeQuery();

            if(!rs.next()){
                System.out.println("Nessuna casa trovata!");
                disconnectDB();
                return a;
            }
            rs.last();
            a = new Abitazione[rs.getRow()];
            rs.first();
            int i=0;
            do{
                a[i] = new Abitazione(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), LocalDate.parse(rs.getString(7)), rs.getString(8), rs.getString(9), rs.getString(10), rs.getBoolean(11), rs.getString(12));
                a[i].setUserPicture(rs.getString(13));
                a[i].setUserFeed(rs.getString(14));
                i++;
            }while(rs.next());

        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        System.out.println("Query eseguita con successo!");
        return a;
    }

    @Override
    public Abitazione[] selectByUser(String user){
        Abitazione a[]=null;
        try {
            if (!connectDB(SELECT_BY_USER)){
                return a;
            }
            stmt = conn.prepareStatement(SELECT_BY_USER);
            stmt.setString(1, user);
            rs = stmt.executeQuery();

            if(!rs.next()){
                System.out.println("Nessuna casa trovata!");
                disconnectDB();
                return a;
            }
            rs.last();
            a = new Abitazione[rs.getRow()];
            rs.first();
            int i=0;
            do{
                a[i] = new Abitazione(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), LocalDate.parse(rs.getString(7)), rs.getString(8), rs.getString(9), rs.getString(10), rs.getBoolean(11), rs.getString(12));
                a[i].setUserPicture(rs.getString(13));
                a[i].setUserFeed(rs.getString(14));
                i++;
            }while(rs.next());

        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        System.out.println("Query eseguita con successo!");
        return a;

    }

    @Override
    public Abitazione selectById(String id){
        Abitazione a = null;
        try{
            if(!connectDB(SELECT_BY_ID)){
                return a;
            }
            stmt = conn.prepareStatement(SELECT_BY_ID);
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            //rs.first();
            if(rs.next()) {
                a = new Abitazione(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), LocalDate.parse(rs.getString(7)), rs.getString(8), rs.getString(9), rs.getString(10), rs.getBoolean(11), rs.getString(12));
                a.setUserPicture(rs.getString(13));
                a.setUserFeed(rs.getString(14));
                System.out.println("Query eseguita con successo!");
            }
        }catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }


        return a;
    }


}
