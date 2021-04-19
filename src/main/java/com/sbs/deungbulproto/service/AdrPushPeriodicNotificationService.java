package com.sbs.deungbulproto.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdrPushPeriodicNotificationService {

    public static String PeriodicNotificationJson() throws JSONException {
        LocalDate localDate = LocalDate.now();

        String sampleData[] = {
        		"fvToz4zBT9-MWXZUp2SaB_:APA91bHsuE9-HmSoS34xXq7VIRrVRAxCtJXd5-02bB5Xl18mSUAO2bklTLHWQCTY8bIKSNy2Zc31kYnRqe6QogFEVa5wka0skquAY1GFiiRveI6AtgYQbEV7ErE4naJZ528Lx9FRa_V6"
        		};

        JSONObject body = new JSONObject();

        List<String> tokenlist = new ArrayList<String>();

        for(int i=0; i<sampleData.length; i++){
            tokenlist.add(sampleData[i]);
        }

        JSONArray array = new JSONArray();

        for(int i=0; i<tokenlist.size(); i++) {
            array.put(tokenlist.get(i));
        }

        body.put("registration_ids", array);

        JSONObject notification = new JSONObject();
        
        notification.put("title","안녕!");
        notification.put("body","오늘은 "+localDate.getDayOfWeek().name()+" 입니다!");
        
        body.put("notification", notification);        

        System.out.println(body.toString());

        return body.toString();
    }
}