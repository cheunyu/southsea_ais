package com.scyb.aisbroadcast.bd.bo;

import java.sql.Timestamp;

/**
 * Created with Intellij IDEA User:foo Date:2015/8/17 Time:10:45
 */
public class Message {
	private String guid;
	private String msgNo;
	private String content;
	private String msgHex;
	private String createTime;
	private String model;

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMsgHex() {
		return msgHex;
	}

	public void setMsgHex(String msgHex) {
		this.msgHex = msgHex;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Override
	public String toString() {
		return "Message [guid=" + guid + ", msgNo=" + msgNo + ", content=" + content + ", msgHex=" + msgHex
				+ ", createTime=" + createTime + ", model=" + model + "]";
	}

}
