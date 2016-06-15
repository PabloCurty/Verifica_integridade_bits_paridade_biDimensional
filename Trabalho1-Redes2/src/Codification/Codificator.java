package Codification;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import ApplicationModel.Block;
import ApplicationModel.OutputFileWriter;
import UserInterface.ConsoleInterface;

import java.util.ArrayList;

/**
 * Class responsable for the codification process
 *
 * @author Henrique Linhares ; Raphael Quintanilha, Pablo Curty, Felipe Coimbra
 */
public class Codificator {

    private File file;
    private FileInputStream fileInputStream;
    
    private int numberOfReads;

    /**
     * The master method of this class. Is called to encode a file and create an
     * output file with the content of the encoded file.
     *
     * @param inputFilePath The path to the (existing) file that are going to be
     * encoded
     * @param outputFilePath The path to the (non-existing) file that are going
     * to keep the encoded content of the input File
     * @throws IOException Can throw IO Exception
     */
    public void encodeFile(String inputFilePath, String outputFilePath) throws IOException {
        
        ConsoleInterface.displayMessage("Reading file...");
        
        ArrayList<Block> blocks = this.readInputFile(inputFilePath);
        
        ConsoleInterface.displayMessage("Calculating Parity Bits...");
        
        for (Block block : blocks) {
            block.makeParityBits();
        }
        
        
        
        ConsoleInterface.displayMessage("Writing output file...");
        
        OutputFileWriter out = new OutputFileWriter();
        out.numberOfReads = this.numberOfReads;
        out.writeOutputFile(outputFilePath, blocks, Boolean.TRUE);
        
        ConsoleInterface.displayMessage("Done");
    }

    /**
     * Method responsable for reading all the data of the input file, and place
     * it into Blocks.
     *
     * @param inputFilePath The path to the file that will be read.
     * @return An ArrayList of Blocks, that contains the data of the input file
     * @throws IOException Can Throw IO Exception
     */
    private ArrayList<Block> readInputFile(String inputFilePath) throws IOException {

        file = new File(inputFilePath);
        fileInputStream = new FileInputStream(file);
        System.out.println("Total file size to encode (in bytes) : " + fileInputStream.available());

        int content;
        int[] bytesArray;
        bytesArray = new int[8];
        numberOfReads = 0;
        ArrayList blocks;
        blocks = new ArrayList<Block>();

        while ((content = fileInputStream.read()) != -1) {
            bytesArray[numberOfReads] = content;
            numberOfReads = numberOfReads + 1;
            if (numberOfReads == 8) {
                Block block = new Block();
                block.newBlockWithoutParityBits(bytesArray);
                blocks.add(block);
                bytesArray = new int[8];
                numberOfReads = 0;
            }
        }

        if (numberOfReads > 0) {
        	// Complete the bytesArray with zeros when the final byte is imcomplete
        	for (int i = numberOfReads; i <= 7; i++) {
				bytesArray[i] = 0;
			}
            Block block = new Block();
            block.newBlockWithoutParityBits(bytesArray);
            blocks.add(block);
        }

        fileInputStream.close();
        return blocks;
    }

	public int getNumberOfReads() {
		return numberOfReads;
	}

	public void setNumberOfReads(int numberOfReads) {
		this.numberOfReads = numberOfReads;
	}
    
    

}
