package com.ravi.artemisjms.jms1.x.queue;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstQueue {


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
        MessageConsumer consumer = session.createConsumer(myQueue);
        //We need to invoke start to inform producer that we are ready to receive messages.
        connection.start();
        TextMessage messageReceived = (TextMessage) consumer.receive(5000);
        System.out.println("Message Received:" + messageReceived.getText());
    }

    private static void producer(Session session, Queue myQueue) throws JMSException {
        String message = "Hello JMS";
        TextMessage textMessage = session.createTextMessage(message);
        MessageProducer producer = session.createProducer(myQueue);
        producer.send(textMessage);
        System.out.println("Message Sent:" + message);
    }
}
