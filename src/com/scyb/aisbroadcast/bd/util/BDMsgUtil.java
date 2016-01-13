package com.scyb.aisbroadcast.bd.util;

import org.apache.log4j.Logger;

/**
 * Created with Intellij IDEA
 * User:foo
 * Date:2015/7/27
 * Time:14:33
 */
public class BDMsgUtil {

    private static byte dataByte[];
    private static byte txxxByte[];
    private static Logger log = Logger.getLogger("BeiDouUtil");


    /**
     * IC卡检测指令,
     * */
    public static byte[] getICJC() {
        dataByte = new byte[12];
        // 指令,$ICJC
        dataByte[0] = 36;
        dataByte[1] = 73;
        dataByte[2] = 67;
        dataByte[3] = 74;
        dataByte[4] = 67;
        // 长度
        dataByte[5] = 0;
        dataByte[6] = 12;
        // 用户地址
        dataByte[7] = (byte) (int) Integer.valueOf("00000110", 2);
        dataByte[8] = (byte) (int) Integer.valueOf("11110100", 2);
        dataByte[9] = (byte) (int) Integer.valueOf("01010011", 2);
        // 信息内容,帧号
        dataByte[10] = (byte) (int) Integer.valueOf("00000000", 2);
        // 校验和
        dataByte[11] = (byte) BDComUtil.getVerif(dataByte);

        return dataByte;
    }

    /**
     * IC信息指令
     * */
    public static void getICXX(byte[] dataByte) {
        StringBuffer binarySb = new StringBuffer();
        for (int i = 0; i < dataByte.length; i++) {
            if (dataByte[i] < 0) {
                binarySb.append(Integer.toBinaryString(dataByte[i]).substring(Integer.toBinaryString(dataByte[i]).length() - 8, Integer.toBinaryString(dataByte[i]).length())).append("-");
            } else {
                binarySb.append(BDComUtil.toFullBinaryString(dataByte[i], 8)).append("-");
            }
        }
        log.info(binarySb.toString());
    }

    /**
     * 通信申请 $TXSQ
     *
     * @param content
     *            电文内容
     * @param cardId
     *            SIM卡ID
     * */
    public static byte[] getTXSQ(String content, int cardId) {
        int byteLength = 19 + content.getBytes().length;
        dataByte = new byte[byteLength];
        // 指令$TXSQ
        dataByte[0] = (byte) (int) Integer.valueOf("00100100", 2);
        dataByte[1] = (byte) (int) Integer.valueOf("01010100", 2);
        dataByte[2] = (byte) (int) Integer.valueOf("01011000", 2);
        dataByte[3] = (byte) (int) Integer.valueOf("01010011", 2);
        dataByte[4] = (byte) (int) Integer.valueOf("01010001", 2);

        // 长度
        String distressDataLengthString = BDComUtil.toFullBinaryString(byteLength, 16);
        dataByte[5] = (byte) (int) Integer.valueOf(distressDataLengthString.substring(0, 7), 2);
        dataByte[6] = (byte) (int) Integer.valueOf(distressDataLengthString.substring(8, 16), 2);
        // 受控用户地址,发出电文指挥机ID:455763
        dataByte[7] = (byte) (int) Integer.valueOf("00000011", 2);
        dataByte[8] = (byte) (int) Integer.valueOf("00000001", 2);
        dataByte[9] = (byte) (int) Integer.valueOf("00011011", 2);
        // 信息类别,混合模式
        dataByte[10] = (byte) (int) Integer.valueOf("01000110", 2);
        // 用户地址,接收电文设备卡ID
        String cardIdString = BDComUtil.toFullBinaryString(cardId, 24);
        dataByte[11] = (byte) (int) Integer.valueOf(cardIdString.substring(0, 8), 2);
        dataByte[12] = (byte) (int) Integer.valueOf(cardIdString.substring(8, 16), 2);
        dataByte[13] = (byte) (int) Integer.valueOf(cardIdString.substring(16, 24), 2);
        // 电文内容长度
        byte[] contentByte = content.getBytes();

        String contentLength = BDComUtil.toFullBinaryString((contentByte.length + 3) * 8, 16);
        dataByte[14] = (byte) (int) Integer.valueOf(contentLength.substring(0, 8), 2);
        dataByte[15] = (byte) (int) Integer.valueOf(contentLength.substring(8, 16), 2);
        // 是否应答,写死
        dataByte[16] = (byte) (int) Integer.valueOf("00000000", 2);
        /**
         * 电文内容 内容第一位为电文内容类型标示为,A4H-混合型,写死 内容数据从输入流赋值
         * */
        dataByte[17] = (byte) (int) Integer.valueOf("10100100", 2);
        for (int i = 0; i < contentByte.length; i++) {
            dataByte[18 + i] = contentByte[i];
        }
        dataByte[dataByte.length - 1] = (byte) BDComUtil.getVerif(dataByte);
        log.info("输出语句:" + BDComUtil.getHexAndString(dataByte));
//        for(int i=0;i<dataByte.length;i++) {
//            System.out.print(Integer.toHexString(dataByte[i])+" ");
//        }
        return dataByte;
    }

