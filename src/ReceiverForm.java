import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReceiverForm implements MessageListener {
    private JPanel receiverForm;
    private JTextPane messagePane;
    Connection connection;

    public ReceiverForm() {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();

        try {
            connection = connectionFactory.createConnection();

            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createQueue("queue_lesson");

            MessageConsumer consumer = session.createConsumer(destination);

            consumer.setMessageListener(this);


        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ReceiverForm");
        frame.setContentPane(new ReceiverForm().receiverForm);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void onMessage(Message message) {
        try {
            messagePane.setText(messagePane.getText() + "\n" + ((TextMessage) message).getText());
            System.out.println(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
/*
mongod.exe --bind_ip localhost --logpath "F:\data\dbConf\mongodb.log" --logappend --dbpath "F:\data\db" --port 27017 --serviceName "mongo" --serviceDisplayName "mongo" --install
 */
