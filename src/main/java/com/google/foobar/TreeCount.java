package com.google.foobar;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TreeCount {
	private static final int maxN = 50;
	private static Map<Integer, BigDecimal> cashBinomialCoeff = new HashMap<Integer, BigDecimal>();
	private static Map<Integer, BigDecimal> cashNumberOfPosibleTree = new HashMap<Integer, BigDecimal>();
	private static Map<Integer, BigDecimal> cashNumberOfGroupTrees = new HashMap<Integer, BigDecimal>();

	private static BigDecimal numberOfPosibleTree(int n) {
		if (n == 0 || n == 1 || n == 2) {
			return BigDecimal.ONE;
		}
		if (cashNumberOfPosibleTree.containsKey(n)) {
			return cashNumberOfPosibleTree.get(n);
		}
		BigDecimal count = BigDecimal.ZERO;
		for (int i = 1; i < n; i++) {
			BigDecimal coeff = binomialCoeff(n - 1, i);
			BigDecimal groups = numberOfGroupTrees(n - 1, i);
			count = count.add(coeff.multiply(groups));
		}
		cashNumberOfPosibleTree.put(n, count);
		return count;
	}

	private static BigDecimal numberOfGroupTrees(int vertices, int trees) {
		if (vertices == trees) {
			return BigDecimal.ONE;
		}
		if (vertices < trees) {
			return BigDecimal.ZERO;
		}
		if (trees == 1) {
			return numberOfPosibleTree(vertices);
		}
		int cashKey = vertices * maxN + trees;
		if (cashNumberOfGroupTrees.containsKey(cashKey)) {
			return cashNumberOfGroupTrees.get(cashKey);
		}

		BigDecimal count = BigDecimal.ZERO;
		for (int i = 1; i <= vertices - trees + 1; i++) {
			BigDecimal posibleTrees = numberOfPosibleTree(i);
			BigDecimal coeff = BigDecimal.ONE;
			BigDecimal groupTrees = numberOfGroupTrees(vertices - i, trees - 1);
			count = count
					.add(posibleTrees.multiply(coeff).multiply(groupTrees));
		}
		cashNumberOfGroupTrees.put(cashKey, count);
		return count;
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

}
