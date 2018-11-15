package com.bosch.smartcity.samsungtvlib.task;

import com.bosch.smartcity.samsungtvlib.Service;
import com.bosch.smartcity.samsungtvlib.TVConfiguration;
import com.bosch.smartcity.samsungtvlib.Utils;

public class WakeupOnLanTask extends TVTask{
	
	private TVConfiguration configuration;
	private Service tvService = new Service();

	public WakeupOnLanTask(ITaskEventListener listener, TVConfiguration configuration) {
		super(listener);
		this.configuration = configuration;
	}

	@Override
	protected void runTask() {
		tvService.WakeOnWirelessLan(configuration.wifiMacAddress);
	}

	@Override
	protected Object getTaskResult() {
		if(Utils.isHostReachable(configuration.ip)) {
			return true;
		}
		return null;
	}

}
