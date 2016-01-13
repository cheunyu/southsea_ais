package com.scyb.aisbroadcast.ui;

public class T {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "6,-154,,,1.6,,6,-154,,,1.6,,6,-154,,,1.6,";
		for (int i = 0; i < str.split(",").length; i++) {
			System.out.println("data: " + str.split(",")[i]);
		}
		System.out.println(str.split(",").length);
		int m = 0;
		for (int k = 0; k < str.length(); k++) {
			if (str.substring(k, k + 1).equals(",")) {
				System.out.println("str: " + k + " - " + str.substring(m, k));
				m = k + 1;
			}
		}
		if(str.substring(str.length()-1, str.length()).equals(",")) {
			System.out.println("str1: ");
		}
	}

}
