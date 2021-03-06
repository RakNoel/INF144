import compression.Huffman;
import compression.LZW;
import compression.LZWDictionary;
import compression.Node;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import static compression.getBitSize.getCompressedBitSize;
import static compression.getBitSize.getTheoreticalBitSize;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * TEst-Class to see if the compression stuctures work as
 * expected and that they actually compress.
 *
 * @author RakNoel
 * @version 1.0
 * @since 25.03.18
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CompressionTest {
    private static URL url_short = CompressionTest.class.getResource("Folktale.txt");
    private static URL url_long = CompressionTest.class.getResource("mobydick.txt");
    private static String short_story = null;   //Askeladden
    private static String long_story = null;    //Moby dick

    @BeforeClass
    public static void onlyonce() throws FileNotFoundException {
        short_story = new BufferedReader(new FileReader(new File(url_short.getPath()))).lines()
                .filter(x -> (x.length() > 1))
                .collect(Collectors.joining(" "));
        long_story = new BufferedReader(new FileReader(new File(url_long.getPath()))).lines()
                .filter(x -> (x.length() > 0))
                .collect(Collectors.joining(" "));
    }

    @Test
    public void Huffman_PriorityQueue_Output() {

        String freq = Huffman.compressText(short_story).split("" + '\u001C')[0];

        PriorityQueue<Node<String>> one = Huffman.getFrequencyQueue(short_story, new StringBuilder());
        PriorityQueue<Node<String>> two = Huffman.extractFrequencyQueue(freq);

        while (!one.isEmpty() || !two.isEmpty())
            assertEquals("Unequal pq!", one.poll().toString(), two.poll().toString());

    }

    @Test
    public void HuffmanCompress_decompress_test() {
        String compressed = Huffman.compressText(short_story);
        String decompressed = Huffman.deCompressText(compressed);
        assertEquals("De-compression loss!", short_story, decompressed);
    }

    @Test
    public void HuffmanCompresses() {
        assertTrue("LZW does not actually compresss",
                getCompressedBitSize(Huffman.compressText(long_story)) < getTheoreticalBitSize(long_story)
        );
    }

    @Test
    public void LZW_Compresses() {
        System.out.println(LZW.compressText(short_story));

        assertTrue("LZW does not actually compresss",
                LZW.compressText(short_story).length() < getTheoreticalBitSize(short_story)
        );
    }

    @Test
    public void LZW_Compress_decompress() {
        String compressed = LZW.compressText("thisisthe"); //should be equal to: "0000010100011011101010100"
        String decompressed = LZW.deCompressText(compressed); //Should be equal to: "thisisthe"

        assertEquals("LZW decompresses new result!", decompressed, "thisisthe");

        compressed = LZW.compressText(short_story); //should be equal to: "0000010100011011101010100"
        decompressed = LZW.deCompressText(compressed); //Should be equal to: "thisisthe"

        assertEquals("LZW decompresses new result!", decompressed, short_story);
    }

    @Test
    public void LZW_Dictionary_equals() {
        String[] devidedString = short_story.split("" + '\u001C');
        String dictionaryPart = devidedString[0];

        LZWDictionary dictionaryFromCompressed = LZW.createDictionary(dictionaryPart);
        LZWDictionary dictionaryFromOriginal = LZW.createDictionary(short_story);

        assertEquals("Dictionaries unequal", dictionaryFromCompressed, dictionaryFromOriginal);
    }
}