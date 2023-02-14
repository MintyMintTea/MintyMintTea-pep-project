package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;


public class MessageService {
    MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id){
        return messageDAO.getMessageById(id);
    }

    public Message addMessage (Message message){
        if((!message.getMessage_text().isBlank()) && message.getMessage_text().length() <255){
            return messageDAO.insertMessage(message);
        }
        return null;
    }
    public Message updateMessage(int id,Message message){
        System.out.println(messageDAO.getMessageById(id)+"updatemessage");
        // if(messageDAO.getMessageById(id)==null){
        //     return messageDAO.updatMessageById(message, id);
        // }
        if(message.message_text != "" && message.message_text.length() <= 255){
            return messageDAO.updatMessageById(message, id);
        }
        return null;
        
    }

    public Message deleteMessageByMessageId(int message_id) {
        // Message message = messageDAO.getMessageById(message_id);
        // messageDAO.deleteMessageByMessageId(message_id);
        // System.out.println(message_id+"messageservic");
        // if(message==null){
        //     return null;
        // }
        // return message;
        System.out.println(messageDAO.getMessageById(message_id)+"Delete Message by id");
        if(messageDAO.getMessageById(message_id)!=null){
            Message message = messageDAO.getMessageById(message_id);
            messageDAO.deleteMessageByMessageId(message_id);
            return message;
        }
        return null;
    }

    public List<Message> getAllMessagesFromUserGivenAccountId(int account_id){
        return messageDAO.getAllMessagesFromUserGivenAccountId(account_id);
    }
}
