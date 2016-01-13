package com.scyb.aisbroadcast.bd.bo;

import java.sql.Timestamp;

/**
 * Created with Intellij IDEA
 * User:foo
 * Date:2015/8/7
 * Time:13:20
 */
public class NumericalForecast {
    private String guid;
    private String model;
    private String boradModel;
    private String forecastTime;
    private String windSpeedList;
    private String windDirectionList;
    private String waterSpeedList;
    private String waterDirectionList;
    private String waveHighList;
    private String waveDirectionList;
    private String bdMsg;
    private String createTime;
    private String msgNo;
    private String bdNo;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getForecastTime() {
        return forecastTime;
    }

    public void setForecastTime(String forecastTime) {
        this.forecastTime = forecastTime;
    }

    public String getWindSpeedList() {
        return windSpeedList;
    }

    public void setWindSpeedList(String windSpeedList) {
        this.windSpeedList = windSpeedList;
    }

    public String getWindDirectionList() {
        return windDirectionList;
    }

    public void setWindDirectionList(String windDirectionList) {
        this.windDirectionList = windDirectionList;
    }

    public String getWaterSpeedList() {
        return waterSpeedList;
    }

    public void setWaterSpeedList(String waterSpeedList) {
        this.waterSpeedList = waterSpeedList;
    }

    public String getWaterDirectionList() {
        return waterDirectionList;
    }

    public void setWaterDirectionList(String waterDirectionList) {
        this.waterDirectionList = waterDirectionList;
    }

    public String getWaveHighList() {
        return waveHighList;
    }

    public void setWaveHighList(String waveHighList) {
        this.waveHighList = waveHighList;
    }

    public String getWaveDirectionList() {
        return waveDirectionList;
    }

    public void setWaveDirectionList(String waveDirectionList) {
        this.waveDirectionList = waveDirectionList;
    }

    public String getBdMsg() {
        return bdMsg;
    }

    public void setBdMsg(String bdMsg) {
        this.bdMsg = bdMsg;
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

	public NumericalForecast() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getMsgNo() {
		return msgNo;
	}

	public void setMsgNo(String msgNo) {
		this.msgNo = msgNo;
	}

	public String getBoradModel() {
		return boradModel;
	}

	public void setBoradModel(String boradModel) {
		this.boradModel = boradModel;
	}

	public String getBdNo() {
		return bdNo;
	}

	public void setBdNo(String bdNo) {
		this.bdNo = bdNo;
	}

	@Override
	public String toString() {
		return "NumericalForecast [guid=" + guid + ", model=" + model + ", boradModel=" + boradModel + ", forecastTime="
				+ forecastTime + ", windSpeedList=" + windSpeedList + ", windDirectionList=" + windDirectionList
				+ ", waterSpeedList=" + waterSpeedList + ", waterDirectionList=" + waterDirectionList
				+ ", waveHighList=" + waveHighList + ", waveDirectionList=" + waveDirectionList + ", bdMsg=" + bdMsg
				+ ", createTime=" + createTime + ", msgNo=" + msgNo + ", bdNo=" + bdNo + "]";
	}




}
