package com.scyb.aisbroadcast.bd.bo;

import java.sql.Timestamp;

/**
 * Created with Intellij IDEA
 * User:foo
 * Date:2015/7/29
 * Time:14:04
 */
public class GeneralForecast {
    private String guid;
    private String waveHigh;
    private String waterTemperature;
    private String tideHighTime;
    private String tideHigh;
    private String tideLowTime;
    private String tideLow;
    private String bdMsg;
    private String createTime;
    private String autoModel;
    private String broadModel;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getWaveHigh() {
        return waveHigh;
    }

    public void setWaveHigh(String waveHigh) {
        this.waveHigh = waveHigh;
    }

    public String getWaterTemperature() {
        return waterTemperature;
    }

    public void setWaterTemperature(String waterTemperature) {
        this.waterTemperature = waterTemperature;
    }

    public String getTideHighTime() {
        return tideHighTime;
    }

    public void setTideHighTime(String tideHighTime) {
        this.tideHighTime = tideHighTime;
    }

    public String getTideHigh() {
        return tideHigh;
    }

    public void setTideHigh(String tideHigh) {
        this.tideHigh = tideHigh;
    }

    public String getTideLowTime() {
        return tideLowTime;
    }

    public void setTideLowTime(String tideLowTime) {
        this.tideLowTime = tideLowTime;
    }

    public String getTideLow() {
        return tideLow;
    }

    public void setTideLow(String tideLow) {
        this.tideLow = tideLow;
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

	public String getAutoModel() {
		return autoModel;
	}

	public void setAutoModel(String autoModel) {
		this.autoModel = autoModel;
	}

	public String getBroadModel() {
		return broadModel;
	}

	public void setBroadModel(String broadModel) {
		this.broadModel = broadModel;
	}

	@Override
	public String toString() {
		return "GeneralForecast [guid=" + guid + ", waveHigh=" + waveHigh + ", waterTemperature=" + waterTemperature
				+ ", tideHighTime=" + tideHighTime + ", tideHigh=" + tideHigh + ", tideLowTime=" + tideLowTime
				+ ", tideLow=" + tideLow + ", bdMsg=" + bdMsg + ", createTime=" + createTime + ", autoModel="
				+ autoModel + ", broadModel=" + broadModel + "]";
	}


}
