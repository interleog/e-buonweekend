package it.uiip.digitalgarage.ebuonweekend.dao;

import com.mysql.jdbc.Statement;
import it.uiip.digitalgarage.ebuonweekend.entity.Wishlist;
import it.uiip.digitalgarage.ebuonweekend.idao.WishlistDAO;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

import static it.uiip.digitalgarage.ebuonweekend.dao.DBController.*;

@Component
public class WishlistDAOImpl implements WishlistDAO{

    private static final String INSERT = "INSERT INTO wishlist (user, idHouse) VALUES(?, ?)";
    private static final String DELETE = "DELETE FROM wishlist WHERE user=? AND idHouse=?";
    private static final String SELECT_BY_NICK = "SELECT * FROM wishlist WHERE user = ?";
    private static final String SELECT_BY_NICK_IDHOUSE = "SELECT * FROM wishlist WHERE user = ? AND idHouse = ?";

    @Override
    public boolean insert(Wishlist w) {

        try {
            if (!connectDB(INSERT)){
                return false;
            }
            stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, w.getUser());
            stmt.setString(2, String.valueOf(w.getIdHouse()));

            int result = stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                w.setId(rs.getInt(1));
            }

            disconnectDB();
            System.out.println("INSERT su wishlist eseguita!");
            return true;

        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
    }

    @Override
    public boolean delete(String nick, long idHouse) {
        try {
            if (!connectDB(DELETE))
                return false;
            else {
                stmt.setString(1, nick);
                stmt.setString(2, String.valueOf(idHouse));
                int rs = stmt.executeUpdate();
                if (rs == 0) {
                    System.out.println("House not found!");
                    disconnectDB();
                    return false;
                } else {
                    System.out.println("House deleted from wishlist!");
                    disconnectDB();
                    return true;
                }
            }

        } catch(Exception e){
            System.out.println("Problema nella query: \n" + DELETE + "\n" + e);
            disconnectDB();
            return false;
        }

    }

    @Override
    public Wishlist[] selectByNick(String nick) {
        Wishlist w[] = null;
        try {

            if(connectDB(SELECT_BY_NICK)) {
                stmt.setString(1, nick);
                rs = stmt.executeQuery();

                if(!rs.next()){
                    System.out.println("Nessuna casa trovata!");
                    disconnectDB();
                    return w;
                }

                rs.last();
                w = new Wishlist[rs.getRow()];
                rs.first();
                int i=0;
                do{
                    w[i] = new Wishlist(rs.getInt(1), rs.getString(2), rs.getLong(3));
                    i++;
                }while(rs.next());
            }
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        System.out.println("Query su WISHLIST eseguita!");
        return w;


    }

    @Override
    public Wishlist selectByNickIdHouse(String nick, String idHouse) {
        Wishlist w = null;
        try {

            if(connectDB(SELECT_BY_NICK_IDHOUSE)) {
                stmt.setString(1, nick);
                stmt.setString(2, idHouse);
                rs = stmt.executeQuery();

                if(!rs.next()){
                    disconnectDB();
                    return null;
                }
                w = new Wishlist(rs.getInt(1), rs.getString(2), rs.getLong(3));
            }
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
        return w;
    }
}
