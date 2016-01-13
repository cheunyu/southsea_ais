/**     
 * @Title: IEC1371ConvertSerialUtil.java    
 * @Package com.scyb.aisweather.vdl.util   
 * @Description: TODO(用一句话描述该文件做什么)    
 * @author A18ccms A18ccms_gmail_com     
 * @date 2014年9月22日 下午6:45:44    
 * @version V1.0    
 */
package com.scyb.aisbroadcast.ais.util;

import com.scyb.aisbroadcast.common.bo.SystemConfig;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *      @ClassName: IEC1371ConvertSerialUtil   
 *  @Description: TODO(IEC1371编码转换串口编码工具类)   
 *  @author cheunyu xiaoyuuii@hotmail.com  @date 2014年9月22日 下午6:45:44           
 */
public class IEC1371ConvertSerialUtil {

	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(this.getClass());
	private AscIIConvertBinaryUtil acbUtil = new AscIIConvertBinaryUtil();

	/**
	 *  @Title: convertSerial     @Description: TODO(对IEC1371补码后，八位二进制转六位二进制)   
	 *  @param @param iecCode  @param @return    设定文件  
	 *  @return String    返回类型    @throws   
	 */
	public String convertSerial(String iecCode) {
		@SuppressWarnings("unchecked")
		Map<String, String> sixbitMap = SystemConfig.getSixbitMap();
		StringBuffer serialCode = new StringBuffer();
		for (int i = 0; i < iecCode.length() % 6; i++) {
			iecCode = iecCode + "0";
		}
		for (int i = 0; i < iecCode.length() / 6; i++) {
			int tmp = Integer.valueOf(iecCode.substring(i * 6, (i + 1) * 6), 2);
			Iterator<Entry<String, String>> iter = sixbitMap.entrySet().iterator();
			while (iter.hasNext()) {
				@SuppressWarnings("rawtypes")
				Entry entry = (Entry) iter.next();
				if (entry.getValue().equals(acbUtil.convertBin(tmp, 6))) {
					serialCode.append(entry.getKey());
				}
			}
		}
		return serialCode.toString();
	}

	/**
	 *  @Title: getSerialMessageCount     @Description: TODO(计算报文总条数)   
	 *  @param @param iecCode  @param @return    设定文件    @return int    返回类型  
	 *  @throws   
	 */
	public int getSerialMessageCount(String serialCode) {
		if (serialCode.length() <= 48) {
			return 1;
		} else {
			if ((serialCode.length() - 48) % 60 == 0) {
				return (serialCode.length() - 48) / 60 + 1;
			} else {
				return (serialCode.length() - 48) / 60 + 2;
			}
		}
	}
	
	/**
	 *  @Title: getSerialMessageCount     @Description: TODO(计算报文总条数)   
	 *  @param @param iecCode  @param @return    设定文件    @return int    返回类型  
	 *  @throws   
	 */
	public int getNewSerialMessageCount(String binCode) {
		if(binCode.length()<=59) {
			return 1;
		}else if(binCode.length()<=283) {
			return 2;
		}else if(binCode.length()<=507) {
			return 3;
		}else if(binCode.length()<=731) {
			return 4;
		}else if(binCode.length()<=931) {
			return 5;
		}
		return 0;
	}

	/**
	 *  @Title: getMessageMap     @Description: TODO(按时隙分割报文)     @param @param
	 * serialCode  @param @param messageCount  @param @return    设定文件  
	 *  @return Map<Integer,String>    返回类型    @throws   
	 */
	public Map<Integer, String> getMessageMap(String serialCode, int messageCount) {
		Map<Integer, String> messageMap = new HashMap<Integer, String>();
		int temp = 0;
		for (int i = 0; i < messageCount; i++) {
			if (i == 0 && serialCode.length() < 48) {
				messageMap.put(1, serialCode.substring(0, serialCode.length()));
			} else if (i == 0 && serialCode.length() > 48) {
				messageMap.put(1, serialCode.substring(0, 48));
				temp = 48;
			} else if (serialCode.length() > temp + 60) {
				messageMap.put(i + 1, serialCode.substring(temp, temp + 60));
				temp = temp + 60;
			} else {
				messageMap.put(i + 1, serialCode.substring(temp, serialCode.length()));
			}

		}
		return messageMap;
	}
	
