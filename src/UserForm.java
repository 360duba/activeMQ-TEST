import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.*;
import javax.swing.*;

public class UserForm {
    private JCheckBox 游戏CheckBox;
    private JCheckBox 学习CheckBox;
    private JCheckBox 放纵CheckBox;
    private JTextArea messageShow;
    JPanel root_pane;

    Session session;
    Connection connection = null;
    String[] topicTexts=new String[]{"游戏","学习","放纵"};
    Topic[] topics = new Topic[3];
    public UserForm(){
    }
    public void dealMessage(int index ,Message message) throws JMSException {
        switch (index){
            case 0:
                if (!游戏CheckBox.isSelected()){
                    return;
                }
                break;
            case 1:
                if (!学习CheckBox.isSelected()){
                    return;
                }
                break;
            case 2:
                if (!放纵CheckBox.isSelected()){
                    return;
                }
                break;
        }
        messageShow.append("\n"+topicTexts[index]+":"+((TextMessage)message).getText());
    }
}
