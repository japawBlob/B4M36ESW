package cz.cvut.fel.esw.shortestpath.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;
import java.util.zip.ZipFile;

/**
 * @author Marek Cuchý (CVUT)
 */
public class FileParser {

    private final ZipFile zip;
    private final String zipEntryName;
    private final int dataSizeParameterIndex;
    private final Consumer<String[]> dataConsumer;

    private int dataSize = -1;

    private int dataRead = 0;

    /**
     * @param zip
     * @param zipEntryName
     * @param dataSizeParameterIndex index of the parameter on the parameter line that defines the number of the
     *                               following lines representing data
     * @param dataConsumer
     */
    public FileParser(ZipFile zip, String zipEntryName, int dataSizeParameterIndex, Consumer<String[]> dataConsumer) {
        this.zip = zip;
        this.zipEntryName = zipEntryName;
        this.dataSizeParameterIndex = dataSizeParameterIndex;
        this.dataConsumer = dataConsumer;
    }

    public void parse() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(zip.getInputStream(zip.getEntry(zipEntryName))));
        while (dataSize == -1) {
            parseHeaderLine(reader.readLine());
        }

        while (dataRead < dataSize) {
            parseDataLine(reader.readLine());
        }
    }

    private void parseDataLine(String line) {
        String[] split = line.split(" ");
        if ("c".equals(split[0])) return;
        dataConsumer.accept(split);
        dataRead++;
    }

    private void parseHeaderLine(String line) {
        String[] split = line.split(" ");

        switch (split[0]) {
            case "c":
                break;
            case "p":
                dataSize = Integer.parseInt(split[dataSizeParameterIndex]);
                break;
            default:
                throw new IllegalStateException("Unknown line start: " + split[0]);
        }
    }
}
