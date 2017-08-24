package com.google.foobar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Answer5 {

	public static int[] answer(int[][] minions) {
		List<Minion> list = new ArrayList<Minion>(minions.length);
		for (int i = 0; i < minions.length; i++) {
			int time = minions[i][0];
			double p = (double) minions[i][1] / minions[i][2];
			list.add(new Minion(p / time, i));
		}
		Collections.sort(list);
		int[] res = new int[minions.length];
		int i = 0;
		for (Minion m : list) {
			res[i++] = m.index;
		}
		return res;
	}

	private static class Minion implements Comparable<Minion> {
		int index;
		double coef;

		public Minion(double coef, int index) {
			this.index = index;
			this.coef = coef;
		}

		public int compareTo(Minion minion) {
			if (coef > minion.coef) {
				return -1;
			}
			if (coef < minion.coef) {
				return 1;
			}
			return index - minion.index;
		}
	}

	private static void print(int[] a) {
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i] + " ");
		}
		System.out.println();
	}

	public static void main(String... args) {
		print(answer(new int[][] { { 5, 1, 5 }, { 10, 1, 2 } }));
		print(answer(new int[][] { { 390, 185, 624 }, { 686, 351, 947 },
				{ 276, 1023, 1024 }, { 199, 148, 250 } }));
	}
}
