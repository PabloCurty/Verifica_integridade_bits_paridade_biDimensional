package ApplicationModel;

/**
 * Class responsable for calculating the convertions between bytes and bits
 *
 * @author Henrique Linhares ; Raphael Quintanilha, Pablo Curty, Felipe Coimbra
 */
public class ByteBitConverter {

    /**
     * Convert a byte into a string of bits
     *
     * eg: param : 37 return : 00100101
     *
     * @param pByte the number that is going to be converted into bits
     * @return A string with the bits of the param number
     */
    public static String byteToBits(int pByte) {
        return String.format("%8s", Integer.toBinaryString(pByte & 0xFF)).replace(' ', '0');
    }

    /**
     * Convert an array of bits into a byte
     *
     * @param bits an arrat of bits
     * @return a byte represented in an int
     */
    public static int bitsToByte(int[] bits) {
        int expoent = bits.length - 1;
        int sum = 0;
        for (int k = 0; k <= bits.length - 1; k++) {
            if (bits[k] == 1) {
                sum = (int) (sum + Math.pow(2, expoent));
            }
            expoent = expoent - 1;
        }
        return sum;
    }

}
