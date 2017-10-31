package it.uiip.digitalgarage.ebuonweekend.controller;

import it.uiip.digitalgarage.ebuonweekend.entity.Abitazione;
import it.uiip.digitalgarage.ebuonweekend.entity.GenericReturn;
import it.uiip.digitalgarage.ebuonweekend.ibe.AbitazioneService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class AbitazioneController{

    @Autowired
    AbitazioneService abitazioneService;


    private final AtomicLong counterHouse = new AtomicLong();

    @RequestMapping("/newHouse")
    public Abitazione newHouse(@RequestParam(value = "city", defaultValue = "undefined") String city,
                               @RequestParam(value = "address", defaultValue = "undefined") String address,
                               @RequestParam(value = "zip", defaultValue = "000") String zip,
                               @RequestParam(value = "mq", defaultValue = "0") String mq,
                               @RequestParam(value = "path", defaultValue = "NO-PATH") String path,
                               @RequestParam(value = "user", defaultValue = "NO-USER") String user,
                               @RequestParam(value = "tags", defaultValue = "NO-TAGS") String tags,
                               @RequestParam(value = "descr", defaultValue= "NO-DESC") String descr,
                               @RequestParam(value = "isAvailable", defaultValue = "true") String isAvailable,
                               @RequestParam(value = "availableDates", defaultValue = "null") String availableDates) {
        //campi obbligatori: pathImage, city, descr
        //example url: http://localhost:8080/newHouse?city=Milano&address=viapozzi&zip=9282&mq=22&path=/img/h1.jpg

        if(!path.equals("NO-PATH") && !city.equals("undefined") && !descr.equals("NO-DESC")) {
            //TODO
            Abitazione abitazione = new Abitazione(counterHouse.incrementAndGet(), city, address, zip, mq, user, LocalDate.now(), path, tags, descr, Boolean.parseBoolean(isAvailable), availableDates);

            if (abitazioneService.insert(abitazione)) {
                return abitazione;
            } else {
                return null;
            }
        }
        else return null;
    }

    @RequestMapping("/deleteHouse")
    public GenericReturn<Boolean> deleteHouse(@RequestParam(value = "id", defaultValue = "NO-ID") String idHouse){
        if(!idHouse.equals("NO-ID")) {
            Abitazione a = new Abitazione(Long.parseLong(idHouse), "", "", "", "", "", LocalDate.now(), "", "", "", true, "");
            return new GenericReturn<>(abitazioneService.delete(a));
        }
        else return new GenericReturn<>(false);
    }

    @RequestMapping("/search")
    public Abitazione[] search(@RequestParam(value = "city", defaultValue = "undefined") String city,
                               @RequestParam(value = "location", defaultValue = "undefined") String location,
                               @RequestParam(value = "date", defaultValue = "undefined") String date) {

        if(city.equals("undefined") && location.equals("undefined") && date.equals("undefined")){
            return abitazioneService.selectAll();
        }
        else if(!city.equals("undefined") && location.equals("undefined") && date.equals("undefined")){
                return abitazioneService.selectByCity(city);
            }
            else if(city.equals("undefined") && !location.equals("undefined") && date.equals("undefined")){
                    return abitazioneService.selectByLocation(location);
                }
                else if(city.equals("undefined") && location.equals("undefined") && !date.equals("undefined")){
                        return abitazioneService.selectByDate(date);
                    }
                    else if(!city.equals("undefined") && !location.equals("undefined") && date.equals("undefined")) {
                            return abitazioneService.selectByCityLocation(city, location);
                        }
                        else if(!city.equals("undefined") && location.equals("undefined") && !date.equals("undefined")){
                                return abitazioneService.selectByCityDate(city, date);
                            }
                            else if(city.equals("undefined") && !location.equals("undefined") && !date.equals("undefined")){
                                    return abitazioneService.selectByLocationDate(location, date);
                                }
                                else{
                                    return abitazioneService.selectByCityLocationDate(city, location, date);
                                }
    }

    @RequestMapping("/searchHouseByUser")
    public Abitazione[] searchHouseByUser(@RequestParam(value = "user", defaultValue = "NO-User") String user) {
        return abitazioneService.selectByUser(user);
    }

    //QUERY PER TORNARE LE CASE DEGLI UTENTI CON FEEDBACK PIù ALTO
    @RequestMapping("/searchBestHouses")
    public Abitazione[] searchBestHouses() {
        return abitazioneService.selectBestHouses();
    }


    @RequestMapping("/searchAllHouse")
    public Abitazione[] searchAllHouse() {
        return abitazioneService.selectAll();
    }

    //QUERY PER AGGIORNARE L'ABITAZIONE DI UN UTENTE
    @RequestMapping("/editHouse")
    public GenericReturn<Boolean> editHouse(@RequestParam(value = "id", defaultValue = "undefined") int id,
                          @RequestParam(value = "city", defaultValue = "undefined") String city,
                          @RequestParam(value = "address", defaultValue = "undefined") String adr,
                          @RequestParam(value = "zip", defaultValue = "0") String zip,
                          @RequestParam(value = "mq", defaultValue = "0") String mq,
                          @RequestParam(value = "pathimg", defaultValue = "undefined") String pathImg,
                          @RequestParam(value = "tags", defaultValue = "undefined") String tags,
                          @RequestParam(value = "descr", defaultValue = "undefined") String descr,
                          @RequestParam(value = "isAvailable", defaultValue = "true") String isAvailable,
                          @RequestParam(value = "availableDates", defaultValue = "null") String availableDates){

        ///ebuonweekend-web/editHouse?id=34&city=Mascali&address=Via%20Nazionale%209&zip=95016&mq=120&pathimg=./resources/photo/CasaLeo.png&tags=mare&descr=Casa%20bellissima%20a%20mare

        Abitazione a = new Abitazione(id, city, adr, zip, mq, "", LocalDate.now(), pathImg, tags, descr,Boolean.parseBoolean(isAvailable), availableDates);

        return new GenericReturn<>(abitazioneService.update(a));
    }


    @RequestMapping("/getHouseById")
    public Abitazione getHouseById(@RequestParam(value = "id", defaultValue = "undefined") String id) {
        return abitazioneService.selectById(id);
    }


    @RequestMapping(value = {"/uploadHouseImage"}, headers = "content-type=multipart/*", method = RequestMethod.POST)
    @ResponseBody
    public GenericReturn<String> uploadHouseAllegato(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        System.out.println(file.getName() + " - " + file.getContentType());

        Path dbFolder = Paths.get("/home/tomcat/e-buonweekend/houses/");

        String error = "";

        Boolean created = true;
        if (Files.notExists(dbFolder)) {
            System.out.println("path doesn't exist");
            created = new File("/home/tomcat/e-buonweekend/houses/").mkdirs();
            if (created)
                System.out.println("path created!");
            else
                System.out.println(error+="error creating path");
        }
        if (created) {

            String fileName= "image_" + (Files.list(Paths.get("/home/tomcat/e-buonweekend/houses/")).count() + 1) + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            File convFile = new File("/home/tomcat/e-buonweekend/houses/" + fileName);

            System.out.println(convFile.getAbsolutePath());
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();

            String path="http://138.68.133.189/ebuonweekend-web/external/houses/"+fileName;

            //convFile.getAbsolutePath().replace("\\","/")
            return new GenericReturn<>(path, null);
        } else {
            System.out.println("Error uploading file.");
            return new GenericReturn<>("Something failed"+error, null);

        }


    }
}