package com.yl.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class SpringBootYlblogApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootYlblogApplication.class, args);
    }

}
