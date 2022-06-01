package cz.cvut.fel.esw.nonblock.map;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.UnaryOperator;

public class NonblockStringSet implements StringSet {

    private final int mask;

    private final AtomicReferenceArray<Node> bins;

    private AtomicInteger size = new AtomicInteger(0);

    private ReentrantLock mutex = new ReentrantLock();

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
//        Node bin = this.bins.get(binIndex);
//        if (bin == null) {
//            bins.getAndSet(binIndex, new Node(word));
//            size.addAndGet(1);
//            return;
//        }
        if (this.bins.compareAndSet(binIndex, null, new Node(word))){
            return;
        }
        //AtomicReferenceFieldUpdater Updater = AtomicReferenceFieldUpdater.newUpdater(Node, )
        Node bin = this.bins.get(binIndex);
        while (true) {
            if (bin.word.equals(word)) {
                return;
            } else {
//                if (bin.next.compareAndSet(null, new Node(word))) {
//                    return;
//                }
                if (bin.next == null) {
                    bin.next = new AtomicReference<>(new Node(word));/* na toto místo se můžou dostat zároveň 2 thready - bylo by fajn udělat tento assign atomicky */
                    return;
                }
                bin = bin.next.get();
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
                bin = bin.next.get();
            }
        }
    }

    @Override
    public int size() {
        //return 10;
        return calculateSize();
    }

    private int calculateSize() {
        int size = 0;
        for (int i = 0; i<bins.length();i++){
            if (bins.get(i) != null){
                size++;
                AtomicReference<Node> bin = bins.get(i).next;
                while (bin != null){
                    size++;
                    bin = bin.get().next;
                }
            }
        }
        return size;
    }

    private int getBinIndex(String word) {
        return word.hashCode() & mask;
    }

    private static class Node {

        private final String word;
        private AtomicReference<Node> next;

        public Node(String word) {
            this.word = word;
            this.next = null;
        }
        public Node(String word, Node next) {
            this.word = word;
            this.next = new AtomicReference<>(next);
        }
        public void setNext (String word){
            this.next = new AtomicReference<>(new Node(word));
        }
    }
}
