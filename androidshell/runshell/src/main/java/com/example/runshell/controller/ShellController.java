package com.example.runshell.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShellController {
	@Autowired
    private SimpMessagingTemplate template;
	
//	@MessageMapping("/hello")
//    @SendTo("/topic/stepmessage")
//    public StepMessage greeting() throws Exception {
//        Thread.sleep(1000); // simulated delay
//        return new StepMessage("Hello, " + HtmlUtils.htmlEscape("test") + "!");
//    }
	
	@RequestMapping("/start")
    public void start() throws Exception {
		ShellExecutor.start(template);
    }
	
	@RequestMapping("/stop")
    public String stop() throws Exception {
		ShellExecutor.stop();
		return "stopped";
    }
	
	@RequestMapping("/test")
    public String test() throws Exception {
		return ShellExecutor.test(template);
    }
}
