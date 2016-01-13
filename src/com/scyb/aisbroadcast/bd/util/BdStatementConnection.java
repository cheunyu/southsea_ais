package com.scyb.aisbroadcast.bd.util;

import java.util.ArrayList;
import java.util.List;

public class BdStatementConnection {

	private static List<String> mmsiList = new ArrayList<String>();

	public static List<String> getMmsiList() {
		return mmsiList;
	}

	public static void setMmsiList(List<String> mmsiList) {
		BdStatementConnection.mmsiList = mmsiList;
	}
	
	public static void addMmsiList(List<String> mmsiList) {
		BdStatementConnection.mmsiList.addAll(mmsiList);
	}
}