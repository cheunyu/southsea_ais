package com.scyb.aisbroadcast.ais.util;

import java.util.ArrayList;
import java.util.List;

public class BroadcastConfig {

	private static int gfTime;
	private static int nfTime;
	private static List<String> locationList = new ArrayList<String>();
	public static int getGfTime() {
		return gfTime;
	}
	public static void setGfTime(int gfTime) {
		BroadcastConfig.gfTime = gfTime;
	}
	public static int getNfTime() {
		return nfTime;
	}
	public static void setNfTime(int nfTime) {
		BroadcastConfig.nfTime = nfTime;
	}
	public static List<String> getLocationList() {
		return locationList;
	}
	public static void setLocationList(List<String> locationList) {
		BroadcastConfig.locationList = locationList;
	}
	
}
