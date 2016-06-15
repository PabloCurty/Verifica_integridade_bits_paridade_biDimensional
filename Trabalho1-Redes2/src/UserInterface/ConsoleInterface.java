package UserInterface;

import Codification.Codificator;
import Decodification.Decodificator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class responsable for all user interaction through the console
 *
 * @author Henrique Linhares ; Raphael Quintanilha ; Filipe Coimbra; Pablo Curty
 */
public class ConsoleInterface {

    private BufferedReader inputConsole;

    /**
     * Constructor
     */
    public ConsoleInterface() {
        this.inputConsole = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Launch the console interface. Gets all the information with the user,
     * such as if he wants to encode or decode, and the paths to the files
     *
     */
    public void launchConsoleInterface() {
        System.out.println("Hello! Welcome to the Encoder / Decoder");
        try {
            manageConsoleInterface();
        } catch (IOException ex) {
            Logger.getLogger(ConsoleInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**Display the following message
     * "Errors in the current block were sucessfuly corrected!"
     * 
     */
    public static void displayErrorsFixedMessage(){
        System.out.println("Errors in the current block were sucessfuly corrected!");
    }

    /** Write the param message in the console
     * 
     * @param S the message that will be displayed in console
     */
    public static void displayMessage(String S){
        System.out.println(S);
    }
    
    /**
     * Manage the console interface. Get all the info from the user, and then,
     * using these info, launch the Codificator or the Decodificator module of
     * the software.
     *
     * @throws IOException Can throw IO Exception
     */
    private void manageConsoleInterface() throws IOException {
        int codeOrDecode = chooseCodeOrDecode();
        String inputFilePath = chooseINFile();
        String outputFilePath = chooseOUTFile();
        if (codeOrDecode == 1) {
            Codificator codificator = new Codificator();
            codificator.encodeFile(inputFilePath, outputFilePath);
        } else {
            Decodificator decodificator = new Decodificator();
            decodificator.decodeFile(inputFilePath, outputFilePath);
        }
    }

    /**
     * Ask for the user if he wats to code or decode a file
     *
     * @return Encode -> returns 1 ; Decode -> returns 2
     * @throws IOException Can throw IO exception
     */
    private int chooseCodeOrDecode() throws IOException {
        int read;
        do {
            System.out.println("1 - Encode File");
            System.out.println("2 - Decode File");
            String readStr = inputConsole.readLine();
            read = Integer.parseInt(readStr);
        } while (read != 1 && read != 2);
        return read;
    }

    /**
     * Asks the user the path to the INPUT file, and check if the path is
     * correct and if the file exists
     *
     * @return the correct path to the input file
     * @throws IOException Can throw IO exception
     */
    private String chooseINFile() throws IOException {
        File file;
        Boolean fileExists = false;
        String readStr;
        do {
            System.out.println("Enter the path to the INPUT file");
            readStr = inputConsole.readLine();
            file = new File(readStr);
            if (file.exists() && !file.isDirectory()) {
                fileExists = true;
            }else if(!file.exists()){
            	ConsoleInterface.displayMessage("File don't exist");
            	fileExists = false;
            }
        } while (fileExists == false);
        return readStr;
    }

    /**
     * Asks the user the path to the OUTPUT file, and check if the path is
     * correct and if the file dont exists
     *
     * @return the correct path to the output file
     * @throws IOException Can throw IO exception
     */
    private String chooseOUTFile() throws IOException {
        File file;
        Boolean pathIsCorrect;
        String readStr;
        do {
            System.out.println("Enter the path to the OUTPUT file");
            readStr = inputConsole.readLine();
            file = new File(readStr);
            if(!file.exists()){
            	if (!file.isDirectory()) {
            		pathIsCorrect = true;
            	} else {
            		pathIsCorrect = false;
            	}
            }else{
            	pathIsCorrect = false;
            	ConsoleInterface.displayMessage("File already exists");
            }
        } while (!pathIsCorrect);
        return readStr;
    }

}
