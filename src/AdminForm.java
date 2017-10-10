import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminForm {
    private JComboBox topicComboBox;
    private JButton addMessageButton;
    private JTextArea meassageInput;
    private JPanel rootPane;
    static UserForm userForm=new UserForm();
    Session session;
    Connection connection = null;
    String[] topicTexts=new String[]{"游戏","学习","放纵"};
    Topic[] topics = new Topic[3];
    MessageProducer[] producers = new MessageProducer[3];

    MessageConsumer[] consumers = new MessageConsumer[3];
    public static void setUIFont (javax.swing.plaf.FontUIResource f){
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put (key, f);
        }
    }
    private AdminForm() {

        for (int i = 0; i < 3; i++) {
            topicComboBox.addItem(topicTexts[i]);
        }
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory("vm://localhost");
            connection = factory.createConnection();
            connection.start();
            for (int i = 0; i < 3; i++) {
                topics[i] = new ActiveMQTopic(topicTexts[i]);
            }
             session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            for (int i = 0; i < 3; i++) {
                producers[i] = session.createProducer(topics[i]);
            }
            for (int i = 0; i < 3; i++) {
                consumers[i] = session.createConsumer(topics[i]);
                int finalI = i;
                consumers[i].setMessageListener(new MessageListener() {
                    @Override
                    public void onMessage(Message message) {
                        try {
                            userForm.dealMessage(finalI,message);
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
        addMessageButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = topicComboBox.getSelectedIndex();
                try {
                    String text=meassageInput.getText();
                    System.out.println(text);
                    Message t = session.createTextMessage(text);
                    producers[i].send(t);
                } catch (JMSException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        setUIFont(new javax.swing.plaf.FontUIResource("微软雅黑", Font.BOLD,20));  //统一设置字体
        JFrame frame = new JFrame("AdminForm");
        AdminForm t = new AdminForm();
        frame.setContentPane(t.rootPane);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        startUser();
    }

    public static void startUser() {
        JFrame frame = new JFrame("UserForm");
        frame.setContentPane(userForm.root_pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
