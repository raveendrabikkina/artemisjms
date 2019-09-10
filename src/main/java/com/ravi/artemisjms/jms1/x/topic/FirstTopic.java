package com.ravi.artemisjms.jms1.x.topic;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstTopic {
    public static void main(String[] args) throws NamingException, JMSException {
        InitialContext initialContext = new InitialContext();
        Topic topic = (Topic) initialContext.lookup("topic/myTopic");
        ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession();
        MessageProducer producer = session.createProducer(topic);
        MessageConsumer consumer1 = session.createConsumer(topic);
        MessageConsumer consumer2 = session.createConsumer(topic);

        TextMessage textMessage = session.createTextMessage("Hello Topic");
        producer.send(textMessage);

        //Tell jms broker that consumers are ready to receive.
        connection.start();
        TextMessage receive1 = (TextMessage) consumer1.receive(1000);
        TextMessage receive2 = (TextMessage) consumer2.receive(1000);

        System.out.println(receive1.getText());
        System.out.println(receive2.getText());
        connection.close();
        initialContext.close();
    }
}
