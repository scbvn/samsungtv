package com.bosch.smartcity.samsungtvlib.task;

import com.bosch.smartcity.samsungtvlib.CONST;
import com.bosch.smartcity.samsungtvlib.Service;

abstract class TVTask implements Runnable {
	
	private ITaskEventListener listener;
	
	public TVTask(ITaskEventListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void run() {
		runTask();
		int retryTime = -1;
		while (retryTime < CONST.TASK_CHECKING_RETRY ) {
			System.out.println("run task:" + retryTime);
			//check the result
			Object taskResult = null;
			if((taskResult = getTaskResult()) != null) {
				if(this.listener != null)
					this.listener.onTaskCompleted(taskResult);
				return;
			} else {
				retryTime++;
			}
			//sleep then retry check result
			try {
				Thread.sleep(CONST.TASK_WAITING_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(this.listener != null) {
			this.listener.onTaskFail(null);
		}
	}
	
	protected abstract void runTask();
	protected abstract Object getTaskResult();
	
}
