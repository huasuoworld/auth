package hua.world.mq;

import java.util.Map;

import org.springframework.jms.core.JmsTemplate;
import javax.websocket.OnClose;

public class MessageSender {

    private final JmsTemplate jmsTemplate;

    public MessageSender(final JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send(final Map map) {
        jmsTemplate.convertAndSend(map);
    }

}
