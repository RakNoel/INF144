package markov;

/**
 * Class with functions made to represent the markov.Markov model.
 * <p>
 * Main point of this class is to generate random strings
 * from a textfile with ordered approximations. Hopefully
 * getting humanly readable text/words out.
 *
 * @author RakNoel
 * @version 1.0
 * @since 20.03.18
 */
public class Markov {
    /**
     * Method to generate strings from the given text using the markov model,
     * and possibly at another degree if given a non-empty table
     *
     * @param text   Text to make model from
     * @param preGen nullable
     * @return returns a new String[] filled with generated strings
     */
    public static String[] generateFromText(String text, String[] preGen) {
        if (preGen == null) {
            preGen = new String[30];
            MarkovModel<String> d0 = getOrder(text, 0);
            for (int i = 0; i < preGen.length; i++)
                preGen[i] = d0.getRandomFreqNode() + "";

            return preGen;
        } else {
            int order = preGen[0].length();
            MarkovModel<String> d0 = getOrder(text, order);

            for (int i = 0; i < preGen.length; i++)
                preGen[i] += d0.getNextNode(preGen[i]);

            return preGen;
        }
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