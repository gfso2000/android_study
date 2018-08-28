package com.example.runshell.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.client.RestTemplate;

import com.example.runshell.bean.StepMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ShellExecutor {
	private static final String WEBSOCKET_TOPIC_NAME = "/topic/stepmessage";
	public static String adbLocation = "";
	public static boolean stop = false;
	
	public static String test(SimpMessagingTemplate template) {
		//execute any command to warm up adb
		ShellExecutor.execShellCmd(adbLocation, "shell", "wm", "size");

		//get IMEI
		//adb shell "service call iphonesubinfo 1 | toybox cut -d \"'\" -f2 | toybox grep -Eo '[0-9]' | toybox xargs | toybox sed 's/\ //g'"
		String imeiCommand = "\"service call iphonesubinfo 1 | toybox cut -d \\\"'\\\" -f2 | toybox grep -Eo '[0-9]' | toybox xargs | toybox sed 's/\\ //g'\"";
		String imei = ShellExecutor.execShellCmd(adbLocation, "shell", imeiCommand);
		return imei;
	}
	
	public static void start(SimpMessagingTemplate template) {
		//execute any command to warm up adb
		ShellExecutor.execShellCmd(adbLocation, "shell", "wm", "size");

		//get IMEI
		//adb shell "service call iphonesubinfo 1 | toybox cut -d \"'\" -f2 | toybox grep -Eo '[0-9]' | toybox xargs | toybox sed 's/\ //g'"
		String imeiCommand = "\"service call iphonesubinfo 1 | toybox cut -d \\\"'\\\" -f2 | toybox grep -Eo '[0-9]' | toybox xargs | toybox sed 's/\\ //g'\"";
		String imei = ShellExecutor.execShellCmd(adbLocation, "shell", imeiCommand);
		if(imei.contains("error")) {
			//if mobile is not connected
			template.convertAndSend(WEBSOCKET_TOPIC_NAME, new StepMessage(imei));
			return;
		}
		
		template.convertAndSend(WEBSOCKET_TOPIC_NAME, new StepMessage("IMEI："+imei));
		//get all user
		String[] allUsers = getAllUser(imei);
		//get shell script
		String shellScript = getShell(imei);
		
		for(String user:allUsers) {
			//the user is "13482877377"
			String username = user;
			String password = "abc123456";
			template.convertAndSend(WEBSOCKET_TOPIC_NAME, new StepMessage("用户名："+username+"-----------------------"));
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(shellScript).get("steps");
				if(root.isArray()) {
					for (final JsonNode objNode : root) {
						String command = objNode.get("command").asText().replace("%username%",username).replaceAll("%password%", password);
						int iterate = objNode.get("iterate").asInt();
						int sleepSeconds = objNode.get("sleep").asInt();
						String description = objNode.get("description").asText();
						
						for(int i=0;i<iterate;i++) {
							String[] commandArgs = command.split(" ");
						    String[] fullCommand = new String[commandArgs.length + 3];
						    fullCommand[0] = adbLocation;
						    fullCommand[1] = "shell";
						    fullCommand[2] = "input";
						    System.arraycopy(commandArgs, 0, fullCommand, 3, commandArgs.length);
						    if(stop){
						    	//if stop is clicked, return
						    	return;
						    }else {
						    	execShellCmd(fullCommand);
						    }
						}
						
						sleepAWhile(sleepSeconds);
						
						template.convertAndSend(WEBSOCKET_TOPIC_NAME, new StepMessage(description));
				    }
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			sleepAWhile(3);//after one user, sleep 3 seconds
		}
	}
	
	public static void stop() {
		stop = true;
	}

	private static void sleepAWhile(int sleepSeconds) {
		try {
			Thread.sleep(sleepSeconds*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static String[] getAllUser(String imei){
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl = "http://localhost:9999/maotai/QueryBookUserServlet?imei="+imei;
		ResponseEntity<String> response
		  = restTemplate.getForEntity(fooResourceUrl, String.class);
		return response.getBody().split(",");
	}
	
	private static String getShell(String imei) {
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl = "http://localhost:9999/maotai/adbshell/"+imei+".txt";
		ResponseEntity<String> response
		  = restTemplate.getForEntity(fooResourceUrl, String.class);
		return response.getBody();
	}
	
	public static String execShellCmd(String... command) {
		StringBuffer result = new StringBuffer("");
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.redirectErrorStream(true);
		Process process;
		try {
			process = pb.start();
			BufferedReader inStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String s; 
			while ((s = inStreamReader.readLine()) != null) {
				result.append(s);
			}
			return result.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
