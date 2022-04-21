package fr.legrain.bdg.webapp;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

//import org.primefaces.push.annotation.OnMessage;
//import org.primefaces.push.annotation.PushEndpoint;
//import org.primefaces.push.impl.JSONEncoder;

@ServerEndpoint("/notifications")
public class PushMessageResourceProgramme {
	
//    @OnMessage(encoders = {JSONEncoder.class})
//    public String onMessage(String message) {
//    	System.out.println("PushMessageResource : "+message);
//        return message;
//    }
    
    @OnMessage/*(encoders = {JSONEncoder.class})*/
    public String onMessage(String message) {
        System.out.println("onMessage - programme");
        return message;
    }
}