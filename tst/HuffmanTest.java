import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;

/**
 * TODO: Describe test
 *
 * @author RakNoel
 * @version 1.0
 * @since 25.03.18
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HuffmanTest {

    private static String original = "abcdefghijklmnopqrstuvwxyzæøå .,-!? aaabbc";

    @Test
    public void PriorityQueue_Output() {

        String freq = Compression.CompressHuffman(original).split("" + '\u001C')[0];

        PriorityQueue<Node<String>> one = Huffman.getFrequencyQueue(original, new StringBuilder());
        PriorityQueue<Node<String>> two = Huffman.extractFrequencyQueue(freq);

        while (!one.isEmpty() || !two.isEmpty())
            assertEquals("Unequal pq!", one.poll().toString(), two.poll().toString());

    }

    @Test
    public void Compress_decompress_test() {

        String compressed = Compression.CompressHuffman(original);
        System.out.println("Compressed: " + compressed);

        String decompressed = Compression.deCompressHuffman(compressed);
        System.out.println("deCompressed: " + decompressed);

        assertEquals("De-compression loss!", original, decompressed);
    }
}