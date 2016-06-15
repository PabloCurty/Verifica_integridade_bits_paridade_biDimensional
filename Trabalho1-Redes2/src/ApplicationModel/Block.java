package ApplicationModel;

import java.util.ArrayList;

/**
 * This class represents a block. Use this class to make the parity bits, or to
 * check if the calculated parity bits are correct.
 *
 * @author Henrique Linhares ; Raphael Quintanilha, Pablo Curty, Felipe Coimbra
 */
public class Block {

    private int[][] matrix;
    private int[] lineParityBits;
    private int[] columnParityBits;
    private boolean doesHaveParityBits;

    /**
     * Constructor Initialize all the class properties
     */
    public Block() {
        this.matrix = new int[8][8];
        this.lineParityBits = new int[8];
        this.columnParityBits = new int[8];
        this.doesHaveParityBits = false;
    }

    /**
     * Create a new block WITHOUT the parity bits Use this method when reading
     * the bytes from a file that is NOT coded
     *
     * @param bytes An array with 8 bytes (the size of a block)
     */
    public void newBlockWithoutParityBits(int[] bytes) {
        this.doesHaveParityBits = false;
        int line;
        int column;
        for (line = 0; line <= 7; line++) {
            int value = bytes[line];
            String bits = ByteBitConverter.byteToBits(value);
            for (column = 0; column <= 7; column++) {
                this.matrix[line][column] = Character.getNumericValue(bits.charAt(column));
            }
        }
    }

    /**
     * Create a new block WITH the parity bits Use this method when reading the
     * bytes from a file that was coded by this software
     *
     * @param bytes An Array with 10 bytes : The first byte is the COLUMNs
     * parity bits and the second byte is the LINEs parity bits. The bytes from
     * 2 to 10 are the block.
     */
    public void newBlockWithParityBits(int[] bytes) {

        this.doesHaveParityBits = true;

        //Reading the first byte : the COLUMNs parity bits
        int value = bytes[0];
        String bits = ByteBitConverter.byteToBits(value);
        for (int k = 0; k < 8; k++) {
            this.columnParityBits[k] = Character.getNumericValue(bits.charAt(k));
        }

        //Reading the second byte : the LINEs parity bits
        value = bytes[1];
        bits = ByteBitConverter.byteToBits(value);
        for (int k = 0; k < 8; k++) {
            this.lineParityBits[k] = Character.getNumericValue(bits.charAt(k));
        }

        //Reading the block 
        int line;
        int column;
        for (line = 2; line <= 9; line++) {
            value = bytes[line];
            bits = ByteBitConverter.byteToBits(value);
            for (column = 0; column <= 7; column++) {
                this.matrix[line - 2][column] = Character.getNumericValue(bits.charAt(column));
            }
        }
    }

    /**
     * Method that creates an ((int) byte) representation of a 8-bits line in
     * this block.
     *
     * @param line The line of the block that is going to be converted to byte
     * @return a byte (8bits) represented in an int
     */
    public int getByteRepresentationOfLine(int line) {
        return ByteBitConverter.bitsToByte(this.matrix[line]);
    }

    /**
     * Method that creates an ((int) byte) representation from the parity bits
     * of the LINES of the block
     *
     * @return a byte (8bits) of the parity bits of the LINES represented in an
     * int
     */
    public int getByteRepresentationOfLineParityBits() {
        return ByteBitConverter.bitsToByte(this.lineParityBits);
    }

    /**
     * Method that creates an ((int) byte) representation from the parity bits
     * of the COLUMNS of the block
     *
     * @return a byte (8bits) of the parity bits of the COLUMNS represented in
     * an int
     */
    public int getByteRepresentationOfColumnParityBits() {
        return ByteBitConverter.bitsToByte(this.columnParityBits);
    }

