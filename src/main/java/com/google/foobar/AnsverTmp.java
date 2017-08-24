package com.google.foobar;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class AnsverTmp {
    private static int[][] leftRight;
    private static int[][] upDown;
    private static int[][] leftDiag;
    private static int[][] rightDiag;

    private static Map<String, Integer> matrices = new HashMap<String, Integer>();

    public static int answer(int[][] matrix) {

        int n = matrix.length;
        leftRight = new int[n][n];
        upDown = new int[n][n];
        leftDiag = new int[n][n];
        rightDiag = new int[n][n];

        String matrixStr = convertMaxrixToStr(matrix);
        buildMatrices(n);
        return (matrices.containsKey(matrixStr) ? matrices.get(matrixStr) : -1);
    }

    private static void buildMatrices(int n) {
        Queue<String> queue = new LinkedList<String>();
        String zeroMatrix = convertMaxrixToStr(new int[n][n]);
        queue.add(zeroMatrix);
        matrices.put(zeroMatrix, 0);

        int level = 1;
        int elOnCurrLevel = 1;
        int countPoll = 0;
        int nextLevelCount = 0;
        while (!queue.isEmpty()) {
            int[][] matrix = convertStrToMatrix(queue.poll());
            countPoll++;
            for (int row = 0; row < n; row++) {
                for (int col = 0; col < n; col++) {
                    int[][] revMatrix = reverseRowCol(matrix, row, col);
                    if (addSymmetric(revMatrix, level)) {
                        queue.add(convertMaxrixToStr(revMatrix));
                        nextLevelCount += 5;
                    }
                }
            }
            if (countPoll == elOnCurrLevel) {
                level++;
                countPoll = 0;
                elOnCurrLevel = nextLevelCount;
                nextLevelCount = 0;
            }
        }
    }

    private static String convertMaxrixToStr(int[][] matrix) {
        StringBuilder str = new StringBuilder("");
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix.length; col++) {
                str.append(matrix[col][row]);
            }
        }
        return str.toString();
    }

    private static int[][] convertStrToMatrix(String str) {
        int size = (int) Math.sqrt(str.length());
        int[][] matrix = new int[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                matrix[col][row] = Integer.parseInt(""
                        + str.charAt(row * size + col));
            }
        }
        return matrix;
    }

    private static boolean addSymmetric(int[][] matrix, int level) {
        String matrixNum = convertMaxrixToStr(matrix);
        if (!matrices.containsKey(matrixNum)) {
            matrices.put(matrixNum, level);
            addSymmetricMatrices(matrix, level);
            return true;
        }
        return false;
    }

    private static void addSymmetricMatrices(int[][] matrix, int level) {
        int n = matrix.length;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                leftRight[row][n - col - 1] = matrix[row][col];
                upDown[n - row - 1][col] = matrix[row][col];
                leftDiag[n - col - 1][n - row - 1] = matrix[row][col];
                rightDiag[col][row] = matrix[row][col];
            }
        }
        matrices.put(convertMaxrixToStr(leftRight), level);
        matrices.put(convertMaxrixToStr(upDown), level);
        matrices.put(convertMaxrixToStr(leftDiag), level);
        matrices.put(convertMaxrixToStr(rightDiag), level);
    }

    private static int[][] reverseRowCol(int[][] matrix, int row, int col) {
        int[][] res = new int[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                res[i][j] = matrix[i][j];
            }
        }
        res[row][col] ^= 1;
        for (int i = 0; i < matrix.length; i++) {
            if (i != row) {
                res[i][col] ^= 1;
            }
            if (i != col) {
                res[row][i] ^= 1;
            }
        }
        return res;
    }

    public static void main(String... args) {
        int[][] a = new int[][] { { 1, 1 }, { 0, 0 } };
        int[][] b = new int[][] {
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
        System.out.println(answer(a));
       // System.out.println(answer(b));
    }
}

