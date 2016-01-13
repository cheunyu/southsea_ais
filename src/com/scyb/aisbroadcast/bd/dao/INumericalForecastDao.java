package com.scyb.aisbroadcast.bd.dao;

import com.scyb.aisbroadcast.bd.bo.NumericalForecast;

/**
 * Created with Intellij IDEA
 * User:foo
 * Date:2015/8/4
 * Time:10:42
 */
public interface INumericalForecastDao {

    public void saveNumericalForecast(NumericalForecast numericalForecast);
    
    public void deleteNumericalForecast();
}
