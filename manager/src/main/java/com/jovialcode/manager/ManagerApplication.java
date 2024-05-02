package com.jovialcode.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

@EnableR2dbcAuditing
@SpringBootApplication(
    exclude = {
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class,
    }
)
public class ManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class, args);
    }

}
