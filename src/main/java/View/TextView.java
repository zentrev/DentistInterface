package View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class TextView {

    private PrintStream out;

    private BufferedReader in;


    public TextView(){
        out = System.out;
        in = new BufferedReader(new InputStreamReader(System.in));
    }


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

    public String promptForString(String prompt) throws IOException {
        out.println(prompt);
        String rawString = in.readLine();
        return rawString;
    }

    public double promtForDouble(double prompt) throws IOException{
        out.println(prompt);
        String rawString = in.readLine();
        double rawdouble = Double.parseDouble(rawString);
        return rawdouble;
    }

    public int promptForInt(int prompt) throws IOException{
        out.println(prompt);
        String rawString = in.readLine();
        int rawInt = Integer.parseInt(rawString);
        return rawInt;
    }

    public void display(String string) {
        out.println(string);

    }
}