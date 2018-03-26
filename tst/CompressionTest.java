import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.PriorityQueue;

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

    private static String original = "abcdefghijklmnopqrstuvwxyzæøå .,-!? aaabbc";

    @Test
    public void Huffman_PriorityQueue_Output() {

        String freq = Compression.CompressHuffman(original).split("" + '\u001C')[0];

        PriorityQueue<Node<String>> one = Huffman.getFrequencyQueue(original, new StringBuilder());
        PriorityQueue<Node<String>> two = Huffman.extractFrequencyQueue(freq);

        while (!one.isEmpty() || !two.isEmpty())
            assertEquals("Unequal pq!", one.poll().toString(), two.poll().toString());

    }

    @Test
    public void HuffmanCompress_decompress_test() {
        String compressed = Compression.CompressHuffman(original);
        String decompressed = Compression.deCompressHuffman(compressed);
        assertEquals("De-compression loss!", original, decompressed);
    }

    @Test
    public void HuffmanCompresses() {
        assertTrue(getCompressedBitSize(Compression.CompressHuffman(original)) < getTHeoreticalBitSize(original));
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