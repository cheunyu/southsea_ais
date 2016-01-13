package com.scyb.aisbroadcast.bd.dao;

import com.scyb.aisbroadcast.bd.bo.LocationInfo;

/**
 * Created with Intellij IDEA
 * User:foo
 * Date:2015/8/7
 * Time:10:37
 */
public interface ILocationInfoDao{

    public void saveLocationInfo(LocationInfo locationInfo);

    public LocationInfo getLocationInfo(int model);
}
