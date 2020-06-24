package com.logicbig.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import java.time.LocalDateTime;


@Controller
public class HelloWebClientController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/")
    @HystrixCommand(fallbackMethod = "failed", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")})
    public String handleRequest(Model model) {
        //accessing hello-service
        HelloObject helloObject = restTemplate.getForObject("http://hello-service/hello", HelloObject.class);
        model.addAttribute("msg", helloObject.getMessage());
        model.addAttribute("time", LocalDateTime.now());
        return "hello-page";
    }
   
    public String failed(Model model) {
    	
    	System.out.println("failed");
    	return "failed";
    }
}