package com.google.foobar;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Answer6 {

	public static int answer(int[][] subway) {
		if (hasPath(subway)) {
			return -1;
		}
		for (int i = 0; i < subway.length; i++) {
			int[][] subwayNew = removeStation(subway, i);
			if (hasPath(subwayNew)) {
				return i;
			}
		}

		return -2;
	}

	private static boolean hasPath(int[][] subway){
		Set<Integer> stations = new HashSet<Integer>();
		for (int i = 0; i < subway.length; i++) {
			stations.add(i);
		}
		return hasPath(subway, stations, -1);
	}
	private static boolean hasPath(int[][] subway, Set<Integer> stations,
			int prevLine) {
		for (int line = 0; line < subway[0].length; line++) {
			if (line != prevLine) {
				Set<Integer> cycle = hasCycle(subway, stations, line);
				if (cycle!= null && cycle.size() > 0) {
					if (cycle.size() == 1) {
						return true;
					}
					if (stations.size() > cycle.size() && stations.containsAll(cycle)) {
						Set<Integer> frequent = mostFrequent(subway, stations,
								cycle, line);
						if (frequent.size() == 1) {
							return true;
						}
						if (hasPath(subway, cycle, line)
								|| hasPath(subway, frequent, line)) {
							return true;
						}
					} else {
						if (hasPath(subway, cycle, line)) {
							return true;
						}
					}
				}
			}

		}
		return false;
	}

	private static Set<Integer> mostFrequent(int[][] subway,
			Set<Integer> first, Set<Integer> second, int line) {
		Map<Integer, Integer> count = new HashMap<Integer, Integer>();
		int max = 0;
		for (int el : first) {
			int station = subway[el][line];
			if (count.containsKey(station)) {
				count.put(station, count.get(station) + 1);
			} else {
				count.put(station, 1);
			}
			if (count.get(station) > max) {
				max = count.get(station);
			}
		}
		Set<Integer> third = new HashSet<Integer>();
		for (int st : second) {
			if (count.get(st) == max) {
				third.add(st);
			}
		}
		return third;
	}

	private static Set<Integer> hasCycle(int[][] subway, Set<Integer> stations,
			int line) {
		Set<Integer> stSet = new HashSet<Integer>();
		for (int st : stations) {
			int i = 0;
			Set<Integer> tempSet = new HashSet<Integer>();
			int tmpSt = st;
			do {
				tmpSt = subway[tmpSt][line];
				tempSet.add(tmpSt);
				i++;
			} while (i < subway.length && !tempSet.contains(st));
			if (tempSet.contains(st)) {
				stSet.add(st);
			}
		}
		for (int st : stSet) {
			Set<Integer> checkSet = new HashSet<Integer>();
			int newSt = st;
			for (int i = 0; i < stSet.size(); i++) {
				newSt = subway[newSt][line];
				checkSet.add(newSt);
			}
			if (checkSet.size() < stSet.size()) {
				return null;
			}
		}
		return stSet;
	}

	private static int[][] removeStation(int[][] subway, int station) {
		int[][] subwayNew = new int[subway.length - 1][subway[0].length];
		int stNew = 0;
		for (int st = 0; st < subway.length; st++) {
			if (st != station) {
				for (int line = 0; line < subway[0].length; line++) {
					subwayNew[stNew][line] = newStationNumber(subway, line, st,
							station);
				}
				stNew++;
			}
		}
		for (int st = 0; st < subwayNew.length; st++) {
			for (int line = 0; line < subwayNew[0].length; line++) {
				if (subwayNew[st][line] > station) {
					subwayNew[st][line] = subwayNew[st][line] - 1;
				}
			}
		}
		return subwayNew;
	}

	private static int newStationNumber(int[][] subway, int line, int currSt,
			int exSt) {
		if (subway[currSt][line] != exSt) {
			return subway[currSt][line];
		} else {
			if (subway[exSt][line] == exSt) {
				return currSt;
			} else {
				return subway[exSt][line];
			}
		}
	}

	public static void main(String... args) {
		int[][] a = new int[][] { { 2, 1 }, { 2, 0 }, { 3, 1 }, { 1, 0 } };
		int[][] b = new int[][] { { 1, 2 }, { 1, 1 }, { 2, 2 } };
		int[][] c = new int[][] { { 1 }, { 0 } };
		int[][] d = new int[][] { { 1, 1 }, { 2, 2 }, { 0, 2 } };
		int[][] e = new int[][] { { 0, 1 }, { 1, 2 }, { 2, 0 } };
		int[][] f = new int[][] { { 1, 2 }, { 2, 2 }, { 3, 3 }, { 0, 0 } };
		int[][] g = new int[][] { { 1, 2 }, { 2, 2 }, { 3, 3 }, { 4, 4 },
				{ 5, 5 }, { 6, 6, }, { 7, 7 }, { 8, 8 }, { 9, 9 }, { 10, 10 },
				{ 11, 11 }, { 12, 12 }, { 13, 13 }, { 14, 14 }, { 15, 15 },
				{ 16, 16 }, { 17, 17 }, { 18, 18 }, { 19, 19 }, { 20, 20 },
				{ 21, 21 }, { 22, 22 }, { 23, 23 }, { 24, 24 }, { 25, 25 },
				{ 0, 0 } };
		System.out.println("-1  " + answer(a));
		System.out.println(" 1   " + answer(b));
		System.out.println(" 0   " + answer(c));
		System.out.println("-1  " + answer(d));
		System.out.println("-2  " + answer(e));
		System.out.println("-1  " + answer(f));
		System.out.println("-1  " + answer(g));
	}

}
