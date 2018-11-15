package test;

import java.util.List;

import com.bosch.smartcity.samsungtvlib.Service;
import com.bosch.smartcity.samsungtvlib.TVConfiguration;
import com.bosch.smartcity.samsungtvlib.TVServiceHelper;
import com.bosch.smartcity.samsungtvlib.Utils;

public class TestApp {

	private final static String TV_IP_WIFI_MAC = "d8:e0:e1:92:36:83";
	private final static String TV_PORT = "26101";
	private final static String TV_IP = "192.168.1.112";
	private final static String WelcomeAppId = "qvdeYAZGAj.BasicUI";
	private final static String TV_NAME = "UA75MU6100";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Service.WakeOnWirelessLan("d8:e0:e1:92:36:83");
		
		runWelcomeApp();
		//(new Service()).runApp(WelcomeAppId, TV_NAME);
		
//		(new Service()).connectToDevice(TV_IP, TV_PORT);
//		System.out.println("Tvs:" + (new Service()).getAvailableTVs());
		
		//(new Service()).runApp(WelcomeAppId, TV_NAME);
	}
	
	private static void runWelcomeApp() {
		try {
			TVConfiguration configuration = new TVConfiguration();
			configuration.ip = TV_IP;
			configuration.port = TV_PORT;
			configuration.wifiMacAddress = TV_IP_WIFI_MAC;
			configuration.tvName = TV_NAME;
			TVServiceHelper.getInstance().setConfiguration(configuration);
			TVServiceHelper.getInstance().launchAppOnTV(WelcomeAppId, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
