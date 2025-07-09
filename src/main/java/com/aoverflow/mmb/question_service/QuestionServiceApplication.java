package com.aoverflow.mmb.question_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class QuestionServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(QuestionServiceApplication.class, args);
  }

}
