package markov;

/**
 * Class with functions made to represent the markov.Markov model.
 * <p>
 * Main point of this class is to generate random strings
 * from a textfile with ordered approximations. Hopefully
 * getting humanly readable text/words out.
 *
 * @author RakNoel
 * @version 2.0
 * @since 01.03.19
 */
public class Markov {

    /**
     * Method to generate text of a given length with a markov-chain of given order from
     * the given text.
     *
     * @param text       The text to analyze and from which to build the markov-model
     * @param order      the order of the markov-model
     * @param length     the length of the final text, should be more than 3 * order or result unstable
     * @param endAtSpace Logical value if the returned string should end naturaly under or exactly at the length
     * @return A randomly generated story from the parameters.
     */
    public static String generateRandomStoryFromText(String text, int order, int length, boolean endAtSpace) {
        MarkovModel<String> d1 = getOrder(text, order);
        StringBuilder result = new StringBuilder();
        String gen = "";

        //Initialize a start
        result.append(d1.getRandomFreqNode());

        do {
            if (order > 0) {
                gen = result.substring(result.length() - order);
                result.append(d1.getNextNode(gen));
            } else {
                result.append(d1.getRandomFreqNode());
            }
        } while (result.length() <= length);

        if (endAtSpace)
            while (result.charAt(result.length() - 1) != ' ')
                result.deleteCharAt(result.length() - 1);

        return result.toString();
    }

    /**
     * Method to create a markov.Markov-model-graph of given order for a text
     *
     * @param input the text from which to model
     * @param order the order of how large each node should be
     * @return A markov-model
     */
    public static MarkovModel<String> getOrder(String input, int order) {
        MarkovModel<String> holder = new MarkovModel<>();

        char[] text = input.toCharArray();
        for (int i = 0; i < text.length - order; i++) {
            StringBuilder key = new StringBuilder("" + text[i]);
            String value = "" + text[i + order];

            for (int k = 1; k < order; k++)
                key.append(text[i + k]);

            holder.addInstance(key.toString(), value);
        }
        return holder;
    }
}