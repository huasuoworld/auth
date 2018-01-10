package hua.world.mq;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.stereotype.Component;
  
  
@ServerEndpoint(value = "/api/websocket/storeGuest") 
@Component
public class StoreGuestWebSocket {  
  
    private static final Log log = LogFactory.getLog(StoreGuestWebSocket.class);  
  
    private static final String GUEST_PREFIX = "Guest_";  
    private static final AtomicInteger connectionIds = new AtomicInteger(0);  
    private static final Map<String,Object> connections = new HashMap<String,Object>();  
  
    private final String nickname;  
    private Session session;  
    
    public String getCurrentUser() {
    	return this.nickname + this.session.getQueryString();
    }
  
    public StoreGuestWebSocket() {  
        nickname = GUEST_PREFIX;  
    }  
  
  
    @OnOpen  
    public void start(Session session) {  
    	this.session = session;  
//        String qs = session.getQueryString();
        connections.put(getCurrentUser(), this);   
        String message = String.format("* %s %s", getCurrentUser(), "has joined.");  
        broadcast(message, true, getCurrentUser());  
    }  
  
  
    @OnClose  
    public void end() throws IOException {  
        connections.remove(this);  
        String message = String.format("* %s %s",  
                nickname, "has disconnected.");  
        broadcast(message, true, getCurrentUser());  
        this.session.close();
    }  
  
  
    /** 
     * 消息发送触发方法 
     * @param message 
     */  
    @OnMessage  
    public void incoming(String message) {  
        // Never trust the client  
        String filteredMessage = String.format("%s: %s",  
                nickname, HTMLFilter.filter(message.toString()));  
        broadcast(message, true, getCurrentUser());  
    }  
  
    @OnError  
    public void onError(Throwable t) throws Throwable {  
        log.error("Chat Error: " + t.toString(), t);  
    }  
  
    /** 
     * 消息发送方法 
     * @param msg 
     */  
    private void broadcast(String msg, boolean single, String user) {  
        if(single){  
            sendUser(msg, user);  
        } else{  
            sendAll(msg);  
        }  
    }   
      
    /** 
     * 向所有用户发送 
     * @param msg 
     */  
    public void sendAll(String msg){  
        for (String key : connections.keySet()) {  
            StoreGuestWebSocket client = null ;  
            try {  
                client = (StoreGuestWebSocket) connections.get(key);  
                if(client == null) {
                	this.end();
                }
                synchronized (client) {  
//                    client.session.getBasicRemote().sendObject(msg);  
                    client.session.getBasicRemote().sendText(msg);
                }  
            } catch (Exception e) {   
                log.debug("Chat Error: Failed to send message to client", e);  
                connections.remove(client);  
                try {  
                    client.session.close();  
                } catch (IOException e1) {  
                    // Ignore  
                }  
                String message = String.format("* %s %s",  
                        client.nickname, "has been disconnected.");  
                broadcast(message.toString(), false, key);  
            }  
        }  
    }  
      
    /** 
     * 向指定用户发送消息  
     * @param msg 
     */  
    public void sendUser(String msg, String user){  
        StoreGuestWebSocket c = (StoreGuestWebSocket) connections.get(user);  
        try {  
            c.session.getBasicRemote().sendText(msg);  
        } catch (IOException e) {  
            log.debug("Chat Error: Failed to send message to client", e);  
            connections.remove(c);  
            try {  
                c.session.close();  
            } catch (IOException e1) {  
                // Ignore  
            }  
            String message = String.format("* %s %s",  
                    c.nickname, "has been disconnected.");  
            broadcast(message, true, user);    
        }   
    }  
}  
