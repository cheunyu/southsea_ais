package com.scyb.aisbroadcast.bd.bo;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created with Intellij IDEA
 * User:foo
 * Date:2015/8/6
 * Time:9:12
 */
public class LocationInfo {
    private String guid;
    private String msgNo;
    private String model;
    private String latlon;
    private String hexMsg;
    private String createTime;
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getMsgNo() {
		return msgNo;
	}
	public void setMsgNo(String msgNo) {
		this.msgNo = msgNo;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getLatlon() {
		return latlon;
	}
	public void setLatlon(String latlon) {
		this.latlon = latlon;
	}
	public String getHexMsg() {
		return hexMsg;
	}
	public void setHexMsg(String hexMsg) {
		this.hexMsg = hexMsg;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "LocationInfo [guid=" + guid + ", msgNo=" + msgNo + ", model=" + model + ", latlon=" + latlon
				+ ", hexMsg=" + hexMsg + ", createTime=" + createTime + "]";
	}
    
}
