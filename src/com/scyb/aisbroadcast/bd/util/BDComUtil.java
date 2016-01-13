package com.scyb.aisbroadcast.bd.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class BDComUtil {

	private static byte[] dataByteFinal;
	private static int dataInd = 0;
	private static Logger log = Logger.getLogger("BDComUtil");
	// 北斗电文长度
	private static int dataLength = -1;
	// 串口数据数组中北斗电文起始下标
	private static int dataBegin = -1;
	// 串口数据数组中北斗电文多余数据
	private static List<Byte> surplusList = new ArrayList<Byte>();
	private static byte[] dataByteTemp = null;
	private static List<Byte> dataByteTempList = null;
	private static StringBuffer serialSb = new StringBuffer();
	private static boolean firstData = true;
	// 电文有效标识
	private static boolean effectiveFlag = false;

	/**
	 * 补全二进制
	 *
	 * @param num
	 *            整型数据
	 * @param subIndex
	 *            转换二进制保留位数
	 */
	public static String toFullBinaryString(int num, int subIndex) {
		char[] chs = new char[subIndex];
		for (int i = 0, k = chs.length; i < k; i++) {
			chs[k - i - 1] = ((num >> i) & 1) == 0 ? '0' : '1';
		}
		return new String(chs);
	}

	/**
	 * 校验和
	 */
	public static int getVerif(byte[] b) {
		int k = 0;
		for (int i = 0; i < b.length; i++) {
			if (i == 0) {
				k = b[i] ^ b[i + 1];
			} else if (i > 0 && i < b.length - 1) {
				k = k ^ b[i + 1];
			}
		}
		return k;
	}

	/**
	 * 校验和
	 */
	public static int getVerif(int[] b) {
		int k = 0;
		for (int i = 0; i < b.length; i++) {
			if (i == 0) {
				k = b[i] ^ b[i + 1];
			} else if (i > 0 && i < b.length - 1) {
				k = k ^ b[i + 1];
			}
		}
		return k;
	}

	/**
	 * 过虑串口数据拼接完整多条数据内容 以一条信息为例，从指挥机底层信号进行分包，进入COM数据并一定是一次进入，也可能2-3次才能完整。
	 * 记录第一个包携带的电文长度数据做返回结果最终数组长度。
	 *
	 * @param dataByte
	 *            串口每次接收的原始字节数组
	 * @return dataByteFinal or null
	 *         如果一条电文没有一次接收完整返回null，直到多条数据拼接成一条完整电文返回电文字节数组
	 */
	public static byte[] filterByte(byte[] dataByte) {
		// 判断首字节是否是北斗电文起始标示符"$"
		if (new String(dataByte).indexOf("$") == 0) {
			// 获取电文长度
			dataLength = Integer.valueOf(
					BDComUtil.toFullBinaryString(dataByte[5], 8) + BDComUtil.toFullBinaryString(dataByte[6], 8), 2);
			// 初始化最终处理后的字节数组，长度不包含CRC和校验位两个BYTE
			dataByteFinal = new byte[dataLength];
			dataInd = 0;
		}
		/* 用于调试监控 */
		// log.info("本次电文总长度" + dataLength + "字节");
		// log.info("单包电文长度：" + dataByte.length);
		// log.info("分包计数下标：" + dataInd);
		// 数据拼包，每次接收的单包原始字节数组向最终拼接字节数组赋值
		for (int i = 0; i < dataByte.length; i++) {
			dataByteFinal[dataInd++] = dataByte[i];
		}
		/* 用于调试监控 */
		// log.info("当前最终数组最后一位下标值：" + dataByteFinal[dataByteFinal.length-1]);
		// 如果校验和为00H,表示一条电文还没有接收完整。
		if (dataByteFinal[dataByteFinal.length - 1] == 0) {
			log.info("单包接收" + dataByte.length + "字节.");
			log.info("没有完整接收电文内容，继续处理...");
			return null;
		} else {
			// 重置最终拼接数组下标
			dataInd = 0;
			// log.info("一条电文接收完成.");
			return dataByteFinal;
		}
	}

	/**
	 * 初始化一条串口数据用于解析北斗电文
	 */
	public boolean dataComplete(byte[] dataByte) {
		log.info("HEX:" + getHexAndString(dataByte));
		serialSb.append(getHexAndString(dataByte));
		// dataByteTemp = new byte[surplusList.size() + dataByte.length];
		dataByteTempList = new ArrayList<Byte>();
		// int dataByteTempIndex = 0;
		for (int k = 0; k < surplusList.size(); k++) {
			dataByteTempList.add(surplusList.get(k));
		}
		for (int m = 0; m < dataByte.length; m++) {
			dataByteTempList.add(dataByte[m]);
		}
		for (int i = 0; i < dataByteTempList.size(); i++) {
			// 取北斗电文起始标示符"$"位置
			if (dataByteTempList.get(i) == 0x24) {
				// 获取电文长度
				dataLength = Integer.valueOf(BDComUtil.toFullBinaryString(dataByteTempList.get(i + 5), 8)
						+ BDComUtil.toFullBinaryString(dataByteTempList.get(i + 6), 8), 2);
				// 电文头
				String msgHead = new String(new byte[] { dataByteTempList.get(i + 1), dataByteTempList.get(i + 2),
						dataByteTempList.get(i + 3), dataByteTempList.get(i + 4) });
				if ("GPST".equals(msgHead) || "GPSX".equals(msgHead)) {
					return false;
				}
				dataBegin = i;
				surplusList.clear();
				for (int j = dataBegin + dataLength; j < dataByteTempList.size(); j++) {
					surplusList.add(dataByteTempList.get(j));
				}
				break;
			}
		}
		log.info("电文起始下标:" + dataBegin);
		log.info("电文长度:" + dataLength);
		log.info("临时电文长度:" + dataByteTempList.size());
		log.info("电文剩余长度:" + surplusList.size());
		log.info(serialSb.toString());
		return true;
	}

	public byte[] dataByteFilter() {
		byte bdMsg[] = new byte[dataLength];
		int bdMsgIndex = 0;
		for (int i = dataBegin; i < dataBegin + dataLength; i++) {
			bdMsg[bdMsgIndex++] = dataByteTempList.get(i);
		}
		return bdMsg;
	}

	/**
	 * 处理串口数据，将串口数据复制到链表中
	 */
	public void dataProcess(byte[] dataByte) {
//		log.info("串口接收数据:" + getHexAndString(dataByte));
		// 判断串口第一次接收数据，由于可能不完整，抛掉该数据不进行处理
		if (firstData) {
			firstData = false;
			if (dataByte[0] != 0x24) {
				return;
			}
		}
		// 一条完整的北斗电文数组
		byte[] b = null;
		// 接收到的串口数据放入链表里
		for (int i = 0; i < dataByte.length; i++) {
			surplusList.add(dataByte[i]);
		}
	}

	/**
	 * 监测链表中是否集齐了一条完整的数据
	 */
	public boolean monitor() {
//		log.info("当前数据链表长度:" + surplusList.size());
		for (int j = 0; j < surplusList.size(); j++) {
			if (surplusList.get(j) == 0x24) {
				// 判断当前数据链表内是否包含电文长度字节
				if (j + 6 < surplusList.size()) {
					dataBegin = j;
					// 获取电文长度
					dataLength = Integer.valueOf(BDComUtil.toFullBinaryString(surplusList.get(j + 5), 8)
							+ BDComUtil.toFullBinaryString(surplusList.get(j + 6), 8), 2);
					if (dataLength > surplusList.size()) {
						// 数据链表不包含完整的电文
						return false;
					} else {
						return true;
					}
				} else {
					// 数据链表不包含完整的电文
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 过滤无效北斗电文
	 */
	public boolean filterData() {
		// 过滤电文标识
		boolean filterFlag = false;
		for (int j = dataBegin; j < surplusList.size(); j++) {
			if (surplusList.get(j) == 0x24) {
//				log.info(j);
//				log.info("j:" + j);
//				log.info("surplusList.size:" + surplusList.size());
				// 电文头检出
				String msgHead = new String(new byte[] { surplusList.get(j + 1), surplusList.get(j + 2),
						surplusList.get(j + 3), surplusList.get(j + 4) });
				if ("GPST".equals(msgHead) || "GPSX".equals(msgHead) || "BDST".equals(msgHead)|| "GLZK".equals(msgHead)) {
					filterFlag = true;
//					log.info("过滤电文:" + msgHead);
					break;
				} else {
					log.info("监测电文:" + msgHead);
					break;
				}
			}
		}
		if (filterFlag) {
//			log.info("开始删除数据链表");
//			log.info("删除数据起始位:" + dataBegin);
//			log.info("删除数据终止位:" + (dataBegin + dataLength));
			int m=0;
			for (int k = dataBegin; k < dataBegin + dataLength; k++) {
				surplusList.remove(dataBegin);
				m++;
			}
//			log.info("删除数据链表位:" + m);
		}
		return filterFlag;
	}

	public byte[] effectiveData() {
		byte[] b = new byte[dataLength];
		int n = 0;
		for (int m = dataBegin; m < dataBegin + dataLength; m++) {
			b[n++] = surplusList.get(m);
		}
		for (int k = dataBegin; k < dataBegin + dataLength; k++) {
			surplusList.remove(dataBegin);
		}
		return b;
	}

	/**
	 * @param dataByte
	 *            单包数据数组
	 * @return void 字符串
	 * @throws @Title:
	 *             getHexAndString
	 * @Description: TODO(输出单包字符串内容)
	 */
	public static String getHexAndString(byte[] dataByte) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < dataByte.length; i++) {
			if (Integer.toHexString(dataByte[i]).length() == 1) {
				sb.append("0").append(Integer.toHexString(dataByte[i]).toUpperCase() + " ");
			} else if (Integer.toHexString(dataByte[i]).length() == 8) {
				sb.append(Integer.toHexString(dataByte[i]).substring(6, 8).toUpperCase() + " ");
			} else {
				sb.append(Integer.toHexString(dataByte[i]).toUpperCase() + " ");
			}
		}
		// log.info(sb.toString());
		return sb.toString();
	}

	public static void main(String args[]) {
		int b[] = { 0x24, 0x54, 0x58, 0x58, 0x58, 0x00, 0x5F, 0x06, 0xF4, 0x53, 0xFF, 0x04, 0x5F, 0xE5, 0x00, 0x00,
				0x02, 0x58, 0xA4, 0x4D, 0x2C, 0x31, 0x2C, 0x31, 0x2C, 0x06, 0x9F, 0x6B, 0xC7, 0x2C, 0x0D, 0x3E, 0xD7,
				0x8E, 0x00 };
		BDComUtil t = new BDComUtil();
		byte b1[] = { 0x41, 0x49, 0x53 };
		System.out.println(t.getVerif(b));
	}
}