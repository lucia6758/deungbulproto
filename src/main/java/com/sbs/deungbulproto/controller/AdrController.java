package com.sbs.deungbulproto.controller;

import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sbs.deungbulproto.container.Container;
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
    public @ResponseBody ResponseEntity<String> send(String pushTitle, String pushBody, String... args) throws JSONException, InterruptedException  {
    	if( pushTitle.length() == 0 ) {
    		pushTitle = "기본 제목";
    	}
    	if( pushBody.length() == 0 ) {
    		pushBody = "";
    	}
    	if(args.length == 0) {
    		args[0] = "fvToz4zBT9-MWXZUp2SaB_:APA91bHsuE9-HmSoS34xXq7VIRrVRAxCtJXd5-02bB5Xl18mSUAO2bklTLHWQCTY8bIKSNy2Zc31kYnRqe6QogFEVa5wka0skquAY1GFiiRveI6AtgYQbEV7ErE4naJZ528Lx9FRa_V6";
    	}
    	
        String notifications = AdrPushPeriodicNotificationService.PeriodicNotificationJson(pushTitle, pushBody, args);   
        
        HttpHeaders headers = new HttpHeaders(); 
        Charset utf8 = Charset.forName("UTF-8"); 
        MediaType mediaType = new MediaType("application", "json", utf8); 
        headers.setContentType(mediaType);

        HttpEntity<String> request = new HttpEntity<>(notifications, headers);

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
    	
    	Container.session.deviceIdToken = req.getParameter("deviceIdToken");	 
    	
    	System.out.println("현재 기기의 deviceId Token = " + Container.session.deviceIdToken);
    	
    	return "현재 기기의 deviceId Token = " + Container.session.deviceIdToken;
    	
    }
    
}