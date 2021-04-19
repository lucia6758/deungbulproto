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
        		"eLreJ7j9RXGKrCNxeiFi-c:APA91bHFkGtJmjsM4vBzUmrno_NipJv8QozKON-5WnsEiBXEz2RMvzr-Gg6kB6KcumOJxzAum1xz_7wf2EcdxBjcuXmQAxoH41_5PQNgosmtQwywzLzJaqSPdTUpHaYlILZkomPb6z8r"
        		,"cnxrOo4WTuCgGL613ulKWN:APA91bHfdAxEHhlss2G583mAlbAMl_f_-jqjj3hJA6NXEu8u7qsR2kfxX-IqlQEMmi9dTeNDH4ozEHY_PaFy978FzwEFK88b_dCBoCGna5Gm7LQlCGUZUZIejKCTyxXWwCg6lr0vyZWy"
        		,"cv2C1RB9SFm8CAsqbjH9rR:APA91bFs1aXKAd7LdqWNJiya4YiOiPPe_Mtc-8sn8_9FJDtyfTiQVplwPgkPORT4MSTnmOylgLKl-lVqF0fz4ndq1bsnHLOcH90OS6w_v1bhgCRDpfhWnNKpc9lZNR2SFrYLAV2S26PG"
        		,"dnoVo9ZZSOyhGA9yYPwD5N:APA91bE31BlX4UB-id8kDhqhvDwQ86cnTv2bQdVOFgWVQ_W_4ZHSp_5CU8GUF0kzShJo4T9xcj_371xCSEUjgAKxN9nO3MciRZDsF0or7g49d45Tv2JVgG5mPdgyxzR09R47RHQQFqcD"
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