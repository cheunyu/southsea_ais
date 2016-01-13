package com.scyb.aisbroadcast.ui;

import com.scyb.aisbroadcast.bd.util.BDComUtil;
import com.scyb.aisbroadcast.common.util.BdSerialController;

public class SendTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		byte[] bdMsg = new byte[] { (byte) (int) Integer.valueOf("10100100", 2), 0x4E, 0x5A, 0x2C, 0x33, 0x2C, 0x30,
				0x2C, 0x31, 0x38, 0x2C, 0x31, 0x2C, 0x31, 0x2C, 0x31, 0x2C, 0x56, 0x7B,
				(byte) (int) Integer.valueOf("11011110", 2), 0x40, 0x2C, 0x33, 0x2E, 0x39, 0x2C, 0x2D, 0x31, 0x32, 0x36,
				0x2C, 0x2C, 0x2C, 0x30, 0x2E, 0x31, 0x2C, 0x2C, 0x33, 0x2E, 0x39, 0x2C, 0x2D, 0x31, 0x32, 0x36, 0x2C,
				0x2C, 0x2C, 0x30, 0x2E, 0x31, 0x2C, 0x2C, 0x33, 0x2E, 0x39, 0x2C, 0x2D, 0x31, 0x32, 0x36, 0x2C, 0x2C,
				0x2C, 0x30, 0x2E, 0x31, 0x2C, 0x2C, 0x33, 0x2E, 0x39, 0x2C, 0x2D, 0x31, 0x32, 0x36, 0x2C, 0x2C, 0x2C,
				0x30, 0x2E, 0x31, 0x2C, 0x2C, 0x33, 0x2E, 0x39, 0x2C, 0x2D, 0x31, 0x32, 0x36, 0x2C, 0x2C, 0x2C, 0x30,
				0x2E, 0x31, 0x2C, 0x2C };

		byte[] dataByte = new byte[bdMsg.length + 18];
		// 指令$TXSQ
		dataByte[0] = (byte) (int) Integer.valueOf("00100100", 2);
		dataByte[1] = (byte) (int) Integer.valueOf("01010100", 2);
		dataByte[2] = (byte) (int) Integer.valueOf("01011000", 2);
		dataByte[3] = (byte) (int) Integer.valueOf("01010011", 2);
		dataByte[4] = (byte) (int) Integer.valueOf("01010001", 2);

		// 长度
		String distressDataLengthString = BDComUtil.toFullBinaryString(bdMsg.length + 18, 16);
		dataByte[5] = (byte) (int) Integer.valueOf(distressDataLengthString.substring(0, 7), 2);
		dataByte[6] = (byte) (int) Integer.valueOf(distressDataLengthString.substring(8, 16), 2);
		// 受控用户地址,发出电文指挥机ID:455763
		dataByte[7] = (byte) (int) Integer.valueOf("00000011", 2);
		dataByte[8] = (byte) (int) Integer.valueOf("00000001", 2);
		dataByte[9] = (byte) (int) Integer.valueOf("00011011", 2);
		// 信息类别,混合模式
		dataByte[10] = (byte) (int) Integer.valueOf("01000110", 2);
		// 用户地址,接收电文设备卡ID
		String cardIdString = BDComUtil.toFullBinaryString(196889, 24);
		dataByte[11] = (byte) (int) Integer.valueOf(cardIdString.substring(0, 8), 2);
		dataByte[12] = (byte) (int) Integer.valueOf(cardIdString.substring(8, 16), 2);
		dataByte[13] = (byte) (int) Integer.valueOf(cardIdString.substring(16, 24), 2);
		String contentLength = BDComUtil.toFullBinaryString((bdMsg.length + 3) * 8, 16);
		dataByte[14] = (byte) (int) Integer.valueOf(contentLength.substring(0, 8), 2);
		dataByte[15] = (byte) (int) Integer.valueOf(contentLength.substring(8, 16), 2);
		// 是否应答,写死
		dataByte[16] = (byte) (int) Integer.valueOf("00000000", 2);
		int k = 0;
		for (int i = 17; i < dataByte.length - 1; i++) {
			dataByte[i] = bdMsg[k];
			k++;
		}
		dataByte[dataByte.length - 1] = (byte) BDComUtil.getVerif(dataByte);
		BDComUtil bd = new BDComUtil();
		System.out.println(bd.getHexAndString(dataByte));

		byte[] b = new byte[] { 0x24, 0x54, 0x58, 0x53, 0x51, 0x00, 0x48, 0x03, 0x01, 0x19, 0x46, 0x03, 0x01, 0x19,
				0x01, (byte) (int) Integer.valueOf("10110000", 2), 0x00, (byte) (int) Integer.valueOf("10100100", 2),
				0x4E, 0x5A, 0x2C, 0x35, 0x2C, 0x30, 0x2C, 0x31, 0x38, 0x2C, 0x32, 0x2C, 0x33, 0x2C, 0x32, 0x2C, 0x56,
				0x7D, (byte) (int) Integer.valueOf("10000100", 2), 0x20, 0x2C, 0x2C, 0x38, 0x2E, 0x34, 0x2C, 0x2D, 0x31,
				0x34, 0x34, 0x2C, 0x2C, 0x2C, 0x31, 0x2E, 0x39, 0x2C, 0x2C, 0x38, 0x2E, 0x34, 0x2C, 0x2D, 0x31, 0x34, 0x34,
				0x2C, 0x2C, 0x2C, 0x31, 0x2E, 0x39, 0x2C};
		byte[] bb = new byte[1];
		bb[0] = (byte) BDComUtil.getVerif(b);
		for(int m=0;m<bb.length;m++) {
			System.out.println(bb[m]);
		}
		System.out.println(Integer.toHexString(BDComUtil.getVerif(b)));
	}

}
