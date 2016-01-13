package com.scyb.aisbroadcast.bd.dao;

import com.scyb.aisbroadcast.bd.bo.GeneralForecast;

/**
 * Created with Intellij IDEA
 * User:foo
 * Date:2015/7/30
 * Time:16:25
 */
public interface IGeneralForecastDao {

    public void saveGeneralForecast(GeneralForecast gf);
    
    public void deleteGeneralForecast();
}
