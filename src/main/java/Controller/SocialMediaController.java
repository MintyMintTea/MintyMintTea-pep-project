package Controller;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;

import java.sql.ResultSet;
import java.util.List;

import org.mockito.internal.matchers.Null;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("/register", this::postRegisterHandler);
        app.post("/login",this::postLoginHandler);
        app.post("/messages",this::postMessagesHandler);
        app.get("/messages",this::getAllMessagesHandler);
        app.get("/messages/{message_id}",this::getMessagesByMessageIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByMessageId);
        app.patch("/messages/{message_id}", this::updateMessageByMessageId);
        app.get("/accounts/{account_id}/messages", this::getallMessagesByAccountId);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void postRegisterHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount != null && account.username.isEmpty()==false && account.password.length()>=4){
            ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    private void postLoginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loginAccount = accountService.postLogin(account.username, account.password);
        //if and only if the username and password provided in the request body JSON match a real account 
        if(loginAccount != null){
            ctx.json(mapper.writeValueAsString(loginAccount));
            ctx.status(200);
            
        }
        else{
            ctx.status(401);
        }

    }

    private void postMessagesHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        System.out.println(message+ "postMessageHandler");
        Message newMessage = messageService.addMessage(message);
        System.out.println(newMessage+"newMessageHandler");
        if(newMessage != null /*&& message.message_text.length() <= 255 && message.message_text.length()>0*/){
            //Message addedMessage = messageService.addMessage(message);
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(newMessage));
        }
        else{
            ctx.status(400);
        }

    }

    private void getAllMessagesHandler(Context ctx){
        ctx.json(messageService.getAllMessages());
        ctx.status(200);
        
    }

    private void getMessagesByMessageIdHandler(Context ctx){
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        System.out.println(message_id+"getmessagebyMessageID");
        if(messageService.getMessageById(message_id)== null){
            ctx.status(200);
        }else{
        ctx.json(messageService.getMessageById(message_id));
        ctx.status(200);
    }
    }

    private void deleteMessageByMessageId(Context ctx)throws JsonProcessingException, JsonMappingException{
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(ctx.pathParam("message_id")); 
        Message updatedMessage = messageService.deleteMessageByMessageId(message_id);

        System.out.println(updatedMessage+"delete message by id");
        if(updatedMessage != null){
            ctx.json(mapper.writeValueAsString(updatedMessage)); /////////
            //ctx.status(200);
        }
        else{
            ctx.body();//////////
        }
    }

    private void updateMessageByMessageId(Context ctx)throws JsonProcessingException,JsonMappingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(),Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message existingMessage = messageService.updateMessage(message_id,message);
        if(existingMessage != null){
            ctx.json(mapper.writeValueAsString(existingMessage));
            ctx.status(200); 
            
        }
        else{
            ctx.status(400);
            
        }
    }

    private void getallMessagesByAccountId(Context ctx) throws JsonMappingException, JsonProcessingException{
        int posted_by = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesFromUserGivenAccountId(posted_by);
        if(messages != null){

        }
        System.out.println(posted_by+"postedby");
        ctx.json(messageService.getAllMessagesFromUserGivenAccountId(posted_by));

    }


}