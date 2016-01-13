package com.scyb.aisbroadcast.ais.util;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.scyb.aisbroadcast.common.util.AisAbkCache;

public class ResendTimer {

	private Timer timer;
	private Logger log = Logger.getLogger(this.getClass());

	public ResendTimer() {
		log.info("启动补发报文线程");
		timer = new Timer();
		timer.schedule(new ResendTask(), 1000, 1000 * 10 * 1);
	}

	public class ResendTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			this.resend();
		}

		public void resend() {
			@SuppressWarnings("unchecked")
			Iterator<Entry<Integer, String[]>> iter = AisAbkCache.getAbmInformationIdMap().entrySet().iterator();
			while (iter.hasNext()) {
				@SuppressWarnings("rawtypes")
				Entry entry = (Entry) iter.next();
				String[] tmp = (String[]) entry.getValue();
				log.info(tmp[1]);
				log.info("补发:" + entry.getKey());
			}
		}
	}
}
