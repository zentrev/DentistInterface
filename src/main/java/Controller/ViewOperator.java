package Controller;

import BusinessObjects.Appointment.Appointment;
import BusinessObjects.Appointment.AppointmentList;
import BusinessObjects.Factory;
import BusinessObjects.Patient.Patient;
import BusinessObjects.Procedure.Procedure;
import BusinessObjects.Procedure.ProcedureList;
import BusinessObjects.Provider.Provider;
import BusinessObjects.User.Administrator;
import BusinessObjects.User.StanderdUser;
import BusinessObjects.User.User;
import View.TextView;
import java.io.IOException;
import java.util.*;

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
        patientsMenu.put(2, "View and Change Patient Information");
        patientsMenu.put(3, "Add New Patient");
        patientsMenu.put(4, "Remove A Patient");
        patientsMenu.put(9, "Back");
    }

    private static final Map<Integer, String> patientViewMenu;
    static {
        patientViewMenu = new HashMap<Integer, String>();
        patientViewMenu.put(1, "View Patient Information");
        patientViewMenu.put(2, "Add Appointment");
        patientViewMenu.put(3, "Remove Appointment");
        patientViewMenu.put(4, "Make Payment");
        patientViewMenu.put(5, "View Balance");
        patientViewMenu.put(6, "View Appointments");
        patientViewMenu.put(9, "Back");
    }

    private static final Map<Integer, String> procedureCreation;
    static {
        procedureCreation = new HashMap<Integer, String>();
        procedureCreation.put(1, "Add Procedure");
        procedureCreation.put(9, "Stop Adding Procedures");
    }

    private static final Map<Integer, String> procedureMenu;
    static {
        procedureMenu = new HashMap<Integer, String>();
        procedureMenu.put(1, "View Available Procedures");
        procedureMenu.put(2, "Add Procedure");
        procedureMenu.put(3, "Remove Procedure");
        procedureMenu.put(9, "Exit");
    }

    private static final Map<Integer, String> appointmentMenu;
    static {
        appointmentMenu = new HashMap<Integer, String>();
        appointmentMenu.put(1, "View Appointments");
        appointmentMenu.put(2, "Add Appointment");
        appointmentMenu.put(3, "Remove Appointment");
        appointmentMenu.put(9, "Exit");
    }

    private static final Map<Integer, String> balanceMenu;
    static {
        balanceMenu = new HashMap<Integer, String>();
        balanceMenu.put(1, "Production");
        balanceMenu.put(2, "Patient Balance");
        balanceMenu.put(9, "Exit");
    }

    private static final Map<Integer, String> intervals;
    static {
        intervals = new HashMap<Integer, String>();
        intervals.put(1, "Day");
        intervals.put(2, "Week");
        intervals.put(3, "Month");
        intervals.put(4, "Year");
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
                case 9:
                    isRuning = false;
                    break;
                default:
                    out.display("Invalid Selection, Try Again...");
            }

        }

    }

    private void startOperations() throws IOException {
        while (userOnline) {
            int selection = out.promptForMenu(operationsMenu);
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
                    editProcedures();
                    break;
                case 5:
                    editAppointments();
                    break;
                case 6:
                    viewBalance();
                    break;
                case 9:
                    userOnline = false;
                    controller.saveAll();
                    break;
                default:
                    throw new IllegalArgumentException("Inlaid selection");

            }
        }
    }

    private void viewBalance() throws IOException {
        boolean viewing = true;
        while(viewing){
            switch(out.promptForMenu(balanceMenu)){
                case 1:

                    Calendar minDate = out.promptForDate("Enter Starting Date");
                    Calendar maxDate = out.promptForDate("Enter Ending Date");
                    Interval interval = null;
                    boolean askin = true;
                    while(askin) {
                        askin = false;
                        switch (out.promptForMenu(intervals)) {
                            case 1:
                                interval = Interval.DAY;
                                break;
                            case 2:
                                interval = Interval.Week;
                                break;
                            case 3:
                                interval = Interval.Month;
                                break;
                            case 4:
                                interval = Interval.Year;
                                break;
                            default:
                                out.display("Invalid selection");
                                askin = true;
                        }
                    }

                    Map<Calendar,Double> tempMap = controller.getProduction(minDate,maxDate,interval);
                    Set set = tempMap.entrySet();
                    Iterator iterator = set.iterator();
                    while(iterator.hasNext()) {
                        Map.Entry nextMap = (Map.Entry)iterator.next();
                        System.out.print("Date: "+ nextMap.getKey() + "\nValue: " + nextMap.getValue() + "\n------------------------------");
                        System.out.println(nextMap.getValue());
                    }
                    break;
                case 2:
                    Map<Patient,Double> balenceMap = controller.getBalances();
                    Set balenceSet = balenceMap.entrySet();
                    Iterator repeter = balenceSet.iterator();
                    while(repeter.hasNext()) {
                        Map.Entry nextMap = (Map.Entry)repeter.next();
                        System.out.print("Date: "+ nextMap.getKey() + "\nValue: " + nextMap.getValue() + "\n------------------------------");
                        System.out.println(nextMap.getValue());
                    }
                    break;
                case 3:
                    double collections = 0;
                    for(Patient patient : controller.getPatientList()){
                        collections += patient.getPaymentsMade();
                    }
                    out.display("Your Total Collections are $"+collections);
                case 9:
                    viewing = false;
                    out.display("Returning...");
                    break;
                default:
                    throw new IllegalArgumentException("Inlaid selection");
            }
        }
    }

    private void editAppointments() throws IOException {
        boolean editing = true;
        while(editing){
            switch(out.promptForMenu(appointmentMenu)){
                case 1:
                    controller.getAppointmentList().sortAppointmentTime();
                    for(Patient patient : controller.getPatientList()){
                        for(Appointment appointment: patient.getAppointments()){
                            out.display(appointment.toString());
                        }
                    }
                    break;
                case 2:
                    Patient patient = getPatientFromMap();
                    patientAppointment(patient);
                    break;
                case 3:
                    controller.getAppointmentList().remove(getAppointmentFromMap());
                    break;
                case 9:
                    editing = false;
                    controller.saveAppointments();
                    out.display("Returning...");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid selection");
            }
        }
    }

    private void editUsers() throws IOException {
        int selection = 0;
        boolean editing = true;

        while (editing) {
            if(logedIn instanceof Administrator){
                selection = out.promptForMenu(adminUserMenu);
            } else if(logedIn instanceof StanderdUser){
                selection = out.promptForMenu(standardUserMenu);
            }
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
                    controller.saveUsers();
                    out.display("Returning...");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid Selection");
            }
        }
    }

    private void editProviders() throws IOException {
        boolean editing = true;
        while (editing) {
            int selection = out.promptForMenu(providerMenu);
            switch (selection) {
                case 1:
                    String firstName = out.promptForString("Enter First Name (leave blank for all results)");
                    String lastName = out.promptForString("Enter Last Name (leave blank for all results)");
                    String title = out.promptForString("Enter Title (leave blank for all results)");
                    for(Provider provider : controller.searchProviders(firstName,lastName,title)){
                        out.display(provider.toString());
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
                    controller.saveProviders();
                    out.display("Returning...");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid selection");
            }
        }
    }

    private void editProcedures() throws IOException {
        boolean editing = true;
        while(editing){
            int selection = out.promptForMenu(procedureMenu);
            switch(selection){
                case 1:
                    for(Procedure procedure : controller.getProcedureList()){
                        out.display(procedure.toString());
                    }
                    break;
                case 2:
                    addProcedure();
                    break;
                case 3:
                    controller.getProcedureList().remove(getProcedureFromMap());
                    break;
                case 9:
                    editing = false;
                    controller.saveProcedures();
                    out.display("Returning...");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid selection");
            }
        }
    }

    private void addProcedure() throws IOException {
        Provider provider = getProviderFromMap();
        String code = out.promptForString("Enter Procedure Code");
        String procedureDescription = out.promptForString("Enter Description");
        double standardCharge = out.promtForDouble("Enter Standard Charge");
        Procedure procedure = factory.getProcedureInstance(provider, code, procedureDescription, standardCharge);
        controller.getProcedureList().add(procedure);
    }

    private void editPatients() throws IOException {
        boolean editing = true;
        while(editing){
            int selection = out.promptForMenu(patientsMenu);
            switch (selection) {
                case 1:
                    String searchFirstName = out.promptForString("Enter First Name (leave blank for all results)");
                    String searchLastName = out.promptForString("Enter Last Name (leave blank for all results)");
                    String searchInsurance = out.promptForString("Enter Insurance Company Name (leave blank for all results)");
                    for(Patient patient : controller.searchPatients(searchFirstName,searchLastName,searchInsurance)){
                        out.display(patient.toString());
                    }
                    break;
                case 2:
                    Patient temp = getPatientFromMap();
                    viewPatientInformation(temp);
                    break;
                case 3:
                    addNewPatient();
                    break;
                case 4:
                    removePatient();
                    break;
                case 9:
                    editing = false;
                    controller.savePatients();
                    out.display("Returning...");
                    break;
                default:
                    throw new IllegalArgumentException("Inlaid Selection");
            }
        }
    }

    private void removePatient() throws IOException {
        controller.getPatientList().remove(getPatientFromMap());
    }

    private void addNewPatient() throws IOException {
        String firstName = out.promptForString("Enter First Name");
        String lastName = out.promptForString("Enter Last Name");
        int id = out.promptForInt("Enter ID");
        long phoneNumber = out.promptForLong("Enter PhoneNumber");
        String emailAddress = out.promptForString("Enter Email Address");
        int groupId = out.promptForInt("Enter Group ID");
        int memberId = out.promptForInt("Enter Member ID");
        AppointmentList appointments = new AppointmentList();
        String insurance = out.promptForString("Enter Insurance Company");

        Patient patient = factory.getPatienInstance(firstName, lastName, id, phoneNumber,
                emailAddress, groupId, memberId, appointments, insurance);
        controller.addPatient(patient);
    }

    private void viewPatientInformation(Patient patient) throws IOException {

        boolean viewing = true;
        while (viewing){
            switch(out.promptForMenu(patientViewMenu)){
                case 1:
                    out.display(patient.toString());
                    break;
                case 2:
                    patientAppointment(patient);
                    break;
                case 3:
                    removePatientAppointment(patient);
                    break;
                case 4:
                    patientPayment(patient);
                    break;
                case 5:
                    out.display("Balance: " + controller.getBalances().get(patient));
                case 6:
                    for(Appointment appointment:patient.getAppointments()){
                        out.display(appointment.toString());
                    }
                case 9:
                    viewing = false;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid selection");
            }
        }
    }

    private void patientPayment(Patient patient) throws IOException {
        double payment = out.promtForDouble("Enter Payment Amount");
        patient.addPayment(payment);
    }

    private void removePatientAppointment(Patient patient) throws IOException {
        patient.getAppointments().remove(getAppointmentFromMap());
    }

    private void patientAppointment(Patient patient) throws IOException {
        ProcedureList procedures = getProcedures();
        Calendar date = Calendar.getInstance();
        Appointment appointment = factory.getAppointmentInsance(patient, procedures, date);
        patient.getAppointments().add(appointment);
        controller.addAppointment(appointment);
    }

    private ProcedureList getProcedures() throws IOException {
        boolean adding = true;
        ProcedureList procedures = new ProcedureList();
        while(adding){
            switch (out.promptForMenu(procedureCreation)){
                case 1:
                    procedures.add(getProcedureFromMap());
                    break;
                case 9:
                    adding = false;
                    break;
                default:
                    throw new IllegalArgumentException("Inlaid selection");
            }
        }
        return procedures;
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
        Map<Integer, String> outMap = new HashMap<>();
        for(int i=0; i<userMap.size(); i++){
            if(userMap.get(i) instanceof Administrator){
                outMap.put(i, i + ": " + userMap.get(i).getUserName()+ " - Admin");
            } else if(userMap.get(i) instanceof StanderdUser) {
                outMap.put(i,i + ": " + userMap.get(i).getUserName());
            }
        }
        int selectedUser = out.promptForMenu(outMap);
        return userMap.get(selectedUser);
    }

    private Provider getProviderFromMap() throws IOException {
        out.display("Select A Provider...");
        Map<Integer,Provider> providerMap = controller.getProviderList().getProviderMap();
        Map<Integer, String> outMap = new HashMap<>();
        for(int i=0; i<providerMap.size(); i++){
            outMap.put(i,i +": " + providerMap.get(i));
        }
        int selectedProvider = out.promptForMenu(outMap);
        return providerMap.get(selectedProvider);
    }

    private Patient getPatientFromMap() throws IOException {
        out.display("Select A Patient...");
        Map<Integer,Patient> patientMap = controller.getPatientList().getPatientMap();
        Map<Integer, String> outMap = new HashMap<>();
        for(int i=0; i<patientMap.size(); i++){
            outMap.put(i,i +": " + patientMap.get(i));
        }
        int selectedProvider = out.promptForMenu(outMap);
        return patientMap.get(selectedProvider);
    }

    private Procedure getProcedureFromMap() throws IOException {
        out.display("Select A Procedure...");
        Map<Integer,Procedure> procedureMap = controller.getProcedureList().getProcedurMap();
        Map<Integer, String> outMap = new HashMap<>();
        for(int i=0; i<procedureMap.size(); i++){
            outMap.put(i,i +": " + procedureMap.get(i));
        }
        int selectedProvider = out.promptForMenu(outMap);
        return procedureMap.get(selectedProvider);
    }

    private Appointment getAppointmentFromMap() throws IOException {
        out.display("Select A Procedure...");
        Map<Integer,Appointment> appointmentMap = controller.getAppointmentList().getAppointmentMap();
        Map<Integer, String> outMap = new HashMap<>();
        for(int i=0; i<appointmentMap.size(); i++){
            outMap.put(i,(i +": " + appointmentMap.get(i).toString()));
        }
        int selectedAppointment = out.promptForMenu(outMap);
        return appointmentMap.get(selectedAppointment);
    }


}
