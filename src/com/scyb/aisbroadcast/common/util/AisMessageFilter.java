package com.scyb.aisbroadcast.common.util;

import java.util.Iterator;
import java.util.Map.Entry;

import com.scyb.aisbroadcast.ais.util.NMEA0183ConvertBinaryUtil;
import com.scyb.aisbroadcast.common.bo.SystemConfig;

public class AisMessageFilter {

	private NMEA0183ConvertBinaryUtil ncbUtil = new NMEA0183ConvertBinaryUtil();
	
	public void message7Or13(String serialData) {
		String nmea0183Data = ncbUtil.convert(serialData);
		Iterator<Entry<Integer, String>> iter = AisAbkCache.getAbmInformationIdMap().entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			if (entry.getKey().equals(Integer.valueOf(nmea0183Data.substring(8, 38), 2))) {
				AisAbkCache.getAbmInformationIdMap().remove(entry.getKey());
			}
		}
	}
	
	public static void main(String args[]) {
//		AisMessageFilter t = new AisMessageFilter();
//		t.message7Or13("7>eq`d@01pT0");
		NMEA0183ConvertBinaryUtil ncbUtil = new NMEA0183ConvertBinaryUtil();
		System.out.println(ncbUtil.convert("7>eq`d@01pT1"));
	}
}
