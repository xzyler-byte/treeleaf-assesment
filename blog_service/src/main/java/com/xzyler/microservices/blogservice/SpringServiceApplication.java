package com.xzyler.microservices.blogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication()
@EnableJpaAuditing
@EnableDiscoveryClient
@EnableFeignClients("com.xzyler.microservices.blogservice.proxy")
@ComponentScan("com.xzyler.microservices.blogservice.*")
public class SpringServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringServiceApplication.class, args);
    }
}