package com.scyb.aisbroadcast.bd.service;


import com.scyb.aisbroadcast.bd.bo.GeneralForecast;
import com.scyb.aisbroadcast.bd.bo.LocationInfo;
import com.scyb.aisbroadcast.bd.bo.Message;
import com.scyb.aisbroadcast.bd.bo.NumericalForecast;

import java.text.NumberFormat;

/**
 * Created with Intellij IDEA
 * User:foo
 * Date:2015/8/4
 * Time:11:09
 */
public interface IAnalyzeComDataService {

    /**
     *  解析北斗电文常规预报
     * */
    public GeneralForecast analyzeBdGeneralForecast(byte[] bytes);

    /**
     *  解析北斗电文数值预报
     * */
    public NumericalForecast analyzeBdNumericalForecast(byte[] bytes);

    /**
     *  解析位置信息设置电文
     */
    public void analyzeBdLocationInfo(byte[] bytes);

    /**
     *  解析短信息电文
     * */
    public Message analyzeBdMessage(byte[] bytes);
    
}
