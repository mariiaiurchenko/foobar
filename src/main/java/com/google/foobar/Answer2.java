package com.google.foobar;

import java.util.HashSet;
import java.util.Set;

public class Answer2 {

	public static int answer(String[] x) {
		Set<String> set = new HashSet<String>();
		int counter = 0;
		for (String str : x) {
			if (!set.contains(str)) {
				set.add(str);
				set.add(new StringBuilder(str).reverse().toString());
				counter++;
			}
		}
		return counter;
	}

	public static void main(String... args) {
		System.out.println(answer(new String[] {"foo", "bar", "oof", "bar"}));
		System.out.println(answer(new String[]{"x", "y", "xy", "yy", "", "yx"}));
	}

}
