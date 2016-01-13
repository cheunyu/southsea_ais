package com.scyb.aisbroadcast.bd.dao;

import com.scyb.aisbroadcast.bd.bo.Message;

/**
 * Created with Intellij IDEA
 * User:foo
 * Date:2015/8/17
 * Time:10:47
 */
public interface IMessageDao{

    public void saveMessage(Message message);
}
