package com.scyb.aisbroadcast.common.bo;

import com.scyb.aisbroadcast.common.util.AisSerialController;
import com.scyb.aisbroadcast.common.util.BdSerialController;

public class SystemCache {

	private static AisSerialController aisSc;
	private static BdSerialController bdSc;

	public static AisSerialController getAisSc() {
		return aisSc;
	}

	public static void setAisSc(AisSerialController aisSc) {
		SystemCache.aisSc = aisSc;
	}

	public static BdSerialController getBdSc() {
		return bdSc;
	}

	public static void setBdSc(BdSerialController bdSc) {
		SystemCache.bdSc = bdSc;
	}

}
