package it.uiip.digitalgarage.ebuonweekend.dao;

import com.mysql.jdbc.Statement;
import it.uiip.digitalgarage.ebuonweekend.entity.Messaggio;
import it.uiip.digitalgarage.ebuonweekend.idao.MessaggioDAO;
import it.uiip.digitalgarage.ebuonweekend.utils.DateUtil;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

import static it.uiip.digitalgarage.ebuonweekend.dao.DBController.*;

@Component
public class MessaggioDAOImpl implements MessaggioDAO{

    private static final String INSERT = "INSERT INTO posta (sender, receiver, message, isHand, dataMessage, idHouseRcv) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_HAND = "UPDATE posta SET isHand=? WHERE sender=? AND receiver=? AND isHand=?";
    private static final String UPDATE_CHECKED_FLAG = "UPDATE posta SET checkedFlag=1 WHERE sender=? AND receiver=? AND checkedFlag=0";
    private static final String UPDATE_HANDSHAKE = "UPDATE posta SET isHand=?, feedback=? WHERE sender=? AND receiver=? AND isHand=?";
    private static final String DELETE_REJECTED_HANDSHAKE = "DELETE FROM posta WHERE sender=? AND receiver=? AND isHand=3 AND feedback IS NULL";
    private static final String ARCHIVE_HAND = "UPDATE posta SET isHand=? WHERE sender=? AND receiver=? AND isHand=?";
    private static final String SELECT_SENDERS_BY_RECEIVER="SELECT sender FROM posta WHERE receiver=? GROUP BY sender";
    private static final String SELECT_MSG_BY_SDR_RCV="SELECT * FROM posta WHERE (receiver=? AND sender=?) OR (receiver=? AND sender=?)";
    private static final String SELECT_MSG_BY_SDR_RCV_HAND="SELECT * FROM posta WHERE sender=? AND receiver=? AND isHand=?";
    private static final String SELECT_MSG_BY_SDR_RCV_HAND_HOUSE="SELECT * FROM posta WHERE sender=? AND receiver=? AND isHand=? AND idHouseRcv=?";
    private static final String SELECT_UNREAD_MSGS_BY_RECEIVER ="SELECT * FROM posta WHERE receiver=? AND checkedFlag=0";

    private static final String SELECT_MSG_BY_RCV="SELECT * FROM posta WHERE receiver=?";
    private static final String SELECT_MSG_BY_ID="SELECT * FROM posta WHERE id=?";

    private static final String SELECT_MSG_BY_RCV_HAND="SELECT * FROM posta WHERE receiver=? AND isHand=?";
    private static final String SELECT_MSG_BY_SDR_HAND="SELECT * FROM posta WHERE sender=? AND isHand=?";

