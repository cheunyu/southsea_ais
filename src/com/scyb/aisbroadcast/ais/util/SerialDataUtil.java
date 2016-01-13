/**
 * @Title: SerialDataServiceImpl.java
 * @Package com.scyb.aisweather.serial.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com
 * @date 2014年9月4日 上午10:33:27
 * @version V1.0
 */
package com.scyb.aisbroadcast.ais.util;

import org.apache.log4j.Logger;

import com.scyb.aisbroadcast.common.util.AisAbkCache;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *      @ClassName: SerialDataServiceImpl   
 *  @Description: TODO(这里用一句话描述这个类的作用)     @author cheunyu xiaoyuuii@hotmail.com
 *  @date 2014年9月4日 上午10:33:27           
 */
public class SerialDataUtil {

    private Logger log = Logger.getLogger(this.getClass());
    private AscIIConvertBinaryUtil ascIIConvertBinaryUtil = new AscIIConvertBinaryUtil();
    private IEC1371ConvertSerialUtil iec1371ConvertSerialUtil = new IEC1371ConvertSerialUtil();

    /**
     *  (非 Javadoc)     
     * <p>
     * Title: gsenerationSerialCode
     * </p>
     *     
     * <p>
     * Description: 
     * </p>
     *     @param code     @see
     * com.scyb.aisweather.serial.service.ISerialDataService
     * #gsenerationSerialCode(java.lang.String)   
     */
    public List<String> generationABM(String iecCode, String mmsi) {
        List<String> serialCodeList = new ArrayList<String>();
        String serialCode = iec1371ConvertSerialUtil.convertSerial(iecCode);
        int messageCount = iec1371ConvertSerialUtil.getSerialMessageCount(serialCode);
        Map<Integer, String> messageMap = iec1371ConvertSerialUtil.getMessageMap(serialCode, messageCount);
        String paddingCode = iec1371ConvertSerialUtil.getPaddingCode(iecCode);
        StringBuffer sb = new StringBuffer();
        CheckCodeUtil checkUtil = new CheckCodeUtil();
        int inforId = AisAbkCache.getAbmInformationId();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] abmInformation = {sb.toString(), sdf.format(cal.getTime())};
        AisAbkCache.getAbmInformationIdMap().put(Integer.parseInt(mmsi), abmInformation);
        for (int i = 0; i < messageCount; ) {
            sb.delete(0, sb.length());
            sb.append("!ECABM,");
            // 语句总数
            sb.append(messageCount).append(",");
            // 语句编号
            sb.append(++i).append(",");
            // 连续信息识别符
            sb.append(inforId).append(",");
            if(i==1) {
                // 目标AIS设备MMSI码
                sb.append(mmsi);
                // 用于无线信息广播的AIS信道
                sb.append(",1").append(",");
                // 报文ID
                sb.append("06").append(",");
            }else {
                sb.append(",,,");
            }
            // 1371-4封装的数据
            sb.append(messageMap.get(i)).append(",");
            // 填充位
            if(i == messageCount) {
            	sb.append(paddingCode);
            }else {
            	sb.append("0");
            }
            sb.append("*");
            sb.append(checkUtil.chkSumXOR(sb.toString()));
            sb.append("\r\n");
            serialCodeList.add(sb.toString());
//            log.info(sb.toString());
        }
        return serialCodeList;
    }

    /**
     *  (非 Javadoc)     
     * <p>
     * Title: generationBBMEight
     * </p>
     *     
     * <p>
     * Description: 
     * </p>
     *     @param code  @param me  @return     @see
     * com.scyb.aisweather.serial.service
     * .ISerialDataService#generationBBMEight(java.lang.String,
     * com.scyb.aisweather.vdl.bo.head.MessageEight)   
     */
    public List<String> generationBBM(String iecCode) {
        List<String> serialCodeList = new ArrayList<String>();
        String serialCode = iec1371ConvertSerialUtil.convertSerial(iecCode);
        int messageCount = iec1371ConvertSerialUtil.getSerialMessageCount(serialCode);
        Map<Integer, String> messageMap = iec1371ConvertSerialUtil.getMessageMap(serialCode, messageCount);
        String paddingCode = iec1371ConvertSerialUtil.getPaddingCode(iecCode);
        StringBuffer sb = new StringBuffer();
        CheckCodeUtil checkUtil = new CheckCodeUtil();
        int inforId = AisAbkCache.getAbmInformationId();
        Map<Integer, String> informationMap = new HashMap<Integer, String>();
        informationMap.put(inforId, sb.toString());
        AisAbkCache.setAbmInformationIdMap(informationMap);
        for (int i = 0; i < messageCount; ) {
            sb.delete(0, sb.length());
            sb.append("!ECBBM,");
            // 语句总数
            sb.append(messageCount).append(",");
            // 语句编号
            sb.append(++i).append(",");
            // 连续信息识别符
            sb.append(inforId).append(",");
            if(i==1) {
                // 用于无线信息广播的AIS信道
                sb.append(",1").append(",");
                // 报文ID
                sb.append("08").append(",");
            }else {
                sb.append(",,,");
            }
            // 1371-4封装的数据
            sb.append(messageMap.get(i)).append(",");
            // 填充位
            sb.append(paddingCode).append("*");
            sb.append(checkUtil.chkSumXOR(sb.toString()));
            sb.append("\r\n");
//            log.info(sb.toString());
            serialCodeList.add(sb.toString());
        }
        return serialCodeList;
    }
}
