package Controller;

import BusinessObjects.Factory;
import BusinessObjects.User.Administrator;
import BusinessObjects.User.StanderdUser;
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
    boolean userOnline;
    User logedIn;

    private static final Map<Integer, String> loginMenu;
    static {
        loginMenu = new HashMap<Integer, String>();
        loginMenu.put(1, "Login");
        loginMenu.put(9, "Exit");
    }

    private static final Map<Integer, String> operationsMenu;
    static {
        operationsMenu = new HashMap<Integer, String>();
        operationsMenu.put(1, "Users");
        operationsMenu.put(2, "Providers");
        operationsMenu.put(3, "Patient");
        operationsMenu.put(4, "Procedure");
        operationsMenu.put(5, "Appointment");
        operationsMenu.put(9, "Log Out");

    }

    private static final Map<Integer, String> adminUserMenu;
    static {
        adminUserMenu = new HashMap<Integer, String>();
        adminUserMenu.put(1, "Change Your Password");
        adminUserMenu.put(2, "Change Another Users Password");
        adminUserMenu.put(9, "Back");

    }

    private static final Map<Integer, String> standardUserMenu;
    static {
        standardUserMenu = new HashMap<Integer, String>();
        standardUserMenu.put(1, "Change Your Password");
        standardUserMenu.put(9, "Back");

    }

    /**
     * The default constructor for the ViewOperator
     */
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


    /**
     * runs the operations
     * @throws IOException
     */
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
                        userOnline = true;
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

    private void startOperations() throws IOException {
        int selection = out.promptForMenu(operationsMenu);
        while (userOnline) {
            switch (selection) {
                case 1:
                    editUsers();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 9:
                    userOnline = false;
                    break;
                default:
                    throw new IllegalArgumentException("Inlaid selection");

            }
        }
    }

    private void editUsers() throws IOException {
        int selection = 0;
        if(logedIn instanceof Administrator){
            selection = out.promptForMenu(adminUserMenu);
        } else if(logedIn instanceof StanderdUser){
            selection = out.promptForMenu(standardUserMenu);
        }

        switch (selection){
            case 1:
                changePassword(logedIn);
                break;
            case 2:
                out.display("Select A User...");
                Map<Integer,User> userMap = controller.getUserList().getUserMap();
                int selectedUser = out.promptForInt(userMap.toString());
                changePassword(userMap.get(selectedUser));
                break;
            case 9:
                out.display("Returning...");
                break;
            default:
                throw new IllegalArgumentException("Invalid Selection");
        }
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
