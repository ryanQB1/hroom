package server.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import server.integration.hroomDAO;
import server.model.Oder;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class hroomcontroller {

    @EJB
    hroomDAO hdb;

    private final int TOT_ROOM1 = 9;
    private final int TOT_ROOM2 = 13;
    private final int TOT_ROOM3 = 34;

    private JsonObject makeorder(String ord) {
        String[] orddiv = ord.split("#");
        if(orddiv.length!=5) return erronousRequest("invalid username or password");
        for(String str : orddiv){
            if(str == null || str.isEmpty()) return erronousRequest("invalid order made : invalid order made");
        }
        if(!hdb.exists(orddiv[0])) return erronousRequest("invalid username");
        boolean val = true;
        String mess;
        try{
        for(int a = 1; a< 4; a++){
            if(Integer.parseInt(orddiv[a+1])>roomav(a, orddiv[1]) && Integer.parseInt(orddiv[a+1])>roommy(a, orddiv[1], orddiv[0])) val = false;
        }
        if(val){
            hdb.newOrder(orddiv[0], orddiv[1], new int[]{Integer.parseInt(orddiv[2]), Integer.parseInt(orddiv[3]), Integer.parseInt(orddiv[4])});
            mess = "rent succesful";
        }
        else{
            mess = "rent unsuccesful : can't rent that amount of rooms!";
        }
        }
        catch(NumberFormatException e){
            return erronousRequest("invalid order made : invalid order made");
        }
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("date", orddiv[1]);
        job.add("room1av", roomav(1, orddiv[1]));
        job.add("room1my", roommy(1, orddiv[1], orddiv[0]));
        job.add("room2av", roomav(2, orddiv[1]));
        job.add("room2my", roommy(2, orddiv[1], orddiv[0]));
        job.add("room3av", roomav(3, orddiv[1]));
        job.add("room3my", roommy(3, orddiv[1], orddiv[0]));
        job.add("loggedin", true);
        job.add("admin", hdb.isAdmin(orddiv[0]));
        job.add("generaltext", mess);
        job.add("username", orddiv[0]);
        return job.build();
    }

    private int roomav(int room, String date) {
        int occ = hdb.occupation(room, date);
        switch (room) {
            case 1:
                return TOT_ROOM1 - occ;
            case 2:
                return TOT_ROOM2 - occ;
            case 3:
                return TOT_ROOM3 - occ;
            default:
                return 0;
        }
    }

    private int roommy(int room, String date, String username) {
        List<Oder> ord = hdb.myOccupation(room, date, username);
        int tot = 0;
        for (Oder or : ord) {
            tot += or.getAmnt();
        }
        return tot;
    }

    private JsonObject newacc(String req) {
        String[] uandp = req.split("#");
        if(uandp.length!=2) return erronousRequest("invalid username or password");
        for(String str : uandp){
            if(str == null || str.isEmpty()) return erronousRequest("invalid username or password");
        }
        JsonObjectBuilder job = Json.createObjectBuilder();
        boolean admin;
        admin = uandp[0].startsWith("admin/");
        if (hdb.newProfile(uandp[0], uandp[1], admin)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime dateTime = LocalDateTime.now();
            String formattedDate = dateTime.format(formatter);
            job.add("date", formattedDate);
            job.add("room1av", roomav(1, formattedDate));
            job.add("room1my", roommy(1, formattedDate, uandp[0]));
            job.add("room2av", roomav(2, formattedDate));
            job.add("room2my", roommy(2, formattedDate, uandp[0]));
            job.add("room3av", roomav(3, formattedDate));
            job.add("room3my", roommy(3, formattedDate, uandp[0]));
            job.add("loggedin", true);
            job.add("admin", hdb.isAdmin(uandp[0]));
            job.add("generaltext", "Succesfully logged in as: " + uandp[0]);
            job.add("username", uandp[0]);
        } else {
            job.add("date", "0001-01-01");
            job.add("room1av", 0);
            job.add("room1my", 0);
            job.add("room2av", 0);
            job.add("room2my", 0);
            job.add("room3av", 0);
            job.add("room3my", 0);
            job.add("loggedin", false);
            job.add("admin", hdb.isAdmin(uandp[0]));
            job.add("username", uandp[0]);
            job.add("generaltext", "Could not create account");
        }
        return job.build();
    }

    private JsonObject login(String req) {
        String[] uandp = req.split("#");
        if(uandp.length!=2) return erronousRequest("invalid username or password");
        for(String str : uandp){
            if(str == null || str.isEmpty()) return erronousRequest("invalid username or password");
        }
        JsonObjectBuilder job = Json.createObjectBuilder();
        if (hdb.loginCorrect(uandp[0], uandp[1])) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime dateTime = LocalDateTime.now();
            String formattedDate = dateTime.format(formatter);
            job.add("date", formattedDate);
            job.add("room1av", roomav(1, formattedDate));
            job.add("room1my", roommy(1, formattedDate, uandp[0]));
            job.add("room2av", roomav(2, formattedDate));
            job.add("room2my", roommy(2, formattedDate, uandp[0]));
            job.add("room3av", roomav(3, formattedDate));
            job.add("room3my", roommy(3, formattedDate, uandp[0]));
            job.add("loggedin", true);
            job.add("admin", hdb.isAdmin(uandp[0]));
            job.add("generaltext", "Succesfully logged in as: " + uandp[0]);
            job.add("username", uandp[0]);
        } else {
            job.add("date", "0001-01-01");
            job.add("room1av", 0);
            job.add("room1my", 0);
            job.add("room2av", 0);
            job.add("room2my", 0);
            job.add("room3av", 0);
            job.add("room3my", 0);
            job.add("loggedin", false);
            job.add("admin", hdb.isAdmin(uandp[0]));
            job.add("username", uandp[0]);
            job.add("generaltext", "LOGIN UNSUCCESFUL");
        }
        return job.build();
    }

    private JsonObject refresh(String refr) {
        String[] dandu = refr.split("#");
        if(dandu.length!=3) return erronousRequest("invalid username or password");
        for(String str : dandu){
            if(str == null || str.isEmpty()) return erronousRequest("invalid username");
        }
        String formattedDate = dandu[1];
        String username = dandu[0];
        JsonObjectBuilder job = Json.createObjectBuilder();
        if (hdb.exists(username)) {
            job.add("date", formattedDate);
            job.add("room1av", roomav(1, formattedDate));
            job.add("room1my", roommy(1, formattedDate, username));
            job.add("room2av", roomav(2, formattedDate));
            job.add("room2my", roommy(2, formattedDate, username));
            job.add("room3av", roomav(3, formattedDate));
            job.add("room3my", roommy(3, formattedDate, username));
            job.add("loggedin", true);
            job.add("admin", Boolean.parseBoolean(dandu[2]));
            job.add("username", username);
            job.add("generaltext", "Page refreshed for: " + username);
        }
        else{
            job.add("date", formattedDate);
            job.add("room1av", 0);
            job.add("room1my", 0);
            job.add("room2av", 0);
            job.add("room2my", 0);
            job.add("room3av", 0);
            job.add("room3my", 0);
            job.add("loggedin", true);
            job.add("admin", Boolean.parseBoolean(dandu[2]));
            job.add("username", username);
            job.add("generaltext", "Could not find user " + username);
        }

        return job.build();
    }

    public JsonObject handleRequest(JsonObject json) {
        String jreqtype = cleanup(json.get("requesttype").toString());
        String jreq = cleanup(json.get("request").toString());
        switch (jreqtype) {
            case "login":
                return login(jreq);
            case "newacc":
                return newacc(jreq);
            case "refresh":
                return refresh(jreq);
            case "makeorder":
                return makeorder(jreq);
            default:
                return erronousRequest("Weird request recieved! Shutting down!");
        }
    }

    public JsonObject erronousRequest(String reason) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("date", "0001-01-01");
        job.add("room1av", 0);
        job.add("room1my", 0);
        job.add("room2av", 0);
        job.add("room2my", 0);
        job.add("room3av", 0);
        job.add("room3my", 0);
        job.add("loggedin", false);
        job.add("username", "-");
        job.add("admin", false);
        job.add("generaltext", reason);
        return job.build();
    }

    private String cleanup(String toClean) {
        return toClean.replaceAll("\"", " ").trim();
    }
}
