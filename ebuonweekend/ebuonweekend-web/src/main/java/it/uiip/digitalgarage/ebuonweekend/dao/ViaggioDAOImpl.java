package it.uiip.digitalgarage.ebuonweekend.dao;

import com.mysql.jdbc.Statement;
import it.uiip.digitalgarage.ebuonweekend.entity.Viaggio;
import it.uiip.digitalgarage.ebuonweekend.idao.ViaggioDAO;
import static it.uiip.digitalgarage.ebuonweekend.dao.DBController.*;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class ViaggioDAOImpl implements ViaggioDAO {

    private static final String INSERT = "INSERT INTO viaggi (user, houseOwner, idHouse, journeyDate, idExchange) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_IDEXCHANGE = "UPDATE viaggi SET idExchange=? WHERE id=?";
    private static final String DELETE_BY_USER_HOUSEOWNER_IDEXCHANGE = "DELETE FROM viaggi WHERE user=? AND houseOwner=? AND idExchange=?";
    private static final String SELECT_BY_USER_IDHOUSE_DATE = "SELECT * FROM viaggi WHERE user=? AND idHouse=? AND journeyDate=?";
    private static final String SELECT_BY_USER_HOUSEOWNER_IDEXCHANGE = "SELECT * FROM viaggi WHERE user=? AND houseOwner=? AND idExchange=?";
    private static final String SELECT_BY_USER_HOUSEOWNER_DATE_IDEXCHANGE = "SELECT * FROM viaggi WHERE user=? AND houseOwner=? AND journeyDate=? AND idExchange=?";
    private static final String SELECT_COMPLETED_JOURNEY = "SELECT viaggi.*, abitazioni.city, abitazioni.address, posta.feedback FROM (viaggi JOIN abitazioni ON viaggi.idHouse=abitazioni.id) JOIN posta ON (user = posta.sender AND houseOwner=posta.receiver AND isHand=4) WHERE user=? AND idExchange<>0;";

    @Override
    public boolean insert(Viaggio v) {
        try {
            if(!connectDB(INSERT))
                return false;
            else {
                stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, v.getUser());
                stmt.setString(2, v.getHouseOwner());
                stmt.setString(3, v.getIdHouse());
                stmt.setString(4, v.getJourneyDate());
                stmt.setString(5, String.valueOf(v.getIdExchange()));

                int result = stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();

                if (rs.next()) {
                    v.setId(rs.getInt(1));
                }
                System.out.println("INSERT su viaggi eseguito!");
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
    public boolean updateIDExchange(Viaggio v, int idExchange) {
        try{
            if(connectDB(UPDATE_IDEXCHANGE)){
                stmt = conn.prepareStatement(UPDATE_IDEXCHANGE, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, String.valueOf(idExchange));
                stmt.setString(2 , String.valueOf(v.getId()));

                int result = stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();
                if(rs.next()){
                    disconnectDB();
                    return true;
                }
            }
        }catch (SQLException e) {
            // e.printStackTrace();
            disconnectDB();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        return false;
    }

    @Override
    public boolean deleteByUserHouseownerIDExchange(String user, String houseOwner, int idExchange) {
        try{
            if(connectDB(DELETE_BY_USER_HOUSEOWNER_IDEXCHANGE)){
                stmt = conn.prepareStatement(DELETE_BY_USER_HOUSEOWNER_IDEXCHANGE, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, user);
                stmt.setString(2, houseOwner);
                stmt.setString(3, String.valueOf(idExchange));

                int result = stmt.executeUpdate();
                if(result != 0){
                    System.out.println("CANCELLAZIONE VIAGGIO ESEGUITA!");
                    disconnectDB();
                    return true;
                }else{
                    disconnectDB();
                    return false;
                }
            }
        }catch (SQLException e) {
            // e.printStackTrace();
            disconnectDB();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        return false;
    }

    @Override
    public Viaggio selectByUserIDHouseDate(String user, String IDHouse, String journeyDate) {
        Viaggio viaggio = null;

        try {

            if(connectDB(SELECT_BY_USER_IDHOUSE_DATE)) {
                stmt.setString(1, user);
                stmt.setString(2, IDHouse);
                stmt.setString(3, journeyDate);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    viaggio = new Viaggio(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
                }
            }
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        return viaggio;
    }

    @Override
    public Viaggio[] selectByUserHouseownerIDExchange(String user, String houseOwner, int idExchange) {
        Viaggio v[] = null;
        int i =0;

        try{
            if(connectDB(SELECT_BY_USER_HOUSEOWNER_IDEXCHANGE)){
                stmt.setString(1, user);
                stmt.setString(2, houseOwner);
                stmt.setString(3, String.valueOf(idExchange));
                rs = stmt.executeQuery();

                if(rs.next()){
                    rs.last();
                    v = new Viaggio[rs.getRow()];
                    rs.beforeFirst();
                    while(rs.next()){
                        v[i] = new Viaggio(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
                        i++;
                    }
                }
            }
        }catch (SQLException e) {
            // e.printStackTrace();
            disconnectDB();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }

        return v;

    }

    @Override
    public Viaggio[] selectByUserHouseownerDateIDExchange(String user, String houseOwner, String date, int idExchange) {

        Viaggio v[] = null;
        int i =0;

        try{
            if(connectDB(SELECT_BY_USER_HOUSEOWNER_DATE_IDEXCHANGE)){
                stmt.setString(1, user);
                stmt.setString(2, houseOwner);
                stmt.setString(3, date);
                stmt.setString(4, String.valueOf(idExchange));
                rs = stmt.executeQuery();

                if(rs.next()){
                    rs.last();
                    v = new Viaggio[rs.getRow()];
                    rs.beforeFirst();
                    while(rs.next()){
                        v[i] = new Viaggio(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
                        i++;
                    }
                }
            }
        }catch (SQLException e) {
            // e.printStackTrace();
            disconnectDB();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }

        return v;

    }

    @Override
    public Viaggio[] selectCompletedJourney(String user) {

        Viaggio v[] = null;
        int i =0;

        try{
            if(connectDB(SELECT_COMPLETED_JOURNEY)){
                stmt.setString(1, user);
                rs = stmt.executeQuery();

                if(rs.next()){
                    rs.last();
                    v = new Viaggio[rs.getRow()];
                    rs.beforeFirst();
                    while(rs.next()){
                        v[i] = new Viaggio(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
                        v[i].setCity(rs.getString(7));
                        v[i].setAddress(rs.getString(8));
                        v[i].setFeedback(rs.getString(9));
                        i++;
                    }
                }
            }
        }catch (SQLException e) {
            // e.printStackTrace();
            disconnectDB();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        return v;
    }
}
