package com.google.foobar;

public class Answer1 {

	public static int answer(int n) {
		for (int base = 2; base < 10; base++) {
			String str = Integer.toString(n, base);
			if (isPalindrome(str)) {
				return base;
			}
		}
		return 0;
	}

	private static boolean isPalindrome(String str) {
		int i = 0, j = str.length() - 1;
		while (i < j) {
			if (str.charAt(i) != str.charAt(j)) {
				return false;
			}
			i++;
			j--;
		}
		return true;
	}

	public static void main(String ...args) {
		System.out.println(answer(0));
		System.out.println(answer(42));
	}

}
