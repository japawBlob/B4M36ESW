package cz.cvut.fel.esw.matrixmultiplication;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Marek Cuchý
 */
public class Main {

    public static final int MAX = 1000; // value upper bound of the randomly generated matrices

    //matrices dimensions
    public static final int N = 968;
    public static final int M = 333;
    public static final int P = 555;

    public static void main(String[] args) throws InterruptedException {

        Random rnd = ThreadLocalRandom.current();

        double[][] a = MatrixUtils.generateMatrix(rnd, N, M, MAX);
        double[][] b = MatrixUtils.generateMatrix(rnd, M, P, MAX);
        double[] a1D = MatrixUtils.to1D(a);
        double[] b1D = MatrixUtils.to1D(b);


        double[][] c = new double[0][];
        double[] c1D = new double[0];


        for (int i = 0; i < 300000; i++) {

            long t1 = System.nanoTime();
            c = MatrixUtils.multiply(a, b);
            //c = MatrixUtils.multiplyTrans(a, b);
            //c1D = MatrixUtils.multiply1D(a1D, b1D, N, M, P);
            long t2 = System.nanoTime();
            System.out.println("TIME: " + (t2 - t1) / 1000000 + "ms");
        }
    }

}
