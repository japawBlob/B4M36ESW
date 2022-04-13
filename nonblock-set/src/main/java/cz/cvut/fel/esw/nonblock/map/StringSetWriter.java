package cz.cvut.fel.esw.nonblock.map;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * @author Marek Cuchý
 */
public class StringSetWriter implements Callable<List<String>> {

    private final StringSet dict;
    private final List<String> toBeAdded;


    public StringSetWriter(int count, StringSet dict, int stringLength, Random rnd) {
        this.dict = dict;
        this.toBeAdded = Utils.generateRandomStrings(stringLength, count, rnd);
    }

    @Override
    public List<String> call() {
        for (String word : toBeAdded) {
            dict.add(word);
        }
        return toBeAdded;
    }
}
