package it.uiip.digitalgarage.ebuonweekend.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import it.uiip.digitalgarage.ebuonweekend.entity.GenericReturn;
import it.uiip.digitalgarage.ebuonweekend.entity.Messaggio;
import it.uiip.digitalgarage.ebuonweekend.entity.Utente;
import it.uiip.digitalgarage.ebuonweekend.ibe.UtenteService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
public class UtenteController {

    @Autowired
    UtenteService utenteService;

    private final AtomicLong counter = new AtomicLong();

    //METODO PER REGISTRAZIONE UTENTE
    @RequestMapping("/register")
    public GenericReturn<Utente> register(@RequestParam(value = "name", defaultValue = "@undefined") String name,
                           @RequestParam(value = "nick", defaultValue = "@unregistered") String nick,
                           @RequestParam(value = "pass", defaultValue = "@unregistered") String pass,
                           @RequestParam(value = "last", defaultValue = "@unregistered") String last,
                           @RequestParam(value = "tel", defaultValue = "@unregistered") String tel,
                           @RequestParam(value = "mail", defaultValue = "@unregistered") String mail,
                           @RequestParam(value = "profilePicture", defaultValue = "http://138.68.133.189/ebuonweekend-web/resources/img/nophoto.png") String picture) {

        ///register?nick=leo&pass=1&mail=abc

        if (!nick.equals("@unregistered") && !pass.equals("@unregistered") && !mail.equals("@unregistered")) {
            List<String> errori = new ArrayList<>();

            Utente u = new Utente(0,name, nick, pass, last, tel, mail, LocalDate.now(), "sea;mountain;city", "0", "0", "0", picture);

            if(utenteService.insert(u)){
                System.out.println("Utente registrato correttamente");
                return new GenericReturn<Utente>(u,errori);
            }
            else{
                System.out.println("ERRORE DI REGISTRAZIONE");
                errori.add("ERRORE DI REGISTRAZIONE");
                return  new GenericReturn<Utente>(null,errori);
            }
        }
        else{
            List<String> errori = new ArrayList<>();

            System.out.println("NOTHING RECEIVED");
            errori.add("NOTHING RECEIVED");
            return  new GenericReturn<Utente>(null,errori);
        }
    }


    //METODO PER IL LOGIN
    @RequestMapping("/logon")
    public GenericReturn<Utente> loginUtente(@RequestParam(value = "nick", defaultValue="NO-NICK") String nick,
                              @RequestParam(value = "pass", defaultValue ="NO-PWD") String pass){

    /*@RequestMapping(value="/logon/{nick}/{pass}")
    public Utente loginUtente(@PathVariable("nick") String nick,
                              @PathVariable("pass") String pass){*/

    /*@RequestMapping(value = "/logon", method = RequestMethod.POST)
    public Utente logInUtente(@RequestBody Utente u) {*/

        Utente u = new Utente(0, "", nick, pass, "", "", "", LocalDate.now(), "", "", "", "", "");

        if(utenteService.authentication(u)){
            System.out.println("Utente loggato!");
            return new GenericReturn<>(u);
        }
        else{
            System.out.println("Nickname o password errati!");
            return new GenericReturn<>(null);
        }

    }

    //METODO PER CANCELLARE L'ACCOUNT
    @RequestMapping("/unregister")
    public GenericReturn<Boolean> unregister(@RequestParam(value = "nick", defaultValue = "NO-NAME") String nick) {
//        http://localhost:8080/unregister?nick=gian&sure=true

        return new GenericReturn<Boolean>(utenteService.delete(nick));
    }

    //METODO PER RITORNARE UN UTENTE
    @RequestMapping("/getUser")
    public Utente getUser(@RequestParam(value = "nick", defaultValue = "@unregistered") String nick) {

        return utenteService.selectByNick(nick);
    }

