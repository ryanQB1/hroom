package server.net;

import java.io.IOException;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.json.JsonObject;
import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import server.controller.hroomcontroller;

@Stateful
@ServerEndpoint(value="/hroomendpoint", encoders = {PageInfoEncoder.class}, decoders = {PageRequestDecoder.class})
public class hroomendpoint {
    
    @EJB
    private final hroomcontroller contr = new hroomcontroller();

    @OnMessage
    public void onMessage(PageRequest req, Session session) throws IOException, EncodeException{
        System.out.println("Page request recieved: " + req.getJson().toString());
        JsonObject toSend = contr.handleRequest(req.getJson());
        session.getBasicRemote().sendObject(toSend);
    }

}
