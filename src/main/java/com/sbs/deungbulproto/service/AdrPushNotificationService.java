package com.sbs.deungbulproto.service;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sbs.deungbulproto.config.HeaderRequestInterceptor;

// firebase_server_key = firebase project > cloud messaging > server key

@Service
public class AdrPushNotificationService {
    private static final String firebase_server_key="AAAAc5C7L-c:APA91bF69g1AmO6g1ywI5zb6T5xRc_P8abJvMHlDSFyGfUkYo_n3P4PWEkbSOg64CuhJeCEiuw_TsQWlpzpk-odJgg7oP8ywlEbkKPEDXQaz2kgb2R98HOBSv4OGxMpraEERPWaTTLuw";
    private static final String firebase_api_url="https://fcm.googleapis.com/fcm/send";

    @Async
    public CompletableFuture<String> send(HttpEntity<String> entity) {

        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

        interceptors.add(new HeaderRequestInterceptor("Authorization",  "key=" + firebase_server_key));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json; UTF-8 "));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(firebase_api_url, entity, String.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }
}