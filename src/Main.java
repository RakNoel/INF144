import compression.Huffman;
import compression.LZW;
import markov.Markov;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static compression.getBitSize.*;

/**
 * Main class to be run, generating all results
 * for the assignment.
 *
 * @author RakNoel
 * @version 1.0
 * @since 27.03.18
 */
public class Main {

    public static void main(String[] args) throws Exception {

        /*
         * First we generate the 30 strings from the markov chain. And inspect them
         */
        String text = new BufferedReader(new FileReader(new File("res/askeladden.txt"))).lines()
                .map(x -> x.toLowerCase().replace(".", "").replace(",", ""))
                .collect(Collectors.joining(" "));

        String[] holder = null;
        for (int i = 0; i <= 3; i++) {
            holder = Markov.generateFromText(text, holder);
            System.out.print(i + " degree :: ");
            System.out.println(Arrays.toString(holder));
        }


        /*
         * Now to the compression part, lets first compress the main short-story
         */
        System.out.println(getCompressionRate(text, LZW.compressText(text)));

        /*
         * Now lets do the huffman and see how stupidly much larger it gets. As this is NOT correctly in saved
         * in binary the result is even worse
         */
        System.out.println(getCompressionRate(text, Huffman.compressText(LZW.compressText(text).replace('\u001C', ' '))));

        /*
         * Now lets do this 100 times more to prove how stupid it actually is.
         */
        ArrayList<String> texter = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            StringBuilder x = new StringBuilder();
            holder = null;
            while (x.length() < text.length()) {
                holder = Markov.generateFromText(text, holder);
                x.append(Arrays.toString(holder));
            }
            texter.add(x.toString());
            System.out.println(x);
        }


        double sum = 0.0;
        for (String s : texter) {
            double comp = getCompressedBitSize(LZW.compressText(s));
            double normal = getTHeoreticalBitSize(s);
            sum += 100 * (comp / normal);
        }
        System.out.println("LZW avg: " + sum / 100);

        sum = 0.0;
        for (String s : texter) {
            double comp = getCompressedBitSize(Huffman.compressText(s));
            double normal = getTHeoreticalBitSize(s);
            sum += 100 * (comp / normal);
        }
        System.out.println("Huffman avg: " + sum / 100);

        sum = 0.0;
        for (String s : texter) {
            double comp = getCompressedBitSize(Huffman.compressText(LZW.compressText(s).replace('\u001C', ' ')));
            double normal = getTHeoreticalBitSize(s);
            sum += 100 * (comp / normal);
        }
        System.out.println("LZW + Huffman avg: " + sum / 100);
    }
}
