import markov.Markov;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * CompressionMain class to be run, generating all results
 * for the assignment.
 *
 * @author RakNoel
 * @version 1.0
 * @since 27.03.18
 */
public class MarkovMain {

    public static void main(String[] args) throws Exception {

        URL url_short = MarkovMain.class.getResource("Folktale.txt");
        URL url_long = MarkovMain.class.getResource("mobydick.txt");

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
        Scanner scn = new Scanner(System.in);
        System.out.println("Please enter the number of generations to process");
        int generations = Integer.parseInt(scn.nextLine());

        System.out.println("Please enter the character length of the text");
        int generateLength = Integer.parseInt(scn.nextLine());

        for (int i = 0; i <= generations; i++) {

            int finalI = i;
            new Thread(() -> System.out.printf("%d degree :: %s %n", finalI, Markov.generateRandomStoryFromText(text, finalI, generateLength, false))).start();
        }
    }
}
