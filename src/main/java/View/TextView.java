package View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Map;
import java.util.Set;

/**
 *Class that makes the View for user
 */
public class TextView {

    private PrintStream out;

    private BufferedReader in;


    /**
     * default constructor that starts bufferedreader.
     */
    public TextView(){
        out = System.out;
        in = new BufferedReader(new InputStreamReader(System.in));
    }


    /**
     * Crates the menu
     * @param option returns how they want it made
     * @return the build menu
     * @throws IOException
     */
    public int promptForMenu(Map<Integer, String> option) throws IOException  {

        for(Integer key : option.keySet()) {
            out.println(key + " - " + option.get(key));
        }
        int selection = readIntFromUser(option.keySet());
        return selection;
    }

    private int readIntFromUser(Set<Integer> set) throws IOException {

        while(true) {
            String rawString = in.readLine();
            try {
                int value = Integer.parseInt(rawString);
                if(set.contains(value)) {
                    return value;
                }
                return value;
            }
            catch(NumberFormatException ex) {}
            out.println("Sorry, you have to enter a valid selection. Try again");
        }
    }

    /**
     * Takes in a String
     * @param prompt
     * @return the string
     * @throws IOException
     */
    public String promptForString(String prompt) throws IOException {
        out.println(prompt);
        String rawString = in.readLine();
        return rawString;
    }

    /**
     * takes in a string then parse it to a double
     * @param prompt
     * @return double
     * @throws IOException
     */
    public double promtForDouble(String prompt) throws IOException{
        out.println(prompt);
        String rawString = in.readLine();
        double rawdouble = Double.parseDouble(rawString);
        return rawdouble;
    }

    /**
     * takes in a string then pares it to a int
     * @param prompt
     * @return int
     * @throws IOException
     */
    public int promptForInt(String prompt) throws IOException{
        out.println(prompt);
        String rawString = in.readLine();
        int rawInt = Integer.parseInt(rawString);
        return rawInt;
    }

    /**
     * takes in a string then pares it to a long
     * @param prompt
     * @return long
     * @throws IOException
     */
    public long promptForLong(String prompt) throws IOException{
        out.println(prompt);
        String rawString = in.readLine();
        long rawInt = Long.parseLong(rawString);
        return rawInt;
    }

    /**
     * creats the display
     * @param string
     */
    public void display(String string) {
        out.println(string);
    }


}
