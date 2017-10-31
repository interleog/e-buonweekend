package it.uiip.digitalgarage.ebuonweekend.dao;

import com.mysql.jdbc.Statement;
import it.uiip.digitalgarage.ebuonweekend.entity.Utente;
import it.uiip.digitalgarage.ebuonweekend.utils.DateUtil;
import it.uiip.digitalgarage.ebuonweekend.idao.UtenteDAO;
import it.uiip.digitalgarage.ebuonweekend.utils.Sha256Util;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.LocalDate;

import static it.uiip.digitalgarage.ebuonweekend.dao.DBController.*;


@Component
public class UtenteDAOImpl  implements UtenteDAO {

    private static final String INSERT = "INSERT INTO utenti (firstName, nickName,password, lastName, telephone, email, memberSince, statoUtente, profilePicture) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE utenti SET firstName=?, lastName=?, telephone=? , statoUtente=?, profilePicture=? WHERE nickName=?";
    private static final String UPDATE_PW = "UPDATE utenti SET firstName=?, lastName=?, password=?, telephone=? , statoUtente=?, profilePicture=? WHERE nickName=?";
    private static final String DELETE = "DELETE FROM utenti WHERE nickName=?";
    private static final String UPDATE_FEED = "UPDATE utenti SET feedTotal=?, numFeed=?, feedValue=? WHERE nickName=? AND password=?";
    private static final String SELECT_BY_NICK_PASS="SELECT * FROM utenti WHERE nickName=? AND password=?";
    private static final String SELECT_BY_NICK = "SELECT * FROM utenti WHERE nickName=?";
    private static final String SELECT_BEST_USERS = "SELECT nickName FROM utenti WHERE feedValue!='0' ORDER BY feedValue * 1 DESC";


    @Override
    public boolean insert(Utente user){

        try {

            if (!connectDB(INSERT)){
                return false;
            }
            stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getNickName());
            String pw = Sha256Util.sha256(user.getPassword());
            stmt.setString(3, pw);
            //stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getTelephone());
            stmt.setString(6, user.getEmail());
            stmt.setString(7, DateUtil.format(user.getMemberSince()));
            stmt.setString(8, user.getStatoUtente());
            stmt.setString(9, user.getProfilePicture());
            int result = stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                user.setId(rs.getInt(1));
                user.setPassword(pw);
            }

            disconnectDB();
            System.out.println("INSERT su utenti eseguita!");
            return true;

        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }

    }

    @Override
    public boolean update(Utente user){
        try {
            if(!connectDB(UPDATE))
                return false;
            else {
                stmt.setString(1, user.getFirstName());
                stmt.setString(2, user.getLastName());
                stmt.setString(3, user.getTelephone());
                stmt.setString(4, user.getStatoUtente());
                stmt.setString(5, user.getProfilePicture());
                stmt.setString(6, user.getNickName());

                int rs = stmt.executeUpdate();
                if(rs == 0){
                    System.out.println("Nickname non trovato!");
                    disconnectDB();
                    return false;
                }
                disconnectDB();
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
    public boolean update_pw(Utente user){
        try {
            if(!connectDB(UPDATE_PW))
                return false;
            else {
                stmt.setString(1, user.getFirstName());
                stmt.setString(2, user.getLastName());
                String pw = Sha256Util.sha256(user.getPassword());
                stmt.setString(3, pw);
                stmt.setString(4, user.getTelephone());
                stmt.setString(5, user.getStatoUtente());
                stmt.setString(6, user.getProfilePicture());
                stmt.setString(7, user.getNickName());

                int rs = stmt.executeUpdate();
                if(rs == 0){
                    System.out.println("Nickname non trovato!");
                    disconnectDB();
                    return false;
                }

                disconnectDB();
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
    public boolean delete(String nickName){

        boolean returned = false;
        try {

            if (!connectDB(DELETE))
                return false;
            else {
                stmt.setString(1, nickName);
                int rs = stmt.executeUpdate();
                if (rs == 0) {
                    System.out.println("Nickname not found!");
                    disconnectDB();
                    return false;
                } else {
                    System.out.println("Unregistered!");
                    returned = true;
                }
            }

        } catch(Exception e){
            System.out.println("Problema nella query unregister(): \n" + DELETE + "\n" + e);
            disconnectDB();
            return false;
        } finally{
            //chiusura connessione db
            disconnectDB();
        }
        return  returned;

}
    @Override
    public boolean authentication(Utente user){
        String nick = user.getNickName();
        String pass = Sha256Util.sha256(user.getPassword());
        //String pass = user.getPassword();
        try{
            if(!connectDB(SELECT_BY_NICK_PASS)){
                return false;
            }
            stmt.setString(1, nick);
            stmt.setString(2, pass);
            rs = stmt.executeQuery();

            if(rs.next()){
                user.setId(rs.getInt(1));
                user.setFirstName(rs.getString(2));
                user.setNickName(rs.getString(3));
                user.setPassword(rs.getString(4));
                user.setLastName(rs.getString(5));
                user.setTelephone(rs.getString(6));
                user.setEmail(rs.getString(7));
                user.setMemberSince(LocalDate.parse(rs.getString(8)));
                user.setStatoUtente(rs.getString(9));
                user.setFeedTotal(rs.getString(10));
                user.setNumFeed(rs.getString(11));
                user.setFeedValue(rs.getString(12));
                user.setProfilePicture(rs.getString(13));

                disconnectDB();
                return true;
            }else{
                disconnectDB();
                return false;
            }

        }catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
    }

    @Override
    public Utente selectByNick(String nickName){
        Utente user = null;
        try {

            if(connectDB(SELECT_BY_NICK)) {
                stmt.setString(1, nickName);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    user = new Utente(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), DateUtil.parse(rs.getString(8)), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13));
                    disconnectDB();
                    return user;
                } else {
                    disconnectDB();
                    return null;
                }
            }
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        return user;
    }

    @Override
    public boolean updateFeedBack(Utente user) {
        try {
            if(!connectDB(UPDATE_FEED))
                return false;
            else {
                stmt.setString(1, user.getFeedTotal());
                stmt.setString(2, user.getNumFeed());
                stmt.setString(3, user.getFeedValue());
                stmt.setString(4, user.getNickName());
                stmt.setString(5, user.getPassword());

                stmt.executeUpdate();

                disconnectDB();
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
    public String[] selectBestUsers(){
        String s[] = null;
        int i=0;
        try{
            if(!connectDB(SELECT_BEST_USERS)){
                return s;
            }
            stmt = conn.prepareStatement(SELECT_BEST_USERS);
            rs = stmt.executeQuery();

            if(!rs.next()){
                System.out.println("Nessun utente trovato!");
                disconnectDB();
                return s;
            }
            rs.last();

            if (rs.getRow()>5){
                s = new String[5];
                rs.beforeFirst();
                do{
                    rs.next();
                    s[i] = rs.getString(1);
                    i++;
                }while(i<s.length);

            }
            else{
                s = new String[rs.getRow()];
                rs.beforeFirst();
                do{
                    rs.next();
                    s[i] = rs.getString(1);
                    i++;
                }while(rs.next());
            }
        }catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }

        return s;

    }

}
