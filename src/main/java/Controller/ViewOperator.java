package Controller;

import BusinessObjects.Factory;
import BusinessObjects.User.User;
import View.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewOperator {

    TextView out;
    Controller controller;
    Factory factory;

    boolean isRuning;
    User logedIn;

    private static final Map<Integer, String> loginMenu;
    static
    {
        loginMenu = new HashMap<Integer, String>();
        loginMenu.put(1, "Login");
        loginMenu.put(9, "Exit");
    }

    public ViewOperator(){
        isRuning = true;
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



    public void runOperator() throws IOException {

        checkFirstUsers();
        while(isRuning) {
            switch (out.promptForMenu(loginMenu)){
                case 1:
                    logedIn = login();
                    if(logedIn != null) {
                        if (logedIn.getUserName().equals("Administrator") && logedIn.getPassword().equals("1234Password")) {
                            out.display("This is your default login, You must change your password");
                            changePassword(logedIn);
                        }
                        startOperations();
                    }
                    break;
                case 2:
                    isRuning = false;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid Selection");
            }

        }

    }

    public void startOperations(){

    }

    private void checkFirstUsers(){
        if(controller.getUserList().size() < 1){
            User admin = factory.getAdministratorUserInstance();
            admin.setUserName("Administrator");
            admin.setPassword("1234Password");
            controller.addUser(admin);
        }
    }

    private User login() throws IOException {
        String userName = out.promptForString("UserName:");
        String password = out.promptForString("Password:");
        for (User user : controller.getUserList()){
            if(user.verifyLogin(userName,password)){
                return user;
            }
        }
        out.display("Invalid Login, try again...");
        return null;
    }

    private void changePassword(User user) throws IOException {
        String newPassword = out.promptForString("New Password:");
        user.setPassword(newPassword);
        out.display("Password Changed...");
    }


}
