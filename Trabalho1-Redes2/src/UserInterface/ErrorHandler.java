package UserInterface;

import ApplicationModel.Block;
import java.util.ArrayList;

/**
 * Class responsable for handling errors that can happen in the execution of the
 * software
 *
 * @author Henrique Linhares ; Raphael Quintanilha, Pablo Curty, Felipe Coimbra
 */
public class ErrorHandler {

    /**
     * Call this method when you try to read a coded block that is smaller than
     * 3 bytes. All of the coded blocks must have at least 3 bytes : 2 for
     * parity bits and at least 1 for the block itself
     *
     * This method will display an error message and finish the execution
     *
     */
    public static void readingBlockSmallerThan3Bytes() {
        System.out.println("Fatal error : Trying to read a block smaller than 3 byes.");
        System.out.println("Aborting execution...");
        System.exit(0);
    }

    /**
     * Method to be used when you have a block with parity bits, and the parity
     * bits dont check correclty and the algorith cant fix the problem
     *
     * This method will display an error message and finish the execution
     */
    public static void couldNotFixParityBitsError() {
        System.out.println("Fatal error : Could not fix a parity error");
        System.out.println("Aborting execution...");
        System.exit(0);
    }

    /**
     * Use this method to display on the console informations about an error
     * while checking the parity of a block
     *
     * @param blockID the ID of the block with the error
     * @param block the block with the error
     */
    public static void displayErrorInBlockInfo(int blockID, Block block) {
        System.out.println("Error in block parity!");
        System.out.println("Block # " + blockID);
        printBlockErrors(block);
        System.out.println("Trying to fix error...");
    }

    /**
     * Private method. Print more details about the errors in the block, like
     * wich line(s) and column(s) have error(s)
     *
     * @param block the block with error(s) that will be printed
     */
    private static void printBlockErrors(Block block) {
        ArrayList lineErrors = block.searchErrorsInLines();
        ArrayList columnErrors = block.searchErrorsInColumns();

        if (!lineErrors.isEmpty()) {
            System.out.println("Errors in line(s): " + lineErrors);
        }

        if (!columnErrors.isEmpty()) {
            System.out.println("Errors in column(s)" + columnErrors);
        }

    }

}
