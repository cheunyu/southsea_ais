package com.scyb.aisbroadcast.bd.service;


import com.scyb.aisbroadcast.bd.bo.NumericalForecast;

import java.util.List;

/**
 * Created with Intellij IDEA
 * User:foo
 * Date:2015/8/4
 * Time:10:41
 */
public interface INumericalForecastService {

    /**
     * 保存常规预报
     */
    public void addNumericalForcastService(NumericalForecast numericalForecast);
    

    /**
     * 删除常规预报
     */
    public void deleteNumericalForcastService();
    

    /**
     * 发送常规预报
     */
    public void sendNumericalForcastService(NumericalForecast numericalForecast, int model, int msgNumber);

    /**
     *  定时播发预报
     * */
    public void sendNumericalForecastPlanService();

}
