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
        assertTrue(getCompressedBitSize(CompressHuffman(long_story)) < getTHeoreticalBitSize(long_story));
    }

    private int getCompressedBitSize(String encodedString) {
        return encodedString.split("" + '\u001C')[1].length();
    }

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