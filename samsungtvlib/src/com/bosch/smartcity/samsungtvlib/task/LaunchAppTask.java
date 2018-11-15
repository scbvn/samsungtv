package com.bosch.smartcity.samsungtvlib.task;

import com.bosch.smartcity.samsungtvlib.Service;

public class LaunchAppTask extends TVTask{

	private String appId;
	private String tvName;
	private Service tvService = new Service();
	
	public LaunchAppTask(ITaskEventListener listener, String appId, String tvName) {
		super(listener);
		this.appId = appId;
		this.tvName = tvName;
	}

	@Override
	protected void runTask() {
		tvService.runApp(appId, tvName);
	}

	@Override
	protected Object getTaskResult() {
		// TODO Auto-generated method stub
		return null;
	}

}
