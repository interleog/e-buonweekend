package it.uiip.digitalgarage.ebuonweekend.dao;

import it.uiip.digitalgarage.ebuonweekend.entity.Mail;
import it.uiip.digitalgarage.ebuonweekend.idao.MailDAO;
import it.uiip.digitalgarage.ebuonweekend.utils.DateUtil;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.sql.Statement;

import static it.uiip.digitalgarage.ebuonweekend.dao.DBController.*;

@Component
public class MailDAOImpl implements MailDAO{

    private static final String INSERT = "INSERT INTO maillist (sender, object, text, mailDay) VALUES(?, ?, ?, ?)";

    @Override
    public boolean insert(Mail m){
        try {
            if(!connectDB(INSERT))
                return false;
            else {
                stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1,m.getSender());
                stmt.setString(2, m.getObject() );
                stmt.setString(3, m.getText());
                stmt.setString(4, DateUtil.format(m.getMailDay()));

                int result = stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();

                if (rs.next()) {
                    m.setId(rs.getInt(1));
                }
                disconnectDB();
                System.out.println("INSERT su mailist eseguito!");
                return true;
            }
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            disconnectDB();
        }
    }




}
