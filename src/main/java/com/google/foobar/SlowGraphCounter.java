package com.google.foobar;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class SlowGraphCounter {

	private static final int maxN = 20;
	private static Map<Integer, BigDecimal> cashBinomialCoeff = new HashMap<Integer, BigDecimal>();
	private static Map<Integer, BigDecimal> cashNumberOfPosibleGraph = new HashMap<Integer, BigDecimal>();
	private static Map<Integer, BigDecimal> cashPosibleGraphGroups = new HashMap<Integer, BigDecimal>();

	public static String answer(int N, int K) {
		return numberOfPosibleGraph(N, K).toString();
	}

	private static BigDecimal numberOfPosibleGraph(int vertices, int edges) {
		if (edges == numberOfAllPosibleAdges(vertices) || vertices == 0
				|| vertices == 1 || vertices == 2) {
			return BigDecimal.ONE;
		}
		if (edges < vertices - 1) {
			return BigDecimal.ZERO;
		}
		int cashKey = edges * numberOfAllPosibleAdges(maxN) + vertices;
		if (cashNumberOfPosibleGraph.containsKey(cashKey)) {
			return cashNumberOfPosibleGraph.get(cashKey);
		}
		BigDecimal count = BigDecimal.ZERO;
		for (int vEdge = 1; vEdge <= edges; vEdge++) {
			count = count.add(posibleGraphGroups(vEdge, vertices - 1, edges
					- vEdge).multiply(binomialCoeff(vertices-1, vEdge)));
		}
		cashNumberOfPosibleGraph.put(cashKey, count);
		return count;
	}

	private static BigDecimal posibleGraphGroups(int subGraphsCountMax,
			int vertices, int edges) {
		if (edges > numberOfAllPosibleAdges(vertices)
				|| subGraphsCountMax > vertices
				|| (subGraphsCountMax == 0 && (vertices != 0 || edges != 0))) {
			return BigDecimal.ZERO;
		}
		if (subGraphsCountMax == 1) {
			if (edges >= vertices - 1) {
				return numberOfPosibleGraph(vertices, edges);//.multiply(BigDecimal.valueOf(vertices));
			}
			return BigDecimal.ZERO;
		}
		if (subGraphsCountMax == 0 && vertices == 0 && edges == 0) {
			return BigDecimal.ONE;
		}
		int cashKey = subGraphsCountMax
				* (int) Math.pow(numberOfAllPosibleAdges(maxN), 2) + edges
				* numberOfAllPosibleAdges(maxN) + vertices;
		if (cashPosibleGraphGroups.containsKey(cashKey)) {
			return cashPosibleGraphGroups.get(cashKey);
		}
		BigDecimal count = BigDecimal.ZERO;
		for (int edgesToFirstGraph = 1; edgesToFirstGraph <= subGraphsCountMax; edgesToFirstGraph++) {
			BigDecimal coeff = BigDecimal.ONE;//binomialCoeff(vertices, edgesToFirstGraph);
			int vertToSubGraphs = subGraphsCountMax - edgesToFirstGraph;
			int minVertFirstG = edgesToFirstGraph;
			int maxVertFirstG = vertices - vertToSubGraphs;
			BigDecimal localCount = BigDecimal.ZERO;
			for (int i = minVertFirstG; i <= maxVertFirstG; i++) {
				int minEdgeFirstG = Math.max(i - 1, edges - numberOfAllPosibleAdges(vertices - i));
				int maxEdgeFirstG = Math.min(numberOfAllPosibleAdges(i), edges);
				for (int j = minEdgeFirstG; j <= maxEdgeFirstG; j++) {
					BigDecimal firstG = numberOfPosibleGraph(i, j);
					BigDecimal groups = posibleGraphGroups(vertToSubGraphs, vertices - i, edges - j);
					localCount = localCount.add(firstG.multiply(groups));
				}
			}
			count = count.add(coeff.multiply(localCount));
		}
		cashPosibleGraphGroups.put(cashKey, count);
		return count;
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
		n = 4;
		k = 3;
		System.out.println("N = " + n + " K = " + k);
		System.out.println("answer = " + numberOfPosibleGraph(n, k).toString());
		n = 20;
		k = 188;
		System.out.println("N = " + n + " K = " + k);
		System.out.println("answer = " + numberOfPosibleGraph(n, k).toString());
		n = 4;
		k = 4;
		System.out.println("N = " + n + " K = " + k);
		System.out.println("answer = " + numberOfPosibleGraph(n, k));
		n = 4;
		k = 6;
		System.out.println("N = " + n + " K = " + k);
		System.out.println("answer = " + numberOfPosibleGraph(n, k));
		n = 3;
		k = 2;
		System.out.println("N = " + n + " K = " + k);
		System.out.println("answer = " + answer(n, k));
		n = 2;
		k = 1;
		System.out.println("N = " + n + " K = " + k);
		System.out.println("answer = " + answer(n, k));
	}

}