	/**
	 *  @Title: getMessageMap     @Description: TODO(按时隙分割报文)     @param @param
	 * serialCode  @param @param messageCount  @param @return    设定文件  
	 *  @return Map<Integer,String>    返回类型    @throws   
	 */
	public Map<Integer, String> getNewMessageMap(String binCode) {
		Map<Integer, String> messageMap = new HashMap<Integer, String>();
		NMEA0183ConvertBinaryUtil ncbUtil = new NMEA0183ConvertBinaryUtil();
		if(binCode.length()<=59) {
			messageMap.put(1, ncbUtil.convert0183Serial(binCode.substring(0, binCode.length())));
		}else if(binCode.length()<=283) {
			messageMap.put(1, ncbUtil.convert0183Serial(binCode.substring(0, 59)));
			messageMap.put(2, ncbUtil.convert0183Serial(binCode.substring(59, binCode.length())));
		}else if(binCode.length()<=507) {
			messageMap.put(1, ncbUtil.convert0183Serial(binCode.substring(0, 59)));
			messageMap.put(2, ncbUtil.convert0183Serial(binCode.substring(59, 283)));
			messageMap.put(3, ncbUtil.convert0183Serial(binCode.substring(283, binCode.length())));
		}else if(binCode.length()<=731) {
			messageMap.put(1, ncbUtil.convert0183Serial(binCode.substring(0, 59)));
			messageMap.put(2, ncbUtil.convert0183Serial(binCode.substring(59, 283)));
			messageMap.put(3, ncbUtil.convert0183Serial(binCode.substring(283, 507)));
			messageMap.put(4, ncbUtil.convert0183Serial(binCode.substring(507, binCode.length())));
		}else if(binCode.length()<=931) {
			messageMap.put(1, ncbUtil.convert0183Serial(binCode.substring(0, 59)));
			messageMap.put(2, ncbUtil.convert0183Serial(binCode.substring(59, 283)));
			messageMap.put(3, ncbUtil.convert0183Serial(binCode.substring(283, 507)));
			messageMap.put(4, ncbUtil.convert0183Serial(binCode.substring(507, 731)));
			messageMap.put(5, ncbUtil.convert0183Serial(binCode.substring(731, binCode.length())));
		}
		return messageMap;
	}
	
	/**
	 * @Title: getPaddingCode   
	 * @Description: TODO(六位二進制补位)   
	 * @param @param iecCode
	 * @param @return    设定文件  
	 * @return String    返回类型  
	 * @throws   
	 */
	public String getPaddingCode(String iecCode) {
		return Integer.toString(iecCode.length() % 6);
	}
	
	/**
	 * @Title: getPaddingCode   
	 * @Description: TODO(六位二進制补位)   
	 * @param @param iecCode
	 * @param @return    设定文件  
	 * @return String    返回类型  
	 * @throws   
	 */
	public String getNewPaddingCode(String binCode, int index) {
		int padding = 0;
		switch(index) {
		case 1:
			for (int i = 0; i < binCode.substring(0, binCode.length()).length() % 6;) {
				padding = ++i;
			}
			break;
		case 2:
			for (int i = 0; i < binCode.substring(59, binCode.length()).length() % 6;) {
				padding = ++i;
			}
			break;	
		case 3:
			for (int i = 0; i < binCode.substring(283, binCode.length()).length() % 6;) {
				padding = ++i;
			}
			break;
		case 4:
			for (int i = 0; i < binCode.substring(507, binCode.length()).length() % 6;) {
				padding = ++i;
			}
			break;
		case 5:
			for (int i = 0; i < binCode.substring(731, binCode.length()).length() % 6;) {
				padding = ++i;
			}
			break;
		}
		return Integer.toString(padding);
	}
	
	public String conver6bit(String inputData) {
		for(int i=0;i<inputData.length();i++) {
			System.out.println(inputData.substring(i, i+1));
			System.out.println();
		}
		return null;
	}
	
	public String conver1371Table44(String message) {
		AscIIConvertBinaryUtil acbUtil = new AscIIConvertBinaryUtil();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<message.length();i++) {
			//message.substring(i, i+1)
			byte[] b = message.substring(i, i+1).getBytes();
			sb.append(acbUtil.convertBin(b[0], 6));
		}
		StringBuffer dacfi = new StringBuffer("0000000001000000100000000001");
		return dacfi.append(sb).toString();
	}
	
	
	public static void main(String args[]) {
		IEC1371ConvertSerialUtil a = new IEC1371ConvertSerialUtil();
		System.out.println(a.conver1371Table44("N"));
	}
}
