package com.sbs.deungbulproto.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sbs.deungbulproto.dto.Adm;
import com.sbs.deungbulproto.dto.Client;
import com.sbs.deungbulproto.dto.Expert;
import com.sbs.deungbulproto.service.AdmMemberService;
import com.sbs.deungbulproto.service.AdrPushNotificationService;
import com.sbs.deungbulproto.service.AdrPushPeriodicNotificationService;
import com.sbs.deungbulproto.service.ClientService;
import com.sbs.deungbulproto.service.ExpertService;

@RestController
public class AdrController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AdrPushNotificationService androidPushNotificationsService;
    @Autowired
	ClientService clientService;
    @Autowired
	ExpertService expertService;
    @Autowired
	AdmMemberService admService;

    @RequestMapping(value = "/adr/push/send", produces="text/plain; charset=UTF-8;")
    public @ResponseBody ResponseEntity<String> send() throws JSONException, InterruptedException  {
        String notifications = AdrPushPeriodicNotificationService.PeriodicNotificationJson();

        HttpEntity<String> request = new HttpEntity<>(notifications);

        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try{
            String firebaseResponse = pushNotification.get();
            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        }
        catch (InterruptedException e){
            logger.debug("got interrupted!");
            throw new InterruptedException();
        }
        catch (ExecutionException e){
            logger.debug("execution error!");
        }

        return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
    }
    
    @RequestMapping(value = "/adr/push/receiveDeviceId")
    public @ResponseBody String receive(HttpServletRequest req, HttpSession session) {
    	
    	String deviceIdToken = req.getParameter("deviceIdToken"); 	
    	
    	System.out.println("현재 기기의 deviceId Token = " + deviceIdToken);
    	session.setAttribute("deviceIdToken", deviceIdToken);
    	
    	return "현재 기기의 deviceId Token = " + deviceIdToken;
    	
    }
    
}