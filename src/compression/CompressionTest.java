package compression;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import static compression.Huffman.CompressHuffman;
import static compression.Huffman.deCompressHuffman;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * TODO: Describe test
 *
 * @author RakNoel
 * @version 1.0
 * @since 25.03.18
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CompressionTest {

    private static String short_story = null;   //Askeladden
    private static String long_story = null;    //Moby dick

    @BeforeClass
    public static void onlyonce() throws FileNotFoundException {
        short_story = new BufferedReader(new FileReader(new File("res/askeladden.txt"))).lines()
                .collect(Collectors.joining(" "));
        long_story = new BufferedReader(new FileReader(new File("res/mobydick.txt"))).lines()
                .collect(Collectors.joining(" "));
    }

    @Test
    public void Huffman_PriorityQueue_Output() {

        String freq = CompressHuffman(short_story).split("" + '\u001C')[0];

        PriorityQueue<Node<String>> one = Huffman.getFrequencyQueue(short_story, new StringBuilder());
        PriorityQueue<Node<String>> two = Huffman.extractFrequencyQueue(freq);

        while (!one.isEmpty() || !two.isEmpty())
            assertEquals("Unequal pq!", one.poll().toString(), two.poll().toString());

    }

    @Test
    public void HuffmanCompress_decompress_test() {
        String compressed = CompressHuffman(short_story);
        String decompressed = deCompressHuffman(compressed);
        assertEquals("De-compression loss!", short_story, decompressed);
    }

    @Test
    public void HuffmanCompresses() {
        assertTrue("LZW does not actually compresss",
                getCompressedBitSize(CompressHuffman(long_story)) < getTHeoreticalBitSize(long_story)
        );
    }

    @Test
    public void LZW_Compresses() {
        System.out.println(LZW.compressText(short_story));

        assertTrue("LZW does not actually compresss",
                getCompressedBitSize(LZW.compressText(short_story)) < getTHeoreticalBitSize(short_story)
        );

        System.out.println(getCompressedBitSize(LZW.compressText(short_story))); //28924
    }

    @Test
    public void LZW_small_Wont_compress() {
        String s = "thisisthe";
        System.out.println(LZW.compressText(s));

        assertFalse("LZW does not actually compresss",
                getCompressedBitSize(LZW.compressText(s)) < getTHeoreticalBitSize(s)
        );

        System.out.println(getCompressedBitSize(LZW.compressText(s)));
    }


    /**
     * Calculates the theoretical size of this string is stored propperly as binary
     * in fileformat UTF-8
     *
     * @param encodedString A compressed string with a FS splitter between dictionary and encoded
     * @return returns the theoretical size of the compressed string
     */
    private int getCompressedBitSize(String encodedString) {
        String[] splitted = encodedString.split("" + '\u001C');
        return getTHeoreticalBitSize(splitted[0]) + splitted[1].length();
    }

    /**
     * Claculated the theoretical size of a string if stored in UTF-8
     * @param originalString the string to measure
     * @return calculated theoretical file-size of string.
     */
    private int getTHeoreticalBitSize(String originalString) {
        int bits = 0;
        for (char ch : originalString.toCharArray()) {
            if ((int) ch <= 127)
                bits += 8;
            else
                bits += 16;
        }

        return bits;
    }
}