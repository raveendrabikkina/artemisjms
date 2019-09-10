package com.ravi.artemisjms.jms1.x.queue;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;


public class QueueBrowserDemo {

    public static void main(String[] args) {
        InitialContext initialContext = null;
        Connection connection = null;
        try {
            initialContext = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            Session session = connection.createSession();

            Queue myQueue = (Queue) initialContext.lookup("queue/myQueue");
            producer(session, myQueue);
            consumer(connection, session, myQueue);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (initialContext != null) {
                try {
                    initialContext.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    private static void consumer(Connection connection, Session session, Queue myQueue) throws JMSException {
        connection.start();

        QueueBrowser browser = session.createBrowser(myQueue);
        Enumeration enumeration = browser.getEnumeration();
        while (enumeration.hasMoreElements()) {
            TextMessage textMessage = (TextMessage) enumeration.nextElement();
            System.out.println("Message received:" + textMessage.getText());
        }
        //We need to invoke start to inform producer that we are ready to receive messages.
    }

    private static void producer(Session session, Queue myQueue) throws JMSException {
        String message1 = "Hello JMS: Message1";
        String message2 = "Hello JMS: Message2";
        TextMessage textMessage1 = session.createTextMessage(message1);
        TextMessage textMessage2 = session.createTextMessage(message2);
        MessageProducer producer = session.createProducer(myQueue);
        producer.send(textMessage1);
        producer.send(textMessage2);

        System.out.println("Message1 Sent:" + message1);
        System.out.println("Message2 Sent:" + message2);
    }
}
