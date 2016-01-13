package com.scyb.aisbroadcast.bd.service;

import com.scyb.aisbroadcast.bd.bo.LocationInfo;

/**
 * Created with Intellij IDEA
 * User:foo
 * Date:2015/8/7
 * Time:10:35
 */
public interface ILocationInfoService {

    /**
     *  保存位置信息
     */
    public void addLocationInfoService(LocationInfo locationInfo);

    /**
     *  取位置信息
     * */
    public LocationInfo getLocationInfoService(int model);
}