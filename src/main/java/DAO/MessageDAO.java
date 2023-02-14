package DAO;

import Model.Message;
import Util.ConnectionUtil;

import static org.mockito.ArgumentMatchers.nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.validator.PublicClassValidator;

public class MessageDAO {

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "Select * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getInt("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try{
        String sql = "SELECT * FROM message WHERE message_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, id);
        
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            System.out.println("");
            Message message = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
                System.out.println("message");
            return message;
        }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        System.out.println("going null");
        return null;
    }

    public void deleteMessageByMessageId(int id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO message(posted_by,message_text,time_posted_epoch) VALUES (?,?,?);"; 
            PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.time_posted_epoch);

            preparedStatement.executeUpdate();
            ResultSet pkResultSet = preparedStatement.getGeneratedKeys();
            //ResultSet rs = preparedStatement.executeQuery();
            if(pkResultSet.next()){
                int generated_message_id = pkResultSet.getInt(1);
                return new Message(generated_message_id,message.getPosted_by(),message.getMessage_text(),message.getTime_posted_epoch());
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessagesFromUserGivenAccountId (int account_id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql =  "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, account_id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"), 
                        rs.getString("message_text"), 
                        rs.getLong("time_posted_epoch"));
                    messages.add(message);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message updatMessageById(Message message, int id){
        Connection connection=ConnectionUtil.getConnection();
        try{
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            System.out.println(message.message_text+"message_text");
            System.out.println(message.getMessage_text()+"getmessagetext");

            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
            return getMessageById(id);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}
