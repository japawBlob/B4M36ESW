package cz.cvut.fel.esw.nonblock.map;


import javax.xml.stream.FactoryConfigurationError;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class NonblockStringSet implements StringSet {

    private final int mask;

    private final AtomicReferenceArray<Node> bins;

    private int size = 0;

    public NonblockStringSet(int minSize) {
        if (minSize <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }
        int binsLength = Utils.smallestGreaterPowerOfTwo(minSize);
        this.mask = binsLength - 1;
        this.bins = new AtomicReferenceArray<Node>(binsLength);
    }

    @Override
    public void add(String word) {
        int binIndex = getBinIndex(word);
        Node bin = this.bins.get(binIndex);
        if (bin == null) {
            bins.set(binIndex, new Node(word));
            size++;
            return;
        }
        while (true) {
            if (bin.word.equals(word)) {
                return;
            } else {
                if (bin.next == null) {
                    size++;
                    bin.next = new Node(word);
                    return;
                }
                bin = bin.next;
            }
        }
    }

    @Override
    public boolean contains(String word) {
        int binIndex = getBinIndex(word);
        Node bin = this.bins.get(binIndex);
        if (bin == null) {
            return false;
        }
        while (true) {
            if (bin.word.equals(word)) {
                return true;
            } else {
                if (bin.next == null) {
                    return false;
                }
                bin = bin.next;
            }
        }
    }

    @Override
    public int size() {
        return calculateSize();
    }

    private int calculateSize() {

        //calculate size by walking through the set
        return size;
    }

    private int getBinIndex(String word) {
        return word.hashCode() & mask;
    }

    private static class Node {

        private final String word;
        private Node next;

        public Node(String word) {
            this.word = word;
        }
        public Node(String word, Node next) {
            this.word = word;
            this.next = next;
        }
    }
}
