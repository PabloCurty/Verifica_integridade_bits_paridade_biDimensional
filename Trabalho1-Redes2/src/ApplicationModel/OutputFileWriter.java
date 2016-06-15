package ApplicationModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import UserInterface.ConsoleInterface;

/**
 * Class responsable for the writings in files
 *
 * @author Henrique Linhares ; Raphael Quintanilha, Pablo Curty, Felipe Coimbra
 */
public class OutputFileWriter {

    private File file;
    private FileOutputStream fileOutputStream;
    
    public int numberOfReads;

    /**
     * Write blocks in an output file
     *
     * @param outputFilePath The path to the (non-existent) file that is going
     * to be written
     * @param blocks The Blocks that are going to be saved in the file
     * @param writeParityBits Write also the parity bits to the output file ?
     * @throws IOException Can throw IO Exception
     */
    public void writeOutputFile(String outputFilePath, ArrayList<Block> blocks, Boolean writeParityBits) throws IOException {

        file = new File(outputFilePath);
        if (!file.exists()) {
            file.createNewFile();
        }else{
        	ConsoleInterface.displayMessage("File already exists");
        	return;
        }
        fileOutputStream = new FileOutputStream(file); 

        for (Block block : blocks) {
            if (writeParityBits) {
                fileOutputStream.write(block.getByteRepresentationOfColumnParityBits()); //Write the columns parity bits
                fileOutputStream.write(block.getByteRepresentationOfLineParityBits());   //write the lines parity bits
            }
            if(!(blocks.get(blocks.size() - 1) == block)){
	            for (int k = 0; k <= 7; k++) {
	                fileOutputStream.write(block.getByteRepresentationOfLine(k)); // write the block
	            }
            }else{
            	for (int i = 0 ; i < numberOfReads ; i++) {
	                fileOutputStream.write(block.getByteRepresentationOfLine(i)); // write the block
	            }
            }
        }
        fileOutputStream.close();
    }

}
