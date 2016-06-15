package Decodification;

import ApplicationModel.OutputFileWriter;
import ApplicationModel.Block;
import UserInterface.ConsoleInterface;
import UserInterface.ErrorHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class responsable for the decodification process
 *
 * @author Henrique Linhares ; Raphael Quintanilha, Pablo Curty, Felipe Coimbra
 */
public class Decodificator {

    private File file;
    private FileInputStream fileInputStream;

    /**
     * The master method of this class. Use this method to read a coded file,
     * check the parity bits, try to fix errors and write a decoded file.
     *
     * @param inputFile The Path to the (existent) coded file that is going to
     * be read
     * @param outputFile The Path to the (non-existent) file that is going to be
     * written
     * @throws IOException Can throw IO Exception
     */
    public void decodeFile(String inputFile, String outputFile) throws IOException {
        
        ConsoleInterface.displayMessage("Reading file...");
        
        ArrayList<Block> blocks = this.readCodedFile(inputFile);

        ConsoleInterface.displayMessage("Checking the parity bits...");
        
        int count = 0;
        for (Block block : blocks) {
            count = count + 1;
            if (!block.parityBitsAreCorrect()) {
                ErrorHandler.displayErrorInBlockInfo(count, block);
                if (block.tryToFixParityBitsErrors()) {
                    ConsoleInterface.displayErrorsFixedMessage();
                } else {
                    ErrorHandler.couldNotFixParityBitsError();
                }
            }
        }
        

        ConsoleInterface.displayMessage("Writing output file...");
        
        OutputFileWriter out = new OutputFileWriter();
        out.writeOutputFile(outputFile, blocks, false);
        
        ConsoleInterface.displayMessage("Done");
    }

    /**
     * Method responsable for reading the coded file and loading all the blocks
     * to the memory.
     *
     * @param codedFilePath The path to the existent coded file that is going to
     * be read.
     * @return An ArrayList with all the blocks that have been read
     * @throws IOException Can throw IO Exception
     */
    private ArrayList<Block> readCodedFile(String codedFilePath) throws IOException {
        file = new File(codedFilePath);
        fileInputStream = new FileInputStream(file);

        int content;
        int[] bytesArray;
        bytesArray = new int[10];
        int numberOfReads = 0;

        ArrayList blocks;
        blocks = new ArrayList<Block>();

        while ((content = fileInputStream.read()) != -1) {
            bytesArray[numberOfReads] = content;
            numberOfReads = numberOfReads + 1;
            if (numberOfReads == 10) {
                Block block = new Block();
                block.newBlockWithParityBits(bytesArray);
                blocks.add(block);
                bytesArray = new int[10];
                numberOfReads = 0;
            }
        }

        if (numberOfReads > 0) {
            if (numberOfReads < 3) {
                ErrorHandler.readingBlockSmallerThan3Bytes();
            }
            Block block = new Block();
            block.newBlockWithParityBits(bytesArray);
            blocks.add(block);
        }

        fileInputStream.close();
        return blocks;

    }

}
