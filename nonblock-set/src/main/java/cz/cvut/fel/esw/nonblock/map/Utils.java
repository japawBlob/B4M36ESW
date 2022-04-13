package cz.cvut.fel.esw.nonblock.map;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    private static final int LEFT_LIMIT = 97; // a
    private static final int RIGHT_LIMIT = 122; // z

    /*
     * https://www.baeldung.com/java-random-string
     */
    public static List<String> generateDistinctRandomStrings(int stringLength, int count, Random rnd) {
        return Stream.generate(() -> rnd.ints(stringLength, LEFT_LIMIT, RIGHT_LIMIT)
                                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                        .toString()).distinct().limit(count).collect(Collectors.toList());
    }
    /*
     * https://www.baeldung.com/java-random-string
     */

    public static List<String> generateRandomStrings(int stringLength, int count, Random rnd) {
        return Stream.generate(() -> rnd.ints(stringLength, LEFT_LIMIT, RIGHT_LIMIT)
                                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                        .toString()).limit(count).collect(Collectors.toList());
    }

    public static int smallestGreaterPowerOfTwo(int minSize) {
        return 1 << (32 - Integer.numberOfLeadingZeros(minSize - 1));
    }
}
