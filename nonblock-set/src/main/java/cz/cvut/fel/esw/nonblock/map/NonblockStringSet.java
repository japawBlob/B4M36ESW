package cz.cvut.fel.esw.nonblock.map;


public class NonblockStringSet implements StringSet {

    private final int mask;


    public NonblockStringSet(int minSize) {
        if (minSize <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }
        int binsLength = Utils.smallestGreaterPowerOfTwo(minSize);
        this.mask = binsLength - 1;
    }

    @Override
    public void add(String word) {
    }

    @Override
    public boolean contains(String word) {
        return false;
    }

    @Override
    public int size() {
        return calculateSize();
    }

    private int calculateSize() {
        //calculate size by walking through the set
        return 0;
    }

    private int getBinIndex(String word) {
        return word.hashCode() & mask;
    }

    private static class Node {

        private final String word;

        public Node(String word) {
            this.word = word;
        }
    }
}
