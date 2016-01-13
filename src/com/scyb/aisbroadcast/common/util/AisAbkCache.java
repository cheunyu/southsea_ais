package com.scyb.aisbroadcast.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AisAbkCache {

	private static int abmInformationId = -1;
	private static int bbmInformationId = -1;
	private static Map<Integer, String[]> abmInformationIdMap = new HashMap<Integer, String[]>();
	private static Map<Integer, String[]> bbmInformationIdMap = new HashMap<Integer, String[]>();
	public static int getAbmInformationId() {
		if(abmInformationId==3) {
			abmInformationId = -1;
		}
		return ++abmInformationId;
	}
	public static void setAbmInformationId(int abmInformationId) {
		AisAbkCache.abmInformationId = abmInformationId;
	}
	public static int getBbmInformationId() {
		if(bbmInformationId==3) {
			bbmInformationId = -1;
		}
		return ++bbmInformationId;
	}
	public static void setBbmInformationId(int bbmInformationId) {
		AisAbkCache.bbmInformationId = bbmInformationId;
	}
	public static Map getAbmInformationIdMap() {
		return abmInformationIdMap;
	}
	public static void setAbmInformationIdMap(Map abmInformationIdMap) {
		AisAbkCache.abmInformationIdMap = abmInformationIdMap;
	}
	public static Map getBbmInformationIdMap() {
		return bbmInformationIdMap;
	}
	public static void setBbmInformationIdMap(Map bbmInformationIdMap) {
		AisAbkCache.bbmInformationIdMap = bbmInformationIdMap;
	}
	
	
}
