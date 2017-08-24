package com.google.foobar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Answer3 {

	public static int answer(int[][] intervals) {
		List<Interval> list = new ArrayList<Interval>(intervals.length);
		for (int[] a : intervals) {
			list.add(new Interval(a[0], a[1]));
		}
		Collections.sort(list);
		int totalTime = 0;
		int start = 0;
		int end = 0;
		for (Interval interval : list) {
			if (end < interval.start) {
				totalTime += end - start;
				start = interval.start;
				end = interval.end;
			} else if (end < interval.end) {
				end = interval.end;
			}
		}
		totalTime += end - start;
		return totalTime;
	}

	private static class Interval implements Comparable<Interval> {
		int start;
		int end;

		public Interval(int start, int end) {
			this.start = start;
			this.end = end;
		}

		public int compareTo(Interval interval) {
			if (start < interval.start) {
				return -1;
			}
			if (start > interval.start) {
				return 1;
			}
			return 0;
		}
	}

	public static void main(String... args) {
		System.out.println(answer(new int[][] { { 1, 3 }, { 3, 6 } }));
		System.out.println(answer(new int[][] { { 10, 14 }, { 4, 18 },
				{ 19, 20 }, { 19, 20 }, { 13, 20 } }));
	}

}
