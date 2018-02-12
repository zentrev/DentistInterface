package Controller;

import BusinessObjects.Factory;
import BusinessObjects.User.User;
import BusinessObjects.User.UserList;
import View.TextView;

import java.io.IOException;

public class ViewOperator {

    TextView out;
    Controller controller;
    Factory factory;

    public ViewOperator(){
        out = new TextView();
        factory = new Factory();
        try {
            controller = new Controller();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void runOperator(){

        checkFirstUsers();
        while(login());


    }

    public void checkFirstUsers(){
        if(controller.getUserList().size() < 1){
            User admin = factory.getAdministratorUserInstance();
            admin.setUserName("Administrator");
            admin.setPassword("1234Password");
            controller.addUser(admin);
        }
    }

    public boolean login(){
        String userName = out.promptForString("UserName:");
        String password = out.promptForString("Password:");
        for (User user : controller.getUserList()){
            if(user.verifyLogin(userName,password)){
                return true;
            }
        }
        out.dispaly("Invalid Login, try again...");
        return false;
    }


}
