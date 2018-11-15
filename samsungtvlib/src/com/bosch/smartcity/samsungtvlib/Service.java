package com.bosch.smartcity.samsungtvlib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Service {
	
	public void WakeOnWirelessLan(String macAddr) {
		int SRC_PORT = 2014;
		int DST_PORT = 2014;
		String magicPacketId = "FF:FF:FF:FF:FF:FF";
		String broadCastAddr = "255.255.255.255";
		String wakeUpIdentifier = "SECWOW";
		String secureOn = "00:00:00:00:00:00";

		int wow_packet_max_size = 136;
		int wow_packet_min_size = 102;
		int wow_packet_sec_size = 6;
		int wow_packet_ss_size = 12;
		int packetSizeAlloc = 102;
		int reservedField = 0;
		int applicationID = 0;

		packetSizeAlloc += 6;
		packetSizeAlloc += 12;

		ByteBuffer wowPacketBuffer = ByteBuffer.allocate(packetSizeAlloc);
		wowPacketBuffer.put(convertMacAddrToBytes("FF:FF:FF:FF:FF:FF"));

		for (int i = 0; i < 16; i++) {
			wowPacketBuffer.put(convertMacAddrToBytes(macAddr));
		}

		wowPacketBuffer.put(convertMacAddrToBytes("00:00:00:00:00:00"));
		wowPacketBuffer.put("SECWOW".getBytes());
		wowPacketBuffer.putInt(reservedField);
		wowPacketBuffer.put((byte) applicationID);

		byte[] magicPacket = wowPacketBuffer.array();

		DatagramSocket wowSocket = null;

		try {
			wowSocket = new DatagramSocket(null);
			wowSocket.setReuseAddress(true);
			wowSocket.bind(new InetSocketAddress(2014));

			DatagramPacket wowPacket = new DatagramPacket(magicPacket, magicPacket.length);

			wowPacket.setAddress(InetAddress.getByName("255.255.255.255"));
			wowPacket.setPort(2014);

			wowSocket.send(wowPacket);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (wowSocket != null) {
				wowSocket.close();
			}
		}
	}

	private static byte[] convertMacAddrToBytes(String macAddr) {
		String[] macAddrAtoms = macAddr.split(":");

		byte[] macAddressBytes = new byte[6];
		for (int i = 0; i < 6; i++) {
			Integer hex = Integer.valueOf(Integer.parseInt(macAddrAtoms[i], 16));
			macAddressBytes[i] = hex.byteValue();
		}

		return macAddressBytes;
	}
	
	public List<String> getAvailableTVs() {
	
		List<String> outputLines = new ArrayList<>();
		try {
			outputLines = runCommandWithParam("sdb", "devices");
		} catch (InvalidParameterException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//no available TVs
		if(outputLines.size() <= 1)
			return null;
		
		//Remove the first line: "List of devices attached"
		outputLines.remove(0);
		
		return outputLines;
	}
	
	public boolean isTVConnected(String tvName) {
		List<String> availableTVs = getAvailableTVs();
		if(availableTVs == null)
			return false;
		for(String tv : availableTVs) {
			if(tv.contains(tvName)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean connectToDevice(String ip, String port) {
		
		List<String> outputs = null;
		try {
			outputs = runCommand("sdb connect " + ip + ":" + port);
		} catch (InvalidParameterException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		if(outputs == null)
			return false;
		
		//device already connected
		if(outputs.size() == 1 && outputs.get(0).contains("is already connected"))
			return true;
		
		if(outputs.size() == 2 && outputs.get(1).contains("connected to")) {
			return true;
		}
		
		return false;
	}
	
	public void runApp(String appId, String tvName) {
		try {
			List<String> outputs = runCommand("tizen.bat run -t " + tvName + " -p " + appId);
		} catch (InvalidParameterException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static List<String> runCommandWithParam(String command, String params) throws IOException, InvalidParameterException {
		
		if(command.isEmpty())
			throw new InvalidParameterException("Command cannot null");
		
		System.out.println("Run command:" + command + " " + params);
		
		Runtime rt = Runtime.getRuntime();
		String[] commands = {command, params};
		Process proc = rt.exec(commands);
		
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		
		List<String> outputLines = new ArrayList<>();
		String line = null;
		while((line = stdInput.readLine()) != null) {
			outputLines.add(line);
		}
		
		String lineError = null;
		while((lineError = stdError.readLine()) != null) {
			System.out.println("\nRun command error:" + lineError);
		}
		
		return outputLines;
	}
	
private static List<String> runCommand(String command) throws IOException, InvalidParameterException {
		
		if(command.isEmpty())
			throw new InvalidParameterException("Command cannot null");
		
		System.out.println("Run command:" + command);
		
		Runtime rt = Runtime.getRuntime();
		Process proc = rt.exec(command);
		
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		
		List<String> outputLines = new ArrayList<>();
		String line = null;
		while((line = stdInput.readLine()) != null) {
			outputLines.add(line);
		}
		
		String lineError = null;
		while((lineError = stdError.readLine()) != null) {
			System.out.println("\nRun command error:" + lineError);
		}
		
		return outputLines;
	}
	
}