    /**
     * 定位申请 $DWSQ
     *
     * @param cardId
     *            SIM卡ID
     * */
    public static byte[] getDWSQ(int cardId) {
        dataByte = new byte[22];
        // 指令,$DWSQ
        dataByte[0] = 36;
        dataByte[1] = 68;
        dataByte[2] = 87;
        dataByte[3] = 83;
        dataByte[4] = 81;
        // 长度
        dataByte[5] = 0;
        dataByte[6] = 22;
        // 用户地址,接收定位电文设备卡ID
        String cardIdString = BDComUtil.toFullBinaryString(cardId, 24);
        dataByte[7] = (byte) (int) Integer.valueOf(cardIdString.substring(0, 8), 2);
        dataByte[8] = (byte) (int) Integer.valueOf(cardIdString.substring(8, 16), 2);
        dataByte[9] = (byte) (int) Integer.valueOf(cardIdString.substring(16, 24), 2);
        // 信息类别
        dataByte[10] = 4;
        dataByte[11] = 0;
        dataByte[12] = 0;
        dataByte[13] = 0;
        dataByte[14] = 0;
        dataByte[15] = 0;
        dataByte[16] = 0;
        dataByte[17] = 0;
        dataByte[18] = 0;
        dataByte[19] = 0;
        dataByte[20] = 0;
        // 校验和
        dataByte[21] = (byte) BDComUtil.getVerif(dataByte);
        return dataByte;
    }

    /**
     * 点名定位(通信申请扩展)
     *
     * @param cardId
     *            定位目标卡号
     * */
    public static byte[] getDMDW(int cardId) {
        dataByte = new byte[23];
        // 指令$TXSQ
        dataByte[0] = (byte) (int) Integer.valueOf("00100100", 2);
        dataByte[1] = (byte) (int) Integer.valueOf("01010100", 2);
        dataByte[2] = (byte) (int) Integer.valueOf("01011000", 2);
        dataByte[3] = (byte) (int) Integer.valueOf("01010011", 2);
        dataByte[4] = (byte) (int) Integer.valueOf("01010001", 2);
        // 长度
        dataByte[5] = 0;
        dataByte[6] = 23;
        // 受控用户地址,发出电文指挥机ID:455763
        dataByte[7] = (byte) (int) Integer.valueOf("00000110", 2);
        dataByte[8] = (byte) (int) Integer.valueOf("11110100", 2);
        dataByte[9] = (byte) (int) Integer.valueOf("01010011", 2);
        // 消息类别
        dataByte[10] = 0x46;
        // 用户地址,接收定位电文设备卡ID
        String cardIdString = BDComUtil.toFullBinaryString(cardId, 24);
        dataByte[11] = (byte) (int) Integer.valueOf(cardIdString.substring(0, 8), 2);
        dataByte[12] = (byte) (int) Integer.valueOf(cardIdString.substring(8, 16), 2);
        dataByte[13] = (byte) (int) Integer.valueOf(cardIdString.substring(16, 24), 2);
        // 常量
        dataByte[14] = 0;
        dataByte[15] = 0x28;
        dataByte[16] = 0;
        dataByte[17] = (byte) 0xA2;

        dataByte[18] = (byte) ((cardId * 8) >> 24);
        dataByte[19] = (byte) ((cardId * 8) >> 16);
        dataByte[20] = (byte) ((cardId * 8) >> 8);
        dataByte[21] = (byte) (cardId * 8);
        // 校验和
        dataByte[22] = (byte) BDComUtil.getVerif(dataByte);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < dataByte.length; i++) {
            sb.append(Integer.toHexString(dataByte[i])).append(" ");
        }
        return dataByte;
    }
    
    /**
     *  获取电文序号
     * */
    public int getMsgNo(byte[] dataByte) {
    	String dataStr = new String(dataByte);
		String data[] = dataStr.substring(18, dataStr.length() - 1).split(",");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < data[1].length(); i++) {
			sb.append(data[1].substring(i, i + 1));
		}
		return Integer.parseInt(sb.toString());
    }

    /**
     * 电文16进制转10进制
     * */
    public int convHexToDec(String data) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < data.length(); i++) {
			sb.append(data.substring(i, i + 1));
		}
		return Integer.parseInt(sb.toString());
    }
    
}
