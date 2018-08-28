package com.example.runshell;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.runshell.controller.ShellExecutor;

@SpringBootApplication
public class RunshellApplication {
	
	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(RunshellApplication.class, args);

		String adbLocation = "C:/Users/i325639/AppData/Local/Android/Sdk/platform-tools/adb.exe";
		//String adbLocation = args[0];
		System.err.println("adb location:"+adbLocation);
		
		ShellExecutor.adbLocation = adbLocation;
		
		openHomePage();
	}

	private static void openHomePage() {
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://localhost:9998");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
