package com.google.foobar;

public class Answer4 {

	public static int answer(int n) {
		int counter = 0;
		while (n > 0) {
			int num = (int) Math.floor(Math.sqrt(n));
			n -= num * num;
			counter++;
		}
		return counter;
	}

	public static void main(String... args) {
		System.out.println(answer(24));
		System.out.println(answer(160));
	}

}
