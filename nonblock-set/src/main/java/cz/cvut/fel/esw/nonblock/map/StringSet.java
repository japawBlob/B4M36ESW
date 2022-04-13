package cz.cvut.fel.esw.nonblock.map;

import java.util.List;

public interface StringSet {

    void add(String word);

    default void addAll(List<String> words) {
        for (String word : words) {
            add(word);
        }
    }

    boolean contains(String word);

    int size();
}
