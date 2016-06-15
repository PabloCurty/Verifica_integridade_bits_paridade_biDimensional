package trabalho1.redes2;

import UserInterface.ConsoleInterface;

/**
 * Launcher Class. Responsable for starting the execution of the software.
 *
 * @author Henrique Linhares ; Raphael Quintanilha ; Filipe Coimbra; Pablo Curty
 */
public class Launcher {

    /**
     * Starts the console interface
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ConsoleInterface consoleInterface = new ConsoleInterface();
        consoleInterface.launchConsoleInterface();
    }
}

