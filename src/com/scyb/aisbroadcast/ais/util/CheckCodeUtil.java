/**     
 * @Title: CheckCodeUtil.java    
 * @Package com.scyb.aisweather.serial.util   
 * @Description: TODO(用一句话描述该文件做什么)    
 * @author A18ccms A18ccms_gmail_com     
 * @date 2014年9月9日 下午12:19:54    
 * @version V1.0    
 */
package com.scyb.aisbroadcast.ais.util;

/**
 *      @ClassName: CheckCodeUtil     @Description: TODO(异或校验和工具类)   
 *  @author cheunyu xiaoyuuii@hotmail.com  @date 2014年9月9日 下午12:19:54           
 */
public class CheckCodeUtil {

	public String chkSumXOR(String strMessage) {
		int tempXOR = 0;
		for (int i = 1; i < strMessage.getBytes().length - 1; i++) {
			if (i == 1) {
				tempXOR = strMessage.getBytes()[i] ^ strMessage.getBytes()[i + 1];
				// log.info(strMessage.charAt(i)+"与"+strMessage.charAt(i+1)+"异或值"
				// + tempXOR);
			} else if (i < strMessage.getBytes().length - 2) {
				tempXOR = tempXOR ^ strMessage.getBytes()[i + 1];
				// log.info(strMessage.charAt(i)+"与"+strMessage.charAt(i+1)+"异或值"
				// + tempXOR);
			}
		}
		if (tempXOR < 10) {
			return "0" + Integer.valueOf(tempXOR);
		} else if (tempXOR < 17) {
			return "0" + Integer.toHexString(tempXOR).toUpperCase();
		} else {
			return Integer.toHexString(tempXOR).toUpperCase();
		}
	}

	public static void main(String args[]) {
		String str = "!BAVDO,1,1,,A,4007R@0000Htt<tSF0l4Q@100@0V,0*";
		CheckCodeUtil cc = new CheckCodeUtil();
		System.out.println(cc.chkSumXOR(str));
	}
}
