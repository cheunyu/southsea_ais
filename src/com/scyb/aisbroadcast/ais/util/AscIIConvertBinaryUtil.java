/**     
 * @Title: AscIIConvertBinary.java    
 * @Package com.scyb.aisweather.vdl.util   
 * @Description: TODO(用一句话描述该文件做什么)    
 * @author A18ccms A18ccms_gmail_com     
 * @date 2014年8月25日 下午2:33:00    
 * @version V1.0    
 */
package com.scyb.aisbroadcast.ais.util;

/**
 *      @ClassName: AscIIConvertBinary     @Description: TODO(ASCII码转换二进制工具)   
 *  @author cheunyu xiaoyuuii@hotmail.com  @date 2014年8月25日 下午2:33:00           
 */
public class AscIIConvertBinaryUtil {

	public String convertBin(int ascii, int bitValue) {
		char[] chs = new char[bitValue];
		for (int i = 0, k = chs.length; i < k; i++) {
			chs[k - i - 1] = ((ascii >> i) & 1) == 0 ? '0' : '1';
		}
		return new String(chs);
	}

	public static String asciiToString(String value) {
		StringBuffer sbu = new StringBuffer();
		String[] chars = value.split(",");
		for (int i = 0; i < chars.length; i++) {
			sbu.append((char) Integer.parseInt(chars[i]));
		}
		return sbu.toString();
	}

}
