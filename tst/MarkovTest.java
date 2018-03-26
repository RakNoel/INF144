import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

/**
 * TODO: Describe test
 *
 * @author RakNoel
 * @version 1.0
 * @since 23.03.18
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MarkovTest {
    private static String short_story = null;   //Askeladden
    private static String long_story = null;    //Moby dick

    @BeforeClass
    public static void onlyonce() throws FileNotFoundException {
        short_story = new BufferedReader(new FileReader(new File("res/askeladden.txt"))).lines()
                .collect(Collectors.joining(" "));
        long_story = new BufferedReader(new FileReader(new File("res/mobydick.txt"))).lines()
                .collect(Collectors.joining(" "));
    }

    /**
     * Will test if the self-frequency count is correct. We can know this
     * by testing the weight to self and the degree of each node = 1. Hence
     * the avg degree should be equal to exactly 1.
     * <p>
     * This will also confirm if number of nodes is not correct
     */
    @Test
    public void Degree_test() {
        String testcase = "aabbbahhhiikikk";
        MarkovModel<String> m1 = Markov.getOrder(testcase, 0);

        int sumNodes = 0;
        int sumEdge = 0;

        for (String n : m1) {
            sumNodes++;
            for (State ignored : m1.getNode(n))
                sumEdge++;
        }

        assertTrue("Avg degree != 1", sumEdge / sumNodes == 1.0);

        for (char ch : testcase.toCharArray())
            assertTrue("Weight to self != 3", m1.getNode("" + ch).getWeight(m1.getNode("" + ch)) == 3);

    }

    /**
     * Test to see if the generated model has the correct order
     */
    @Test
    public void Order_test() {
        TestOrders(short_story, 5);
    }

    /**
     * Test to see the time it takes to analyse huge files.
     */
    @Test
    public void Speed_test() {
        TestOrders(long_story, 3);
    }

    /**
     * Method to test the orders generated from a given text up to
     * a given order
     *
     * @param text      text from which to make model
     * @param uptoorder the maximum order to test starting form 1
     */
    private void TestOrders(String text, int uptoorder) {
        //Ignore test for 0
        for (int i = 1; i < uptoorder; i++) {
            MarkovModel<String> m1 = Markov.getOrder(text, i);
            for (String n : m1)
                assertTrue("Order " + i + " not correct", n.length() == i);
        }
    }
}