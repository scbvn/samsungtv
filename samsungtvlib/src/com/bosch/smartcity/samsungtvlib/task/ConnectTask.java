package com.bosch.smartcity.samsungtvlib.task;

import javax.security.auth.login.Configuration;

import com.bosch.smartcity.samsungtvlib.Service;
import com.bosch.smartcity.samsungtvlib.TVConfiguration;

public class ConnectTask extends TVTask{

	private TVConfiguration configuration;
	private Service tvService = new Service();
	
	public ConnectTask(ITaskEventListener listener, TVConfiguration configuration) {
		super(listener);
		this.configuration = configuration;
	}

	@Override
	protected void runTask() {
		tvService.connectToDevice(configuration.ip, configuration.port);
	}

	@Override
	protected Object getTaskResult() {
		return tvService.getAvailableTVs();
	}

}
