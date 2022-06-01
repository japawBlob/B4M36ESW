package cz.cvut.fel.esw.nonblock.map;


import java.util.concurrent.atomic.AtomicReference;

public class SynchronizedStringSet implements StringSet {

    private final int mask;

    private int size = 0;

    private final Node[] bins;
    private final int binsLength;

    public SynchronizedStringSet(int minSize) {
        if (minSize <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }
        int binsLength = Utils.smallestGreaterPowerOfTwo(minSize);
        this.mask = binsLength - 1;
        this.bins = new Node[binsLength];
        this.binsLength = binsLength;
    }

    @Override
    public synchronized void add(String word) {
        int binIndex = getBinIndex(word);
        Node bin = bins[binIndex];
        if (bin == null) {
            bins[binIndex] = new Node(word);
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
        for (Node n = bins[binIndex]; n != null; n = n.next) {
            if (n.word.equals(word)) {
                return true;
            }
        }
        return false;
    }

    private int getBinIndex(String word) {
        return word.hashCode() & mask;
    }

    @Override
    public int size() {
        //return calculateSize();
        return size;
    }

    private int calculateSize() {
        int size = 0;
        for (int i = 0; i<binsLength;i++){
            if (bins[i] != null){
                size++;
                Node bin = bins[i].next;
                while (bin != null){
                    size++;
                    bin = bin.next;
                }
            }
        }
        return size;
    }

    private static class Node {

        private final String word;
        private Node next;

        public Node(String word) {
            this(word, null);
        }

        public Node(String word, Node next) {
            this.word = word;
            this.next = next;
        }
    }
}
