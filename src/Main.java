import markov.Markov;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.stream.Collectors;

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
        //String text = new Scanner(new File("res/.txt")).nextLine().toLowerCase();
        String text = new BufferedReader(new FileReader(new File("res/askeladden.txt"))).lines()
                .map(x -> x.toLowerCase().replace(".", "").replace(",", ""))
                .collect(Collectors.joining(" "));

        String[] holder = null;
        for (int i = 0; i <= 10; i++) {
            holder = Markov.generateFromText(text, holder);
            System.out.print(i + " degree :: ");
            System.out.println(Arrays.toString(holder));
        }
    }
}
