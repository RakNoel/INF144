package compression;

import markov.Markov;
import markov.MarkovModel;

import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Class with huffman usage methods
 *
 * @author RakNoel
 * @version 1.0
 * @since 20.03.18
 */
public class Huffman {

    static PriorityQueue<Node<String>> getFrequencyQueue(String text, StringBuilder queueSaver) {
        MarkovModel<String> d0 = Markov.getOrder(text, 0); //Zeroth markov = freq analysis
        PriorityQueue<Node<String>> treeQueue = new PriorityQueue<>();
        d0.iterator().forEachRemaining(s -> {
            int weight = d0.getNode(s).getWeight(d0.getNode(s));
            queueSaver.append(weight);
            queueSaver.append(s);
            queueSaver.append('\u0000'); //null
            treeQueue.add(new LeafNode<>(s, weight));
        });

        queueSaver.append('\u001C'); //FS ~ File-separator
        return treeQueue;
    }

    static PriorityQueue<Node<String>> extractFrequencyQueue(String text) {
        PriorityQueue<Node<String>> treeQueue = new PriorityQueue<>();
        String[] nodes = text.split("" + '\u0000');

        nodes:
        for (String s : nodes) {
            StringBuilder builder = new StringBuilder();
            char[] ch = s.toCharArray();

            for (int i = 0; i < s.length(); i++)
                if (Character.isDigit(ch[i])) {
                    builder.append(ch[i]);
                } else {
                    String name = s.substring(i);
                    int frequency = Integer.parseInt(builder.toString());
                    treeQueue.add(new LeafNode<>(name, frequency));
                    continue nodes;
                }
        }

        return treeQueue;
    }

    private static SearchNode<String> buildTreeFrom(PriorityQueue<Node<String>> treeQueue) {
        while (treeQueue.size() > 1) {
            Node<String> left = treeQueue.poll();
            Node<String> right = treeQueue.poll();
            treeQueue.add(new SearchNode<>(left, right, left.getTotalWeight() + right.getTotalWeight()));
        }

        Node<String> x = treeQueue.poll();
        return (x instanceof SearchNode) ? (SearchNode<String>) x : null;
    }

    private static HashMap<String, String> createCompresser(String original, StringBuilder queueSaver) {
        HashMap<String, String> compresser = new HashMap<>();
        treeBFS(buildTreeFrom(getFrequencyQueue(original, queueSaver)), "", compresser);
        return compresser;
    }

    private static void treeBFS(Node<String> searchRoot, String path, HashMap<String, String> compresser) {
        if (searchRoot instanceof LeafNode) {
            compresser.put(searchRoot.getName(), path);
            return;
        }

        treeBFS(searchRoot.getLeft(), path + "1", compresser);
        treeBFS(searchRoot.getRight(), path + "0", compresser);
    }

    private static String compressText(String original) {
        StringBuilder outputString = new StringBuilder();
        HashMap<String, String> compresser = createCompresser(original, outputString);

        for (char ch : original.toCharArray())
            outputString.append(compresser.get(ch + ""));

        return outputString.toString();
    }

    private static String[] searchForNode(Node<String> searchRoot, String bitstream) {
        int steps = 0;
        String result = null;
        do {
            if (searchRoot instanceof LeafNode)
                result = searchRoot.getName();
            else
                searchRoot = (bitstream.charAt(steps++) == '1') ? searchRoot.getLeft() : searchRoot.getRight();
        } while (result == null);

        return new String[]{bitstream.substring(steps), result};
    }

    private static String deCompressText(String encoded) {
        StringBuilder outputString = new StringBuilder();
        int devider = encoded.indexOf('\u001C');
        String frequencyTable = encoded.substring(0, devider);
        String bitstream = encoded.substring(devider + 1);

        SearchNode<String> searchRoot = buildTreeFrom(extractFrequencyQueue(frequencyTable));

        do {
            String[] x = searchForNode(searchRoot, bitstream);
            outputString.append(x[1]);
            bitstream = x[0];
        } while (bitstream.length() > 0);

        return outputString.toString();
    }

    static String CompressHuffman(String original) {
        return Huffman.compressText(original);
    }

    static String deCompressHuffman(String encodeed) {
        return Huffman.deCompressText(encodeed);
    }
}