/**     
 * @Title: ZnConvertComputerUtil.java    
 * @Package com.scyb.aisweather.vdl.util   
 * @Description: TODO(用一句话描述该文件做什么)    
 * @author A18ccms A18ccms_gmail_com     
 * @date 2014年10月13日 下午4:14:19    
 * @version V1.0    
 */
package com.scyb.aisbroadcast.ais.util;

import java.io.UnsupportedEncodingException;

/**
 *      @ClassName: ZnConvertComputerUtil     @Description: TODO(中文转机内码)   
 *  @author cheunyu xiaoyuuii@hotmail.com  @date 2014年10月13日 下午4:14:19      
 *     
 */
public class CharacterConvertAisCodeUtil {

	AscIIConvertBinaryUtil asbUtil = new AscIIConvertBinaryUtil();

	/**
	 *  @Title: getCc     @Description: TODO(ASCII、汉字转AIS收发码方法)   
	 *  @param @param inputData  @param @return    设定文件  
	 *  @return String    返回类型    @throws   
	 */
	// public String convertAisCode(String inputData) {
	// StringBuffer sendCode = new StringBuffer();
	// byte b[];
	// for (int k = 0; k < inputData.length(); k++) { // 循环字符串每个字符
	// // 汉字字节数标识符
	// int zhCount = 0;
	// try {
	// b = inputData.substring(k, k + 1).getBytes("gbk");
	// for (int i = 0; i < b.length; i++) {
	// // System.out.println(b[i]);
	// System.out.println(asbUtil.convertBin(b[i], 8));
	// if ((b[i] & 0x80) < 0x80) {
	// sendCode.append(asbUtil.convertBin(this.standardAsciiConvert(b[i]), 7));
	// } else {
	// zhCount++;
	// if (zhCount % 2 == 0) {
	// sendCode.append(asbUtil.convertBin(b[i] - 0x80, 7));
	// zhCount = 0;
	// } else {
	// sendCode.append(asbUtil.convertBin(1, 1) + asbUtil.convertBin(b[i] -
	// 0x80, 6));
	// }
	// }
	// }
	// } catch (UnsupportedEncodingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// return sendCode.toString();
	// }

	/**
	 *  @Title: standardAsciiConvert     @Description: TODO(标准ASCII码转7位收发码)   
	 *  @param @param asciiByte  @param @return    设定文件    @return int    返回类型  
	 *  @throws   
	 */
	public int standardAsciiConvert(Byte asciiByte) {
		if (asciiByte < 0x40) {
			return asciiByte;
		} else {
			return asciiByte - 0x40;
		}
	}

	/**
	 *  @Title: convertAisCode     @Description: TODO(ASCII、汉字转AIS收发码方法)   
	 *  @param @param inputData  @param @return    设定文件  
	 *  @return String    返回类型    @throws   
	 */
	public String convertAisCode(String inputData) {
		StringBuffer sendCode = new StringBuffer();
		byte b[];
		for (int k = 0; k < inputData.length(); k++) { // 循环字符串每个字符
			try {
				b = inputData.substring(k, k + 1).getBytes("gbk");
				for (int i = 0; i < b.length; i++) {
					// System.out.println(b[i]);
					if ((b[i] & 0x80) < 0x80) {
						sendCode.append(asbUtil.convertBin(this.standardAsciiConvert(b[i]), 7));
					} else {
						b[i] = (byte) (b[i] - 0x80);
						b[i + 1] = (byte) (b[i + 1] - 0x80);
						int aa = 0;
						int bb = 0;
						if (b[i] < 0x40) {
							bb = b[i + 1] / 0x20;
							aa = (b[i] - 0x30) * 4 + bb;
							bb = b[i + 1] % 0x20;
						} else {
							aa = b[i] - 0x40;
							bb = b[i + 1];
						}
						i++;
						sendCode.append(
								asbUtil.convertBin(1, 1) + asbUtil.convertBin(aa, 6) + asbUtil.convertBin(bb, 7));
					}
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sendCode.toString();
	}

	/**
	 *  @Title: convertAsciiCode     @Description: TODO(AIS收发码转ASCII、汉字方法)   
	 *  @param @param inputData  @param @return    设定文件  
	 *  @return String    返回类型    @throws   
	 */
	public String convertAsciiCode(String inputData) {
		int charCount = inputData.length() / 6;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < charCount; i++) {
			int sixBinaryValue = Integer.parseInt(inputData.substring(i * 6, (i + 1) * 6), 2);
			if (sixBinaryValue == 0) {
				continue;
			}
			if (sixBinaryValue < 0x20) {
				sb.append(((char) Integer.parseInt("01" + inputData.substring(i * 6, (i + 1) * 6), 2)));
			} else {
				sb.append(((char) Integer.parseInt("00" + inputData.substring(i * 6, (i + 1) * 6), 2)));
			}
		}
		return sb.toString();

	}

	// 二进制转字符串
	public static String byte2hex(byte[] b) {
		StringBuffer sb = new StringBuffer();
		String tmp = "";
		for (int i = 0; i < b.length; i++) {
			tmp = Integer.toHexString(b[i] & 0XFF);
			if (tmp.length() == 1) {
				sb.append("0" + tmp);
			} else {
				sb.append(tmp);
			}

		}
		return sb.toString();
	}

	public static void main(String args[]) {
		CharacterConvertAisCodeUtil z = new CharacterConvertAisCodeUtil();
		System.out.println(z.standardAsciiConvert("N".getBytes()[0]));
		// System.out.println(z.convertAsciiCode("001100001001000001001110"));
		// System.out.println(z.convertAisCode("P"));
		// char c = 'L';
		// System.out.println((int)c);
	}

}
