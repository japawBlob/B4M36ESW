package cz.cvut.fel.esw.matrixmultiplication;

import java.util.Random;

public class MatrixUtils {

	/**
	 * Multiply an {@code n*m} matrix {@code a} by an {@code m*p} matrix {@code b} resulting in an
	 * {@code n*p} matrix. The matrix {@code b} is transposed before the multiplication.
	 *
	 * @param a
	 * 		{@code n*m} matrix
	 * @param b
	 * 		{@code m*p} matrix
	 *
	 * @return {@code n*p} matrix
	 */
	public static double[][] multiplyTrans(double[][] a, double[][] b) {
		double[][] ct = transpose(b);
		int n = a.length;
		int m = a[0].length;
		int p = ct.length;

		double[][] res = new double[n][p];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < p; j++) {
				for (int k = 0; k < m; k++) {
					res[i][j] += a[i][k] * ct[j][k];
				}
			}
		}
		return res;
	}

	/**
	 * Multiply an {@code n*m} matrix {@code a} by an {@code m*p} matrix {@code b} by classic approach resulting in an
	 * {@code n*p} matrix.
	 *
	 * @param a
	 * 		{@code n*m} matrix
	 * @param b
	 * 		{@code m*p} matrix
	 *
	 * @return {@code n*p} matrix
	 */
	public static double[][] multiply(double[][] a, double[][] b) {
		int n = a.length;
		int m = a[0].length;
		int p = b[0].length;

		double[][] res = new double[n][p];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < p; j++) {
				for (int k = 0; k < m; k++) {
					res[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		return res;
	}

	/**
	 * Multiply matrices in 1D representation.
	 *
	 * @param a
	 * 		{@code n*m} matrix in 1D representation
	 * @param b
	 * 		{@code m*p} matrix in 1D representation
	 * @param n
	 * 		row number of {@code a}
	 * @param m
	 * 		column number of {@code a} and row number of {@code a}
	 * @param p
	 * 		column number of {@code a}
	 *
	 * @return {@code n*p} matrix in 1D representation
	 */
	public static double[] multiply1D(double[] a, double[] b, int n, int m, int p) {
		double[] res = new double[n * p];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < p; j++) {
				for (int k = 0; k < m; k++) {
					res[i * p + j] += a[i * m + k] * b[k * p + j];
				}
			}
		}
		return res;
	}

	/**
	 * Convert 2D matrix representation to 1D representation
	 *
	 * @param in
	 *
	 * @return
	 */
	public static double[] to1D(double[][] in) {
		int n = in.length;
		int m = in[0].length;
		double[] out = new double[n * m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				out[i * m + j] = in[i][j];
			}
		}
		return out;
	}

	/**
	 * Create transpose of a given {@code matrix}
	 *
	 * @param matrix
	 *
	 * @return
	 */
	public static double[][] transpose(double[][] matrix) {
		int n = matrix.length;
		int m = matrix[0].length;

		double[][] trans = new double[m][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				trans[j][i] = matrix[i][j];
			}
		}
		return trans;
	}

	/**
	 * Generate random {@code n*m} matrix with values within interval {@code [0,ub)}
	 *
	 * @param rnd
	 * @param n
	 * @param m
	 * @param ub
	 *
	 * @return
	 */
	public static double[][] generateMatrix(Random rnd, int n, int m, int ub) {
		double[][] matrix = new double[n][m];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				matrix[i][j] = rnd.nextInt(ub);
			}
		}
		return matrix;
	}
}