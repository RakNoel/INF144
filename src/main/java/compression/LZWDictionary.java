package compression;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class LZWDictionary implements Iterable<String> {
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