    @Override
    public boolean insert(Messaggio m){
        try {
            if(!connectDB(INSERT))
                return false;
            else {
                stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, m.getSender());
                stmt.setString(2, m.getReceiver());
                stmt.setString(3, m.getMessage());
                stmt.setString(4, m.getIsHand().toString());
                stmt.setString(5, DateUtil.format(m.getDataMessage()));
                stmt.setString(6, String.valueOf(m.getIdHouseRcv()));

                int result = stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();

                if (rs.next()) {
                    m.setId(rs.getInt(1));
                }
                System.out.println("INSERT su messaggi eseguito!");
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
    public boolean deleteRejectedHandshake(String sdr, String rcv) {
        try{
            if(connectDB(DELETE_REJECTED_HANDSHAKE)){
                stmt = conn.prepareStatement(DELETE_REJECTED_HANDSHAKE, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, sdr);
                stmt.setString(2 , rcv);

                int result = stmt.executeUpdate();
                if(result != 0){
                    System.out.println("CANCELLAZIONE REJECTED HANDSHAKE ESEGUITA!");
                    disconnectDB();
                    return true;
                }
                else{
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
    public int[] setRead(String sender, String receiver){
        int a[] = null;
        //UPDATE posta SET checkedFlag=1 WHERE sender=? AND receiver=? AND checkedFlag=0
        try{
            if(!connectDB(UPDATE_CHECKED_FLAG)){
                return a;
            }

            stmt = conn.prepareStatement(UPDATE_CHECKED_FLAG, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, sender);
            stmt.setString(2, receiver);

            int result = stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            a = new int[result];
            rs.beforeFirst();
            if(rs.next()){
                int i = 0;
                do{
                    //System.out.println("ResultSet["+i+"] = "+rs.getInt(1));
                    a[i] = rs.getInt(1);
                    i++;
                    rs.next();
                }while(i<a.length);
                disconnectDB();
                System.out.println("UPDATE checked flag su posta eseguito!");
                return a;
            }
            else{
                disconnectDB();
                return a;
            }
        }catch (SQLException e) {
            // e.printStackTrace();
            disconnectDB();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
    }

    @Override
    public Messaggio selectById(Long index) {
        Messaggio msg = null;
        try {

            if(connectDB(SELECT_MSG_BY_ID)) {
                stmt.setLong(1, index);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    msg = new Messaggio(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getDate(6).toLocalDate(), rs.getString(7),rs.getBoolean(8));
                    msg.setIdHouseRcv(Long.valueOf(rs.getString(9)));
                    disconnectDB();
                    return msg;
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

        return msg;
    }

    @Override
    public boolean updateHand(Messaggio m) {
        try{
            if(!connectDB(UPDATE_HAND)){
                return false;
            }
            stmt = conn.prepareStatement(UPDATE_HAND, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, "2");
            stmt.setString(2, m.getSender());
            stmt.setString(3, m.getReceiver());
            stmt.setString(4, m.getIsHand().toString());
            int result = stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if(rs.next()){
                disconnectDB();
                System.out.println("UPDATE su posta eseguito!");
                return true;
            }
            else{
                disconnectDB();
                return false;
            }
        }catch (SQLException e) {
            // e.printStackTrace();
            disconnectDB();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }

    }

    @Override
    public boolean updateHandshake(Messaggio m, String feedback) {
        try{
            if(!connectDB(UPDATE_HANDSHAKE)){
                return false;
            }
            stmt = conn.prepareStatement(UPDATE_HANDSHAKE, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, "4");
            stmt.setString(2, feedback);
            stmt.setString(3, m.getSender());
            stmt.setString(4, m.getReceiver());
            stmt.setString(5, m.getIsHand().toString());

            int result = stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if(rs.next()){
                disconnectDB();
                System.out.println("UPDATE su posta eseguito!");
                return true;
            }
            else{
                disconnectDB();
                return false;
            }
        }catch (SQLException e) {
            // e.printStackTrace();
            disconnectDB();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }

    }

    @Override
    public boolean archiveHand(Messaggio m) {
        try{
            if(!connectDB(ARCHIVE_HAND)){
                return false;
            }
            stmt = conn.prepareStatement(ARCHIVE_HAND, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, "0");
            stmt.setString(2, m.getSender());
            stmt.setString(3, m.getReceiver());
            stmt.setString(4, m.getIsHand().toString());
            int result = stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if(rs.next()){
                disconnectDB();
                System.out.println("UPDATE su posta eseguito!");
                return true;
            }
            else{
                disconnectDB();
                return false;
            }
        }catch (SQLException e) {
            // e.printStackTrace();
            disconnectDB();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }

    }

    @Override
    public String[] selectSendersByRcv(String rcv){
        String m[]=null;
        int i=0;

        try{
            if(!connectDB(SELECT_SENDERS_BY_RECEIVER)){
                return m;
            }
            stmt.setString(1, rcv);
            rs = stmt.executeQuery();
            rs.last();
            m = new String[rs.getRow()];
            rs.beforeFirst();
            if (!rs.next()) {
                System.out.println("Nessun messaggio trovato!");
            } else {
                do {
                    //m[i] = new Messaggio(0, rs.getString(1), rcv, "", 5, null, "");
                    m[i] = rs.getString(1);
                    i++;
                    //}
                } while (rs.next());
                System.out.println("Ricerca avvenuta con successo!");
            }

        }catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        return m;

    }

    @Override
    public Messaggio[] selectMsgBySdrRcv(String sdr, String rcv){
        Messaggio m[]=null;
        int i=0;

        try{
            if(!connectDB(SELECT_MSG_BY_SDR_RCV)){
                return m;
            }
            stmt.setString(1, sdr);
            stmt.setString(2, rcv);
            stmt.setString(3, rcv);
            stmt.setString(4, sdr);

            rs = stmt.executeQuery();
            rs.last();
            m = new Messaggio[rs.getRow()];
            rs.beforeFirst();
            if(!rs.next()){
                System.out.println("Impossibile trovare messaggi!");
                disconnectDB();
                return null;
            }else{
                do{
                    m[i] = new Messaggio(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getDate(6).toLocalDate(), rs.getString(7),rs.getBoolean(8));
                    m[i].setIdHouseRcv(Long.valueOf(rs.getString(9)));
                    i++;
                    rs.next();
                }while(i<m.length);
                System.out.println("Messaggi trovati!");
            }
        }catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        return m;
    }

    @Override
    public Messaggio[] selectMsgBySdrRcvHand(String sdr, String rcv, String hand) {
        Messaggio m[]=null;
        int i=0;

        try{
            if(!connectDB(SELECT_MSG_BY_SDR_RCV_HAND)){
                return m;
            }
            stmt.setString(1, sdr);
            stmt.setString(2, rcv);
            stmt.setString(3, hand);

            rs = stmt.executeQuery();
            rs.last();
            m = new Messaggio[rs.getRow()];
            rs.beforeFirst();
            if(!rs.next()){
                System.out.println("Impossibile trovare messaggi!");
                disconnectDB();
                return null;
            }else{
                do{
                    m[i] = new Messaggio(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getDate(6).toLocalDate(), rs.getString(7),rs.getBoolean(8));
                    m[i].setIdHouseRcv(Long.valueOf(rs.getString(9)));
                    i++;
                    rs.next();
                }while(i<m.length);
                System.out.println("Messaggi trovati!");
            }
        }catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        return m;
    }

    @Override
    public Messaggio[] selectMsgBySdrRcvHandHouse(String sdr, String rcv, String hand, String idHouseRcv) {
        Messaggio m[]=null;
        int i=0;

        try{
            if(!connectDB(SELECT_MSG_BY_SDR_RCV_HAND_HOUSE)){
                return m;
            }
            stmt.setString(1, sdr);
            stmt.setString(2, rcv);
            stmt.setString(3, hand);
            stmt.setString(4, idHouseRcv);

            rs = stmt.executeQuery();
            rs.last();
            m = new Messaggio[rs.getRow()];
            rs.beforeFirst();
            if(!rs.next()){
                System.out.println("Impossibile trovare messaggi!");
                disconnectDB();
                return null;
            }else{
                do{
                    m[i] = new Messaggio(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getDate(6).toLocalDate(), rs.getString(7),rs.getBoolean(8));
                    m[i].setIdHouseRcv(Long.valueOf(rs.getString(9)));
                    i++;
                    rs.next();
                }while(i<m.length);
                System.out.println("Messaggi trovati!");
            }
        }catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        return m;
    }


    @Override
    public Messaggio[] selectMsgByReceiver(String rcv) {
        Messaggio m[]=null;
        int i=0;

        try{
            if(!connectDB(SELECT_UNREAD_MSGS_BY_RECEIVER)){
                return m;
            }
            stmt.setString(1, rcv);

            rs = stmt.executeQuery();
            rs.last();
            m = new Messaggio[rs.getRow()];
            rs.beforeFirst();
            if(!rs.next()){
                System.out.println("Impossibile trovare messaggi!");
                disconnectDB();
                return null;
            }else{
                do{
                    m[i] = new Messaggio(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getDate(6).toLocalDate(), rs.getString(7),rs.getBoolean(8));
                    m[i].setIdHouseRcv(Long.valueOf(rs.getString(9)));
                    i++;
                    rs.next();
                }while(i<m.length);
                System.out.println("Messaggi trovati!");
            }
        }catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        return m;
    }

    @Override
    public Messaggio[] selectMsgByRcvHand(String rcv, String hand){
        Messaggio m[]=null;
        int i=0;

        try{
            if(hand.equals("0") || hand.equals("1") || hand.equals("2")) {
                if (!connectDB(SELECT_MSG_BY_RCV_HAND)) {
                    return m;
                }
                stmt.setString(1, rcv);
                stmt.setString(2, hand);
                rs = stmt.executeQuery();
                rs.last();
                m = new Messaggio[rs.getRow()];
                rs.beforeFirst();
                if (!rs.next()) {
                    System.out.println("Impossibile trovare messaggi!");
                    disconnectDB();
                    return null;
                }
                do {
                    m[i] = new Messaggio(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getDate(6).toLocalDate(), rs.getString(7), rs.getBoolean(8));
                    m[i].setIdHouseRcv(Long.valueOf(rs.getString(9)));
                    i++;
                    rs.next();
                } while (i < m.length);
                System.out.println("Messaggi trovati!");
            }
            else{
                if (!connectDB(SELECT_MSG_BY_RCV)) {
                    return m;
                }
                stmt.setString(1, rcv);
                rs = stmt.executeQuery();
                rs.last();
                m = new Messaggio[rs.getRow()];
                rs.beforeFirst();
                if (!rs.next()) {
                    System.out.println("Impossibile trovare messaggi!");
                    disconnectDB();
                    return null;
                }
                do {
                    m[i] = new Messaggio(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getDate(6).toLocalDate(), rs.getString(7),rs.getBoolean(8));
                    m[i].setIdHouseRcv(Long.valueOf(rs.getString(9)));
                    i++;
                    rs.next();
                } while (i < m.length);
                System.out.println("Messaggi trovati!");
            }
        }catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        return m;
    }

    @Override
    public Messaggio[] selectMsgBySdrHand(String sdr, String hand) {
        Messaggio m[]=null;
        int i = 0;
        try{
            if (!connectDB(SELECT_MSG_BY_SDR_HAND)) {
                return m;
            }
            stmt.setString(1, sdr);
            stmt.setString(2, hand);
            rs = stmt.executeQuery();
            rs.last();
            m = new Messaggio[rs.getRow()];
            rs.beforeFirst();
            if (!rs.next()) {
                System.out.println("Impossibile trovare messaggi!");
                disconnectDB();
                return null;
            }
            do {
                m[i] = new Messaggio(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getDate(6).toLocalDate(), rs.getString(7),rs.getBoolean(8));
                m[i].setIdHouseRcv(Long.valueOf(rs.getString(9)));
                i++;
                rs.next();
            } while (i < m.length);
            System.out.println("Messaggi trovati!");

        }catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        return m;
    }
}
