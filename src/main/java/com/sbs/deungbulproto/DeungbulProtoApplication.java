package com.sbs.deungbulproto;

import java.io.FileInputStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@SpringBootApplication
public class DeungbulProtoApplication {
	
    private FirebaseOptions option;
    private Firestore db; 
    //private final static String PATH = "C:/work/sts-4.8.0.RELEASE/workspace/FirebaseNotificationServer/prac-9d3e8-firebase-adminsdk-12b92-37c7f156ba.json";
    private final static String COLLECTION_NAME = "컬렉션";

	public static void main(String[] args) {
		SpringApplication.run(DeungbulProtoApplication.class, args);
		DeungbulProtoApplication app = new DeungbulProtoApplication();
//        try {
//            app.init();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    
//    private void init() throws Exception{
//        FileInputStream refreshToken = new FileInputStream(PATH);
//        option = new FirebaseOptions.Builder()
//                .setCredentials(GoogleCredentials.fromStream(refreshToken))
//                .setDatabaseUrl("https://prac-9d3e8-default-rtdb.firebaseio.com")  //내 저장소 주소
//                .build();
//        FirebaseApp.initializeApp(option);
//    }

}
