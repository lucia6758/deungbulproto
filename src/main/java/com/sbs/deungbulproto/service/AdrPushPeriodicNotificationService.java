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

        String sampleData[] = {"fuvRi09NSSulzzMM0IFv1O:APA91bGM1lRpaoBrQA2ONjCQMdKoqqGuoQYLaMBnqTMjIkQg59T73yjTVT7rizsxQg6QK1tx_Bps3VccZxGKB2zm0LnSzu1IcWwgCSKDgTct6_oTMDfi5e9TCoTe4I_aIpcf6nGFU4uc","cnxrOo4WTuCgGL613ulKWN:APA91bHfdAxEHhlss2G583mAlbAMl_f_-jqjj3hJA6NXEu8u7qsR2kfxX-IqlQEMmi9dTeNDH4ozEHY_PaFy978FzwEFK88b_dCBoCGna5Gm7LQlCGUZUZIejKCTyxXWwCg6lr0vyZWy","device token value 3"};

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