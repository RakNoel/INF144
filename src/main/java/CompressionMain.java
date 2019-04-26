import compression.Huffman;
import compression.LZW;
import markov.Markov;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static compression.getBitSize.*;

/**
 * CompressionMain class to be run, generating all results
 * for the assignment.
 *
 * @author RakNoel
 * @version 1.0
 * @since 27.03.18
 */
public class CompressionMain {

    public static void main(String[] args) throws Exception {

        URL url_short = CompressionMain.class.getResource("Folktale.txt");
        URL url_long = CompressionMain.class.getResource("mobydick.txt");

        final int generations = 5;
        final int iterations = 300; //samples to be run

        /*
         * First we generate the 30 strings from the markov chain. And inspect them
         */
        String text = new BufferedReader(new FileReader(new File(url_short.getPath()))).lines()
                .map(x -> x.toLowerCase()
                        .replace(".", "")
                        .replace(",", "")
                        .replace("/n", " ")
                        .replace("(", " ")
                        .replace(")", " ")
                        .replace(";", "")
                        .replace(":", "")
                )
                .collect(Collectors.joining(" "));

        /*
         * Generate short stories (length 30), swap boolean to stop random ending last words.
         */

        /*
        for (int i = 0; i <= generations; i++) {

            int finalI = i;
            new Thread(() -> System.out.printf("%d degree :: %s %n", finalI, Markov.generateRandomStoryFromText(text, finalI, 100, false))).start();
        }
         */


        /*
         * Now to the compression part, lets first compress the main short-story
         */
        System.out.println("First off a normal LZW compression");
        System.out.println(getCompressionRate(text, LZW.compressText(text)));

        /*
         * Now lets do the huffman and see how stupidly much larger it gets. As this is NOT correctly in saved
         * in binary the result is even worse
         */
        System.out.println("Now a huffman compression of an already compressed text");
        System.out.println(getCompressionRate(text, Huffman.compressText(LZW.compressText(text).replace('\u001C', ' '))));

        /*
         * Now lets do this a lot more times times more to prove how stupid it actually is.
         */

        var percent = 0;
        System.out.println("Beginning large compression test:");
        System.out.println("00 percent");

        StringBuilder result = new StringBuilder();
        for (int generation = 0; generation < 5; generation++) {
            result.append(System.lineSeparator());
            result.append(String.format("GENERATION %d: %n", generation));

            ArrayList<String> texter = new ArrayList<>();
            for (int i = 0; i < iterations; i++)
                texter.add(Markov.generateRandomStoryFromText(text, generation, 10000, false));

            //Print results
            double sum = 0.0;
            for (String s : texter) {
                double comp = getCompressedBitSize(LZW.compressText(s));
                double normal = getTheoreticalBitSize(s);
                sum += 100 * (comp / normal);
            }
            result.append(String.format("LZW avg: %f %n", sum / iterations));

            sum = 0.0;
            for (String s : texter) {
                double comp = getCompressedBitSize(Huffman.compressText(s));
                double normal = getTheoreticalBitSize(s);
                sum += 100 * (comp / normal);
            }
            result.append(String.format("Huffman avg: %f %n", sum / iterations));

            sum = 0.0;
            for (String s : texter) {
                double comp = getCompressedBitSize(Huffman.compressText(LZW.compressText(s).replace('\u001C', ' ')));
                double normal = getTheoreticalBitSize(s);
                sum += 100 * (comp / normal);
            }
            result.append(String.format("LZW + Huffman avg: %f %n", sum / iterations));

            percent += 20;
            System.out.printf("%d percent %n", percent);
        }
        System.out.println(result.toString());
    }
}
