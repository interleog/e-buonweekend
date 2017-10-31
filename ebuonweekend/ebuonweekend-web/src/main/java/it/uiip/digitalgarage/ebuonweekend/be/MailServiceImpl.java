package it.uiip.digitalgarage.ebuonweekend.be;

import it.uiip.digitalgarage.ebuonweekend.entity.Mail;
import it.uiip.digitalgarage.ebuonweekend.ibe.MailService;
import it.uiip.digitalgarage.ebuonweekend.idao.MailDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService{

    @Autowired
    MailDAO mailDAO;

    @Override
    public  boolean insert(Mail m){
        return mailDAO.insert(m);

    }



}
