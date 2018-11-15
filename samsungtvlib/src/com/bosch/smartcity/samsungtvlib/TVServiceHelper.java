package com.bosch.smartcity.samsungtvlib;

import java.security.InvalidParameterException;
import java.util.List;

import com.bosch.smartcity.samsungtvlib.task.ConnectTask;
import com.bosch.smartcity.samsungtvlib.task.ITaskEventListener;
import com.bosch.smartcity.samsungtvlib.task.WakeupOnLanTask;

public class TVServiceHelper {

	private static TVServiceHelper instance = null;
	private ITVEventListner listener;
	private Service tvService = new Service();
	private TVConfiguration configuration = null;
	
	private TVServiceHelper() {
		
	}
	
	public static TVServiceHelper getInstance() {
		if(instance ==  null)
			instance = new TVServiceHelper();
		return instance;
	}
	
	public void setListener(ITVEventListner listener) {
		this.listener = listener;
	}
	
	public void setConfiguration(TVConfiguration configuration) {
		this.configuration = configuration;
	}
	
	
	/**
	 * Launch a installed app on the TV
	 * @param appId id of the app
	 * @param isHighestPriority if it's true, the app will be launched despite another task is running. If it's 
	 * false, the app is only launched when the TV is turn off
	 */
	public void launchAppOnTV(String appId, boolean isHighestPriority) throws Exception {
		if(this.configuration == null)
			throw new Exception("Configuration of target TV cannot be null");
		
		//check the tv is available or not
		if(Utils.isHostReachable(configuration.ip)) {
			//Do nothing now
			System.out.println("TV is already turned on, do nothing to avoid interupt other tasks");
		} else {
			//turn on the tv
			tvService.WakeOnWirelessLan(configuration.wifiMacAddress);
			WakeupOnLanTask wakeUpTask = new WakeupOnLanTask(new ITaskEventListener() {
				
				@Override
				public void onTaskFail(String error) {
					System.out.println("Wake up on lan fail");
				}
				
				@Override
				public void onTaskCompleted(Object data) {
					
					//Connect to device
					ConnectTask connectTask = new ConnectTask(new ITaskEventListener() {
						
						@Override
						public void onTaskFail(String error) {
							System.out.println("Connect fail");
						}
						
						@Override
						public void onTaskCompleted(Object data) {
							List<String> result = (List<String>)data;
							System.out.println("Connect result:" + result);
							if(result != null && result.size() > 0) {
								System.out.println("Connect success");
								tvService.runApp(appId, configuration.tvName);
							}
							
							
						}
					}, configuration);
					new Thread(connectTask).start();
				}
			}, configuration);
			new Thread(wakeUpTask).start();
		}
	}
	
	
}
