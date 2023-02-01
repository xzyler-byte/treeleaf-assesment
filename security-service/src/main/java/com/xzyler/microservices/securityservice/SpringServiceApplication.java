package com.xzyler.microservices.securityservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages={"com.netflix.client.config.IClientConfig"})
@EnableJpaAuditing
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan("com.xzyler.microservices.securityservice.*")
public class SpringServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringServiceApplication.class, args);
    }
}