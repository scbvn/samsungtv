package com.bosch.smartcity.samsungtvlib.task;

public interface ITaskEventListener {
	void onTaskCompleted(Object data);
	void onTaskFail(String error);
}
