package com.sbs.deungbulproto.controller;

import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import com.sbs.deungbulproto.service.AdrPushNotificationService;
import com.sbs.deungbulproto.service.AdrPushPeriodicNotificationService;

@RestController
public class AdrController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AdrPushNotificationService androidPushNotificationsService;

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
    
    @RequestMapping(value = "/receive")
    public @ResponseBody void receive(HttpServletRequest req) {
    	String deviceId = (String)req.getAttribute("deviceId");
    	
    	
    }
    
}