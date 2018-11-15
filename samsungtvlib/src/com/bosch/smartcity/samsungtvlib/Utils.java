package com.bosch.smartcity.samsungtvlib;

import java.io.IOException;
import java.net.InetAddress;

public class Utils {
	public static boolean isHostReachable(String host) {
		try {
			return InetAddress.getByName(host).isReachable(3000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
