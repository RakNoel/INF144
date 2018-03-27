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
    public static String compressText(String original) {
        LZWDictionary dictionary = createDictionary(original);
        String holder = "";
        String previous = "";
        StringBuilder output = new StringBuilder();

        output.append(dictionary.getMax());
        output.append('\u0000');

        for (String s : dictionary)
            output.append(s); //SHOULD ONLY BE OF LENGTH ONE

        output.append('\u001C'); //FILE SPLITTER

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
        return null; //TODO:
    }

    private static String toBinaryString(int decimal, int size) {
        StringBuilder bldr = new StringBuilder();
        String s = new BigInteger(decimal + "").toString(2);

        while (bldr.length() + s.length() < size)
            bldr.append(0);

        return bldr.append(s).toString();
    }

    private static LZWDictionary createDictionary(String original) {
        LZWDictionary dictionary = new LZWDictionary();
        MarkovModel<String> d0 = Markov.getOrder(original, 0);

        for (String s : d0)
            dictionary.put(s);

        dictionary.lock();

        return dictionary;
    }
}

class LZWDictionary implements Iterable<String> {
    private HashMap<String, Integer> dictionary;
    private ArrayList<String> rectionary;
    private int index;
    private int max;
    private boolean locked;

    LZWDictionary() {
        this.dictionary = new HashMap<>();
        this.rectionary = new ArrayList<>();
        this.index = 0;
        this.max = 0;
        this.locked = false;
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

    int get(String key) {
        return dictionary.getOrDefault(key, -1);
    }

    int getMax() {
        return this.max;
    }

    void lock() {
        if (locked)
            return;
        this.locked = true;
        this.max = this.index - 1;
    }

    @Override
    public Iterator<String> iterator() {
        return this.rectionary.iterator();
    }
}