package com.google.foobar;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Answer {

	private static final int maxN = 20;
	private static Map<Integer, BigDecimal> cashBinomialCoeff = new HashMap<Integer, BigDecimal>();
	private static Map<Integer, BigDecimal> cashNumberOfPosibleGraph = new HashMap<Integer, BigDecimal>();

	public static String answer(int N, int K) {
		return numberOfPosibleGraph(N, K).toString();
	}

	private static BigDecimal numberOfPosibleGraph(int vertices, int edges) {
		if (vertices == 2 || vertices == 1) {
			return BigDecimal.ONE;
		}
		int cashKey = vertices * maxN + edges;
		if (cashNumberOfPosibleGraph.containsKey(cashKey)) {
			return cashNumberOfPosibleGraph.get(cashKey);
		}
		int firstGraph = vertices / 2;
		int secondGraph = firstGraph + isOdd(vertices);
		int maxEdgesBetween = Math.min(secondGraph * firstGraph, edges
				- (firstGraph + secondGraph - 2));
		int minEdgesBetween = Math.max(1, edges
				- numberOfAllPosibleAdges(firstGraph)
				- numberOfAllPosibleAdges(secondGraph));
		BigDecimal count = BigDecimal.ZERO;
		for (int i = maxEdgesBetween; i >= minEdgesBetween; i--) {
			BigDecimal coeffForEdges = binomialCoeff(firstGraph * secondGraph,
					i);
			// binomialCoeff(firstGraph, i).multiply(binomialCoeff(secondGraph,
			// i));
			int minEdgesInFirstG = firstGraph - 1;
			int maxEdgesInFirstG = Math.min(
					numberOfAllPosibleAdges(firstGraph), edges - i
							- secondGraph + 1);
			BigDecimal localCount = BigDecimal.ZERO;
			for (int j = minEdgesInFirstG; j <= maxEdgesInFirstG; j++) {
				BigDecimal countFirstG = numberOfPosibleGraph(firstGraph, j);
				int secondGEdges = edges - i - j;
				BigDecimal countSecondG = numberOfPosibleGraph(secondGraph,
						secondGEdges);
				localCount = localCount.add(countFirstG.multiply(countSecondG));
			}
			count = count.add(localCount.multiply(coeffForEdges));
		}
		//count = count.multiply(binomialCoeff(vertices, firstGraph));
		cashNumberOfPosibleGraph.put(cashKey, count);
		return count;
	}

	private static int isOdd(int n) {
		return n & 1;
	}

	private static int numberOfAllPosibleAdges(int n) {
		return n * (n - 1) / 2;
	}

	private static BigDecimal binomialCoeff(int n, int k) {
		BigDecimal res = BigDecimal.ONE;
		if (k > n - k) {
			k = n - k;
		}
		int cashKey = n * maxN + k;
		if (cashBinomialCoeff.containsKey(cashKey)) {
			return cashBinomialCoeff.get(cashKey);
		}
		for (int i = 0; i < k; i++) {
			res = res.multiply(BigDecimal.valueOf(n - i));
			res = res.divide(BigDecimal.valueOf(i + 1));
		}
		cashBinomialCoeff.put(cashKey, res);
		return res;
	}

	public static void main(String[] args) {
		int n;
		int k;
		n = 20;
		k = 189;
		System.out.println("N = " + n + " K = " + k);
		System.out.println("answer = " + numberOfPosibleGraph(n, k));
		n = 3;
		k = 3;
		System.out.println("N = " + n + " K = " + k);
		System.out.println("answer = " + numberOfPosibleGraph(n, k));
		n = 3;
		k = 2;
		System.out.println("N = " + n + " K = " + k);
		System.out.println("answer = " + numberOfPosibleGraph(n, k).toString());
		n = 3;
		k = 2;
		System.out.println("N = " + n + " K = " + k);
		System.out.println("answer = " + answer(n, k));
		n = 2;
		k = 1;
		System.out.println("N = " + n + " K = " + k);
		System.out.println("answer = " + answer(n, k));
		n = 20;
		k = 189;
		System.out.println("N = " + n + " K = " + k);
		System.out.println("answer = " + answer(n, k));
	}
}
