package Controller;

import BusinessObjects.Factory;
import BusinessObjects.Patient.Patient;
import BusinessObjects.Provider.Provider;
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
        operationsMenu.put(6, "View Balance");
        operationsMenu.put(9, "Log Out");

    }

    private static final Map<Integer, String> adminUserMenu;
    static {
        adminUserMenu = new HashMap<Integer, String>();
        adminUserMenu.put(1, "Change Your Password");
        adminUserMenu.put(2, "Change Another Users Password");
        adminUserMenu.put(3, "Create New Standard User");
        adminUserMenu.put(4, "Create New Administrative User");
        adminUserMenu.put(5, "Remove User");
        adminUserMenu.put(9, "Back");

    }

    private static final Map<Integer, String> providerMenu;
    static {
        providerMenu = new HashMap<Integer, String>();
        providerMenu.put(1, "Search Providers");
        providerMenu.put(2, "Add A Provider");
        providerMenu.put(3, "Remove A Provider");
        providerMenu.put(9, "Back");

    }

    private static final Map<Integer, String> standardUserMenu;
    static {
        standardUserMenu = new HashMap<Integer, String>();
        standardUserMenu.put(1, "Change Your Password");
        standardUserMenu.put(9, "Back");
    }

    private static final Map<Integer, String> patientsMenu;
    static {
        patientsMenu = new HashMap<Integer, String>();
        patientsMenu.put(1, "Search Patients");
        patientsMenu.put(2, "View A Patient");
        patientsMenu.put(3, "Add New Patient");
        patientsMenu.put(4, "Remove A Patient");
        patientsMenu.put(9, "Back");

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
                    editProviders();
                    break;
                case 3:
                    editPatients();
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
        boolean editing = true;
        while (editing) {
            switch (selection) {
                case 1:
                    changePassword(logedIn);
                    break;
                case 2:
                    User passwordUser = getUserFromMap();
                    changePassword(passwordUser);
                    break;
                case 3:
                    String userName = out.promptForString("Enter UserName");
                    String password = out.promptForString("Enter Password");
                    String firstName = out.promptForString("Enter First Name");
                    String lastName = out.promptForString("Enter Last Name");
                    User addUser = factory.getStandardUserInstance(userName, password, firstName, lastName);
                    controller.addUser(addUser);
                    break;
                case 4:
                    String adminUserName = out.promptForString("Enter UserName");
                    String adminPassword = out.promptForString("Enter Password");
                    String adminFirstName = out.promptForString("Enter First Name");
                    String adminLastName = out.promptForString("Enter Last Name");
                    User adminAddUser = factory.getAdministratorUserInstance(adminUserName, adminPassword, adminFirstName, adminLastName);
                    controller.addUser(adminAddUser);
                    break;
                case 5:
                    User removeUser = getUserFromMap();
                    controller.removeUser(removeUser);
                    break;
                case 9:
                    editing = false;
                    out.display("Returning...");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid Selection");
            }
        }
    }

    private void editProviders() throws IOException {
        int selection = out.promptForMenu(providerMenu);
        boolean editing = true;
        while (editing) {
            switch (selection) {
                case 1:
                    String firstName = out.promptForString("Enter First Name (leave blank for all results)");
                    String lastName = out.promptForString("Enter Last Name (leave blank for all results)");
                    String title = out.promptForString("Enter Title (leave blank for all results)");
                    for(Provider provider : controller.searchProviders(firstName,lastName,title)){
                        provider.toString();
                    }
                    break;
                case 2:
                    String addFirstName = out.promptForString("Enter FirstName");
                    String addLastName = out.promptForString("Enter LastName");
                    String addTitle = out.promptForString("Enter Title");
                    int addId = out.promptForInt("Enter ID");
                    Provider addProvider = factory.getProviderInstance(addFirstName,addLastName,addTitle,addId);
                    controller.addProvider(addProvider);
                    break;
                case 3:
                    Provider removeProvider = getProviderFromMap();
                    controller.removeProvider(removeProvider);
                    break;
                case 9:
                    editing = false;
                    out.display("Returning...");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid selection");
            }
        }
    }

    private void editPatients() throws IOException {
        int selection = out.promptForMenu(patientsMenu);
        boolean editing = true;
        while(editing){
            switch (selection) {
                case 1:
                    //Search
                    String searchFirstName = out.promptForString("Enter First Name (leave blank for all results)");
                    String searchLastName = out.promptForString("Enter Last Name (leave blank for all results)");
                    String searchInsurence = out.promptForString("Enter Insurance Company Name (leave blank for all results)");
                    for(Patient patient : controller.searchPatients(searchFirstName,searchLastName,searchInsurence)){
                        patient.toString();
                    }
                    break;
                case 2:
                    //View
                    break;
                case 3:
                    //Add
                    break;
                case 4:
                    //Remove
                    break;
                case 9:
                    editing = false;
                    break;
                default:
                    throw new IllegalArgumentException("Inlaid Selection");
            }
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

    private User getUserFromMap() throws IOException {
        out.display("Select A User...");
        Map<Integer,User> userMap = controller.getUserList().getUserMap();
        int selectedUser = out.promptForInt(userMap.toString());
        return userMap.get(selectedUser);
    }

    private Provider getProviderFromMap() throws IOException {
        out.display("Select A User...");
        Map<Integer,Provider> providerMap = controller.getProviderList().getProviderMap();
        int selectedProvider = out.promptForInt(providerMap.toString());
        return providerMap.get(selectedProvider);
    }


}