    //QUERY PER AGGIORNARE IL PROFILO DI UN UTENTE
    @RequestMapping("/editUser")
    public GenericReturn<Boolean> editUser(@RequestParam(value = "nickName", defaultValue = "undefined") String nickName,
                         @RequestParam(value = "pass", defaultValue = "undefined") String pass,
                         @RequestParam(value = "firstName", defaultValue = "undefined") String firstName,
                         @RequestParam(value = "lastName", defaultValue = "undefined") String lastName,
                         @RequestParam(value = "telephone", defaultValue = "undefined") String tel,
                         @RequestParam(value = "statoUtente", defaultValue = "undefined") String stato,
                         @RequestParam(value = "profilePicture", defaultValue = "undefined") String picture){

        ///ebuonweekend-web/editUser?nickName=leo&firstName=Leonardo&lastName=Galati&telephone=3483401922&statoUtente=montagna&profilePicture=C:/E-Buonweekend-Uploads/image_4.jpg

        if(!nickName.equals("undefined")) {
            Utente u = new Utente(0, firstName, nickName, pass, lastName, tel, "", LocalDate.now(), stato, "0", "0", "0", picture);
            return new GenericReturn<Boolean>(utenteService.update(u));
        }
        else {
            return new GenericReturn<Boolean>(false);
        }

    }


    //METODO PER AGGIUNGERE UN FEEDBACK
    @RequestMapping("/addFeed")
    public GenericReturn<Boolean> addFeed(@RequestParam(value = "sender", defaultValue = "undefined") String sender,
                          @RequestParam(value = "houseOwner", defaultValue = "NO-OWNER") String houseOwner,
                          @RequestParam(value = "feed", defaultValue="null") String feed){
        //http://localhost:8080/ebuonweekend-web/addFeed?sender=leo&houseOwner=lorenzo_g&feed=10

        if(!sender.equals(houseOwner) && !sender.equals("undefined")) {
            return new GenericReturn<Boolean>(utenteService.insertFeedback(sender, houseOwner, feed));
        }
        else{
            return new GenericReturn<Boolean>(false);
        }
    }


    //METODO PER LEGGERE IL FEED DI UN UTENTE
    @RequestMapping("/readFeed")
    public GenericReturn<String> readFeed(@RequestParam(value = "user", defaultValue = "undefined") String user){

        return new GenericReturn<>(utenteService.selectFeedByNick(user));

    }

    //METODO PER CERCARE TUTTI I FEEDBACK ANCORA DA DARE DELL'UTENTE
    @RequestMapping("/pendingFeedbacks")
    public Messaggio[] pendingFeedbacks(@RequestParam(value="user", defaultValue = "undefined") String user){

        return utenteService.pendingFeedbacks(user);

    }

    //METODO PER RICERCARE TUTTI I VIAGGI DI UN UTENTE
    @RequestMapping("/searchMyJourneys")
    public Messaggio[] searchMyJourneys(@RequestParam(value="user", defaultValue = "undefined") String user){

        return utenteService.selectMyJourneys(user);

    }

    @RequestMapping(value = {"/uploadImage"}, headers = "content-type=multipart/*", method = RequestMethod.POST)
    @ResponseBody
    public GenericReturn<String> uploadAllegato(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        System.out.println(file.getName() + " - " + file.getContentType());

        Path dbFolder = Paths.get("/home/tomcat/e-buonweekend/profiles/");

        String error = "";

        Boolean created = true;
        if (Files.notExists(dbFolder)) {
            System.out.println("path doesn't exist");
            created = new File("/home/tomcat/e-buonweekend/profiles/").mkdirs();
            if (created)
                System.out.println("path created!");
            else
                System.out.println(error+="error creating path");
        }
        if (created) {

            String fileName= "image_" + (Files.list(Paths.get("/home/tomcat/e-buonweekend/profiles/")).count() + 1) + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            File convFile = new File("/home/tomcat/e-buonweekend/profiles/" + fileName);

            System.out.println(convFile.getAbsolutePath());
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();

            String path="http://138.68.133.189/ebuonweekend-web/external/profiles/"+fileName;

            //convFile.getAbsolutePath().replace("\\","/")
            return new GenericReturn<>(path, null);
        } else {
            System.out.println("Error uploading file.");
            return new GenericReturn<>("Something failed"+error, null);

        }


    }


}
