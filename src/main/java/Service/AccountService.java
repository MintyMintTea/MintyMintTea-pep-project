package Service;
import Model.Account;

import java.util.List;

import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public List<Account> getAllAccounts(){
        return accountDAO.getAllAccounts();
    }

    public Account addAccount(Account account){
        if(accountDAO.getAccountByID(account.getAccount_id())!= null){
            System.out.println("get account = null");
            return null;
        }   
        System.out.println("getaccount != null");
        return accountDAO.addAccount(account);
    }

    public Account postLogin(String username, String password) {
        // if(accountDAO.getAccountByID(account.getAccount_id()) == null){ 
        //     System.out.println(accountDAO.getAccountByID(account.getAccount_id())+"postLogin");
        //     return null;
        // }
        
        //return accountDAO.getAccountByID(account.getAccount_id());
        return accountDAO.geAccountbyUsernameAndPassword(username, password);
    }

}