    /**
     * Method used to calculate the parity bits. After calling this method, you
     * will fill the lineParityBits and columnParityBits properties from this
     * class.
     *
     * Attention : If your block already have parity bits calculated, this
     * method wont do anything. If you are looking for search if the parity bits
     * are correct, use the parityBitsAreCorrect() from this class.
     *
     */
    public void makeParityBits() {

        if (doesHaveParityBits == true) {
            return;
        } else {
            doesHaveParityBits = true;
        }

        int sum = 0;
        int line;
        int column;

        /*
         Calculates the parity bit line by line
         */
        for (line = 0; line <= 7; line++) {
            for (column = 0; column <= 7; column++) {
                sum = sum + this.matrix[line][column];
            }
            if (sum % 2 == 0) {
            	// Paridade par
                this.lineParityBits[line] = 0;
            } else {
            	//Paridade impar
                this.lineParityBits[line] = 1;
            }
            sum = 0;
        }

        /*
         Calculates the parity bit column by column
         */
        for (column = 0; column <= 7; column++) {
            for (line = 0; line <= 7; line++) {
                sum = sum + this.matrix[line][column];
            }
            if (sum % 2 == 0) {
                this.columnParityBits[column] = 0;
            } else {
                this.columnParityBits[column] = 1;
            }
            sum = 0;
        }

    }

    /**
     * A Method that checks if the parity bits are correct. When returns true,
     * everything seems to be allright, i.e., the parity algorithm was not able
     * to find any problem in this block. Returns false when an error is found
     * OR when the block dont have calculated parity bits If you found errors
     * with this check, you might want to use the tryToFixParityBitsErrors() to
     * try to fix these errors.
     *
     * @return True == No errors ; False == Found Errors OR dont have calculated
     * parity bits
     */
    public boolean parityBitsAreCorrect() {
        if (this.doesHaveParityBits) {
            ArrayList lineErrors = this.searchErrorsInLines();
            ArrayList columnErrors = this.searchErrorsInColumns();
            if (lineErrors.isEmpty() && columnErrors.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Used the parity algorithm to try to repair the errors that were found in
     * this block.
     *
     * @return True == All errors were fixed ; False == Could not fix the
     * errors.
     */
    public boolean tryToFixParityBitsErrors() {
        int sum = 0;
        int line;
        int column;

        ArrayList<Integer> lineErrors = this.searchErrorsInLines();
        ArrayList<Integer> columnErrors = this.searchErrorsInColumns();

        if (lineErrors.size() == 1 && columnErrors.size() == 1) {
            line = lineErrors.get(0);
            column = columnErrors.get(0);
            if (this.matrix[line][column] == 0) {
                this.matrix[line][column] = 1;
            } else {
                this.matrix[line][column] = 0;
            }
            return parityBitsAreCorrect();
        }
        return false;
    }

    /**
     * Check the parity bits of all LINES, searching for errors.
     *
     * @return An arraylist with the number of the lines that the errors were
     * found. Remember that first line is 0 If it returns an empty ArrayList, it
     * means that it couldnt find any errors in LINES.
     */
    public ArrayList<Integer> searchErrorsInLines() {
        int sum = 0;
        int line;
        int column;

        ArrayList<Integer> lineErrors = new ArrayList<Integer>();

        for (line = 0; line <= 7; line++) {
            for (column = 0; column <= 7; column++) {
                sum = sum + matrix[line][column];
            }
            if (sum % 2 != this.lineParityBits[line]) { //Found error in line
                lineErrors.add(line);
            }
            sum = 0;
        }
        return lineErrors;
    }

    /**
     * Check the parity bits of all COLUMNs, searching for errors.
     *
     * @return An arraylist with the number of the columns that the errors were
     * found. Remember that first column is 0 If it returns an empty ArrayList,
     * it means that it couldnt find any errors in COLUMNs.
     */
    public ArrayList<Integer> searchErrorsInColumns() {
        int sum = 0;
        int line;
        int column;

        ArrayList<Integer> columnErrors = new ArrayList<Integer>();

        for (column = 0; column <= 7; column++) {
            for (line = 0; line <= 7; line++) {
                sum = sum + matrix[line][column];
            }
            if (sum % 2 != this.columnParityBits[column]) { // Found error in column
                columnErrors.add(column);
            }
            sum = 0;
        }
        return columnErrors;
    }

}
