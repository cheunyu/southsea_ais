/**     
 * @Title: NMEA0183ConvertBinary.java    
 * @Package com.scyb.aisweather.vdl.util   
 * @Description: TODO(用一句话描述该文件做什么)    
 * @author A18ccms A18ccms_gmail_com     
 * @date 2014年11月10日 上午11:09:43    
 * @version V1.0    
 */
package com.scyb.aisbroadcast.ais.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.scyb.aisbroadcast.common.bo.SystemConfig;

/**
 *      @ClassName: NMEA0183ConvertBinary   
 *  @Description: TODO(这里用一句话描述这个类的作用)     @author cheunyu xiaoyuuii@hotmail.com
 *  @date 2014年11月10日 上午11:09:43           
 */
public class NMEA0183ConvertBinaryUtil {

	private AscIIConvertBinaryUtil acbUtil = new AscIIConvertBinaryUtil();

	public String convert(String vdlMessage) {
		StringBuffer sb = new StringBuffer();
		byte b[];
		for (int i = 0; i < vdlMessage.length(); i++) {
			b = vdlMessage.substring(i, i + 1).getBytes();
			for (int j = 0; j < b.length; j++) {
				int tmp = b[j] + 0x28;
				if (tmp > 0x80) {
					tmp = tmp + 0x20;
				} else {
					tmp = tmp + 0x28;
				}
				sb.append(acbUtil.convertBin(tmp, 6));
			}
		}
		return sb.toString();
	}

	public String convert0183Serial(String iecCode) {
		@SuppressWarnings("unchecked")
		Map<String, String> sixbitMap = SystemConfig.getSixbitMap();
		StringBuffer serialCode = new StringBuffer();
		for (int i = 0; i < iecCode.length() % 6; i++) {
			iecCode = iecCode + "0";
		}
		for (int i = 0; i < iecCode.length() / 6; i++) {
			Iterator<Entry<String, String>> iter = sixbitMap.entrySet().iterator();
			while (iter.hasNext()) {
				@SuppressWarnings("rawtypes")
				Entry entry = (Entry) iter.next();
				if (entry.getValue().equals(iecCode.substring(i * 6, (i + 1) * 6))) {
					serialCode.append(entry.getKey());
				}
			}
		}
		return serialCode.toString();
	}
	
	public static void main(String args[]) {
		NMEA0183ConvertBinaryUtil a = new NMEA0183ConvertBinaryUtil();
		System.out.println(a.convert("N"));
	}
}
