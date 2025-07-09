package com.aoverflow.mmb.question_service.common.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.aoverflow.mmb.question_service")
public class OpenFeignConfig {

}
