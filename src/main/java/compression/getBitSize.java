package compression;

/**
 * TODO: Describe class
 *
 * @author RakNoel
 * @version 1.0
 * @since 28.03.18
 */
public class getBitSize {
    /**
     * Calculates the theoretical size of this string is stored propperly as binary
     * in fileformat UTF-8
     *
     * @param encodedString A compressed string with a FS splitter between dictionary and encoded
     * @return returns the theoretical size of the compressed string
     */
    public static int getCompressedBitSize(String encodedString) {

        if (encodedString.contains("" + '\u001C')) {
            String[] splitted = encodedString.split("" + '\u001C');
            return getTheoreticalBitSize(splitted[0]) + splitted[1].length();
        }
        return encodedString.length();
    }

    /**
     * Claculated the theoretical size of a string if stored in UTF-8
     *
     * @param originalString the string to measure
     * @return calculated theoretical file-size of string.
     */
    public static int getTheoreticalBitSize(String originalString) {
        int bits = 0;
        for (char ch : originalString.toCharArray()) {
            if ((int) ch <= 127)
                bits += 8;
            else
                bits += 16;
        }

        return bits;
    }

    public static String getCompressionRate(String original, String compressed) {
        double comp = getCompressedBitSize(compressed);
        double normal = getTheoreticalBitSize(original);

        StringBuilder bldr = new StringBuilder();

        bldr.append("Compressed size: ");
        bldr.append(comp);
        bldr.append(System.lineSeparator());

        bldr.append("Original size: ");
        bldr.append(normal);
        bldr.append(System.lineSeparator());

        double rate = 100 * (comp / normal);

        bldr.append("Result: ");
        bldr.append(rate);
        bldr.append("%");
        bldr.append(System.lineSeparator());

        return bldr.toString();
    }
}
