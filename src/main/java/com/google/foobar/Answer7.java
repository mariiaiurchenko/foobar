package com.google.foobar;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Answer7 {

	public static int answer(int[][] matrix) {
		Matrix m = new Matrix(matrix);
		// Set<Matrix> ddd = m.genrateRevertRowColMatrixs();
		// System.out.println(ddd.size());
		Map<Matrix, Integer> allAnswerMatrix = buildAllMatrix(matrix.length, m);
		System.out.println(allAnswerMatrix.keySet().size());
		// for (Matrix ttt : allAnswerMatrix.keySet()) {
		// System.out.println(allAnswerMatrix.get(ttt));
		// printMatrix(ttt.getBinaryMatrix());
		// System.out.println("");
		// }
		return -1;
		// return (allAnswerMatrix.containsKey(m) ? allAnswerMatrix.get(m) :
		// -1);
	}

	private static Map<Matrix, Integer> buildAllMatrix(int length, Matrix target) {
		Map<Matrix, Integer> allAnswerMatrix = new HashMap<Matrix, Integer>();
		Queue<Matrix> queue = new LinkedList<Matrix>();
		Matrix zeroMatrix = new Matrix(length);
		queue.add(zeroMatrix);
		allAnswerMatrix.put(zeroMatrix, 0);
		// if (target.equals(zeroMatrix)) {
		// return allAnswerMatrix;
		// }

		int level = 1;
		int elOnCurrLevel = 1;
		int countPoll = 0;
		int nextLevelCount = 0;
		while (!queue.isEmpty()) {
			Matrix matrix = queue.poll();
			countPoll++;
			Set<Matrix> newMatrixs = matrix.genrateRevertRowColMatrixs();
			for (Matrix nM : newMatrixs) {
				if (!allAnswerMatrix.containsKey(nM)) {
					allAnswerMatrix.put(nM, level);
					queue.add(nM);
					nextLevelCount++;
				}
			}
			if (countPoll == elOnCurrLevel) {
				level++;
				countPoll = 0;
				elOnCurrLevel = nextLevelCount;
				nextLevelCount = 0;
			}
		}
		return allAnswerMatrix;
	}

	private static class Matrix {

		Map<Integer, Integer> rows;
		Map<Integer, Integer> cols;
		int size;
		int hashCode;

		Matrix(int[][] m) {
			this.size = m.length;
			rows = new HashMap<Integer, Integer>();
			cols = new HashMap<Integer, Integer>();
			for (int i = 0; i < size; i++) {
				int row = 0;
				int col = 0;
				for (int j = 0; j < size; j++) {
					hashCode += m[i][j];
					row += m[i][j];
					col += m[j][i];
				}
				if (rows.containsKey(row)) {
					rows.put(row, rows.get(row) + 1);
				} else {
					rows.put(row, 1);
				}
				if (cols.containsKey(col)) {
					cols.put(col, cols.get(col) + 1);
				} else {
					cols.put(col, 1);
				}
			}
		}

		Matrix(Map<Integer, Integer> cols, Map<Integer, Integer> rows) {
			for (int key : cols.keySet()) {
				size += cols.get(key);
				this.hashCode += key * cols.get(key);
			}

			this.cols = cols;
			this.rows = rows;
		}

		Matrix(int size) {
			this.size = size;
			this.rows = new HashMap<Integer, Integer>();
			this.cols = new HashMap<Integer, Integer>();
			this.rows.put(0, size);
			this.cols.put(0, size);
		}

		Set<Matrix> genrateRevertRowColMatrixs() {
			Set<Matrix> res = new HashSet<Matrix>();
			for (int row : rows.keySet()) {
				for (int col : cols.keySet()) {
					Set<Map<Integer, Integer>> revertedCol = revertCols(row,
							col);
					Set<Map<Integer, Integer>> revertedRow = revertRows(row,
							col);
					for (Map<Integer, Integer> rC : revertedCol) {
						for (Map<Integer, Integer> rR : revertedRow) {
							res.add(new Matrix(rC, rR));
						}
					}
				}
			}

			return res;
		}

		private Set<Map<Integer, Integer>> revertRows(int row, int col) {
			return calcNextMaps(row, col, rows, size);
		}

		private Set<Map<Integer, Integer>> revertCols(int row, int col) {
			return calcNextMaps(col, row, cols, size);
		}

		private static Set<Map<Integer, Integer>> calcNextMaps(int selectRow,
				int selectCol, Map<Integer, Integer> rows, int size) {
			int ones = size - selectCol;
			int zeros = selectCol;
			Map<Integer, Integer> currMatrix = new HashMap<Integer, Integer>();
			Queue<Integer> rowsList = new LinkedList<Integer>(rows.keySet());
			currMatrix.put(size - selectRow, 1);
			Set<Map<Integer, Integer>> newMatrixs = new HashSet<Map<Integer, Integer>>();
			if (selectRow > 0 && selectCol > 0) {
				newMatrixs.addAll(calcNextMaps(selectRow, ones, zeros - 1,
						currMatrix, new LinkedList<Integer>(rowsList), rows,
						size));
			}
			if (selectRow < size && selectCol < size) {
				newMatrixs.addAll(calcNextMaps(selectRow, ones - 1, zeros,
						currMatrix, new LinkedList<Integer>(rowsList), rows,
						size));
			}
			return newMatrixs;
		}

		private static Set<Map<Integer, Integer>> calcNextMaps(int selectRow,
				int ones, int zeros, Map<Integer, Integer> currMatrix,
				Queue<Integer> rowsList, Map<Integer, Integer> rows, int size) {
			int row = rowsList.poll();
			int numOfRows = (row == selectRow) ? rows.get(row) - 1 : rows
					.get(row);
			int maxOnes = Math.min(ones, numOfRows);
			int minOnes = numOfRows - Math.min(zeros, numOfRows);
			Set<Map<Integer, Integer>> newMaps = new HashSet<Map<Integer, Integer>>();
			for (int i = minOnes; i <= maxOnes; i++) {
				Map<Integer, Integer> nextMatrix = new HashMap<Integer, Integer>(
						currMatrix);
				if (row < size && i > 0) {
					if (nextMatrix.containsKey(row + 1)) {
						nextMatrix.put(row + 1, nextMatrix.get(row + 1) + i);
					} else {
						nextMatrix.put(row + 1, i);
					}
				}
				if (row > 0 && numOfRows - i > 0) {
					if (nextMatrix.containsKey(row - 1)) {
						nextMatrix.put(row - 1, nextMatrix.get(row - 1)
								+ numOfRows - i);
					} else {
						nextMatrix.put(row - 1, numOfRows - i);
					}
				}
				if (rowsList.isEmpty()) {
					newMaps.add(nextMatrix);
				} else {
					newMaps.addAll(calcNextMaps(selectRow, ones - i, zeros
							- (numOfRows - i), nextMatrix,
							new LinkedList<Integer>(rowsList), rows, size));
				}
			}
			return newMaps;
		}

		@Override
		public int hashCode() {
			return hashCode;
		}

		@Override
		public boolean equals(Object obj) {
			Matrix other = (Matrix) obj;
			if ((cols.equals(other.cols) && rows.equals(other.rows))
					|| (cols.equals(other.rows) && rows.equals(other.cols)))
				return true;
			return false;
		}
	}

	private static void printMatrix(int[][] matrix) {
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix.length; col++) {
				System.out.print(matrix[row][col] + " ");
			}
			System.out.println("");
		}

	}

	public static void main(String[] args) {
		// String str = convertMaxrixToStr(new int[][] { { 1, 0 }, { 1, 0 } });
		// printMatrix(convertStrToMatrix(str));
		int[][] a = new int[][] { { 1, 1 }, { 0, 0 } };
		int[][] b = new int[][] { { 1, 1, 1 }, { 1, 0, 0 }, { 1, 0, 1 } };
		int[][] d = new int[][] { { 0, 0, 0, 0 }, { 0, 0, 0, 0 },
				{ 0, 0, 0, 0 }, { 0, 0, 0, 0 } };

		// printMatrix(b);
		// System.out.println("");
		// printMatrix(new Matrix(b).getBinaryMatrix());
		int[][] c = new int[][] {
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
		// System.out.println(answer(a));
		System.out.println(answer(a));
		// System.out.println(answer(c));

	}
}
