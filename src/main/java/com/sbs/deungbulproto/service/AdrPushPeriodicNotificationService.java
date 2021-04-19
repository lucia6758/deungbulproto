package com.sbs.deungbulproto.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdrPushPeriodicNotificationService {

    public static String PeriodicNotificationJson(String pushTitle, String pushBody, String[] args) throws JSONException {
        LocalDate localDate = LocalDate.now();

        String receiveMembers[] = args;

        JSONObject body = new JSONObject();

        List<String> tokenlist = new ArrayList<String>();

        for(int i=0; i<receiveMembers.length; i++){
            tokenlist.add(receiveMembers[i]);
        }

        JSONArray array = new JSONArray();

        for(int i=0; i<tokenlist.size(); i++) {
            array.put(tokenlist.get(i));
        }

        body.put("registration_ids", array);

        JSONObject notification = new JSONObject();
        
        notification.put("title", pushTitle);
        notification.put("body", pushBody);
        
        body.put("notification", notification);        

        System.out.println(body.toString());

        return body.toString();
    }
}