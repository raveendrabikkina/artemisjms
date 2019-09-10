package com.ravi.artemisjms.jms2.x;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Simple API
 * JMSContext = Connection + Session
 * JMSProducer in 2.x vs MessageProducer in 1.x
 * We can use @Inject and @Resource to lookup and we can create custom connectionFactory rather thatn getting default implementation using @JMSConnectionFactoryDefinitions and @JMSConnectionFactoryDefinition
 * or using xml <jms-connection-factory></jms-connection-factory>
 *
 * @Inject
 * @JMSConnectionFactory("jms/connectionFactory") private JMSContext context;
 * @Resource("jms/dataQueue") private Queue dataQueue;
 */
public class JMSContextDemo {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");
        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {
            //What ever we have done using session can be done with jmsContext.
            jmsContext.createProducer().send(queue, "Hello and welcome to JMS2.x");
            //With JMS 2.x we no need to create Message Object.
            String messageReceived = jmsContext.createConsumer(queue).receiveBody(String.class);
            System.out.println(messageReceived);
        }
    }
}
