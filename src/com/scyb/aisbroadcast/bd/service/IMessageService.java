package com.scyb.aisbroadcast.bd.service;

import com.scyb.aisbroadcast.bd.bo.Message;
import com.scyb.aisbroadcast.bd.dao.IMessageDao;

/**
 * Created with Intellij IDEA
 * User:foo
 * Date:2015/8/17
 * Time:10:34
 */
public interface IMessageService {

    /**
     * 保存短消息
     */
    public void addMessageService(Message message);

    /**
     * 发送短消息
     */
    public void sendMessageService(Message message);
}
