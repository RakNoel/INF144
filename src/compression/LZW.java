package compression;

import markov.Markov;
import markov.MarkovModel;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * TODO: Describe class
 *
 * @author RakNoel
 * @version 1.0
 * @since 27.03.18
 */
public class LZW {

    private static String getStandardDictionary() {
        StringBuilder bldr = new StringBuilder();

        for (int i = 32; i < 127; i++)
            bldr.append((char) i);

        bldr.append('æ');
        bldr.append('ø');
        bldr.append('å');
        bldr.append('Æ');
        bldr.append('Ø');
        bldr.append('Å');

        return bldr.toString();
    }

    public static String compressText(String original) {
        LZWDictionary dictionary = createDictionary(getStandardDictionary());
        String holder = "";
        String previous = "";
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < original.length(); i++) {
            holder += original.charAt(i);
            if (dictionary.put(holder)) {
                output.append(toBinaryString(dictionary.get(previous), dictionary.getIndexBinary()));
                holder = "";
                i--;
            }
            previous = holder;
        }

        if (holder.length() > 0) {
            output.append(toBinaryString(dictionary.get(previous), dictionary.getIndexBinary()));
        }

        return output.toString();
    }

    public static String deCompressText(String encoded) {
        StringBuilder output = new StringBuilder();
        String encodedPart = encoded;

        LZWDictionary dictionary = createDictionary(getStandardDictionary());

        String working = "";
        while (encodedPart.length() > 0) {
            int workingLength = dictionary.getNextIndexBinary(); //How many bits to read
            StringBuilder bitreader = new StringBuilder();
            int selector = 0;

            //Read inn bits
            while (selector < workingLength && selector < encodedPart.length())
                bitreader.append(encodedPart.charAt(selector++));

            encodedPart = encodedPart.substring(selector);

            //Cast binary to integer
            int readNumber = Integer.parseInt(new BigInteger(bitreader.toString(), 2).toString());

            String getter = dictionary.get(readNumber);

            if (encodedPart.length() == 0) {
                output.append(working);
                output.append(getter);
                break;
            }

            if (dictionary.put(working + getter.charAt(0))) {
                output.append(working);
                System.out.print(working);
                working = getter;
            } else {
                working += getter;
            }


        }

        return output.toString();
    }

    private static String toBinaryString(int decimal, int size) {
        StringBuilder bldr = new StringBuilder();
        String s = new BigInteger(decimal + "").toString(2);

        while (bldr.length() + s.length() < size)
            bldr.append(0);

        return bldr.append(s).toString();
    }

    static LZWDictionary createDictionary(String original) {
        LZWDictionary dictionary = new LZWDictionary();
        MarkovModel<String> d0 = Markov.getOrder(original, 0);

        for (String s : d0)
            dictionary.put(s);

        return dictionary;
    }
}

class LZWDictionary implements Iterable<String> {
    private HashMap<String, Integer> dictionary;
    private ArrayList<String> rectionary;
    private int index;

    LZWDictionary() {
        this.dictionary = new HashMap<>();
        this.rectionary = new ArrayList<>();
        this.index = 0;
    }

    boolean put(String s) {
        if (!dictionary.containsKey(s)) {
            dictionary.put(s, index++);
            this.rectionary.add(s);
            return true;
        }
        return false;
    }

    int getIndex() {
        return this.index - 1;
    }

    int getIndexBinary() {
        return new BigInteger("" + (this.index - 1)).toString(2).length();
    }

    int getNextIndexBinary() {
        return new BigInteger("" + (this.index + 1)).toString(2).length();
    }

    int get(String key) {
        return dictionary.getOrDefault(key, -1);
    }

    String get(int key) {
        return rectionary.get(key);
    }

    @Override
    public Iterator<String> iterator() {
        return this.rectionary.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LZWDictionary))
            return false;

        LZWDictionary other = (LZWDictionary) o;

        for (String s : this.rectionary)
            if (other.get(s) == -1)
                return false;

        return (this.getIndex() == other.getIndex());
    }
}