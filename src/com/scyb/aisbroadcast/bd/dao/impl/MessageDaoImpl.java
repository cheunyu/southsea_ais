package com.scyb.aisbroadcast.bd.dao.impl;

import java.util.UUID;

import org.apache.log4j.Logger;

import com.scyb.aisbroadcast.bd.bo.GeneralForecast;
import com.scyb.aisbroadcast.bd.bo.Message;
import com.scyb.aisbroadcast.bd.dao.IMessageDao;
import com.scyb.aisbroadcast.common.util.SqlHelper;

/**
 * Created with Intellij IDEA
 * User:foo
 * Date:2015/8/17
 * Time:10:48
 */
public class MessageDaoImpl implements IMessageDao {

    private Logger log = Logger.getLogger(this.getClass());
    private SqlHelper sqlHelper = new SqlHelper();
    
    @Override
    public void saveMessage(Message message) {
    	String sql = "insert into MESSAGE values (?,?,?,?,?,?)";
    	String[] parameters = {UUID.randomUUID().toString(), message.getMsgNo(), message.getContent(), message.getMsgHex(), message.getCreateTime(), message.getModel()};
    	sqlHelper.executeUpdate(sql, parameters);
    }
}
