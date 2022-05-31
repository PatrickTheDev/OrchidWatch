package com.github.patrickpaul.fcmservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class FCMServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FCMServiceApplication.class, args);
    }

    /*
    FileInputStream serviceAccount =
        new FileInputStream("path/to/serviceAccountKey.json");

    FirebaseOptions options = new FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build();

    FirebaseApp.initializeApp(options);
     */

}
