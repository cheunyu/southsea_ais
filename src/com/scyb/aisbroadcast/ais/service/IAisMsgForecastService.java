package com.scyb.aisbroadcast.ais.service;


import com.scyb.aisbroadcast.bd.bo.GeneralForecast;
import com.scyb.aisbroadcast.bd.bo.NumericalForecast;

import java.util.List;

/**
 * Created with Intellij IDEA
 * User:foo
 * Date:2015/8/4
 * Time:10:52
 */
public interface IAisMsgForecastService {

    /**
     * 生成Ais常规预报消息内容
     * */
    public String generalForecastMsg(GeneralForecast generalForecast);

    /**
     * 生成Ais数值预报消息内容
     * */
    public List<String> numericalForecastMsg(NumericalForecast numericalForecast, int model);


}
