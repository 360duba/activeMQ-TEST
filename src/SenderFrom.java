import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SenderFrom implements MessageListener {
    private JTextArea messageList;
    private JButton sendButton;
    private JPanel senderPanel;
    private JTextField messageInput;
    private JTextField nameInput;
    Session session;
    MessageProducer producer;

    Connection connection = null;
    public static void setUIFont (javax.swing.plaf.FontUIResource f){
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put (key, f);
        }
    }
    public SenderFrom() {

        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("queue_lesson");
            producer = session.createProducer(destination);
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(this);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        sendButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = messageInput.getText();
                try {
                    Message message = session.createMessage();
                    message.setStringProperty("user",nameInput.getText());
                    message.setStringProperty("message",messageInput.getText());

                    messageList.append("\n"+nameInput.getText()+":\t"+messageInput.getText());
                    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                    producer.send(message);
                } catch (JMSException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        setUIFont(new javax.swing.plaf.FontUIResource("微软雅黑",Font.BOLD,20));  //统一设置字体
        JFrame frame = new JFrame("SenderFrom");
        frame.setContentPane(new SenderFrom().senderPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void onMessage(Message message) {
        try {
            String user = message.getStringProperty("user");
            String text = message.getStringProperty("message");
            System.out.println(text);
            messageList.append("\n"+user+":\t"+text);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
