package com.rbkmoney.threeds.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.stream.Stream;

@EnableScheduling
@ServletComponentScan
@SpringBootApplication(scanBasePackages = "com.rbkmoney.threeds.server")
public class ThreeDsServerApplication extends SpringApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ThreeDsServerApplication.class, args);
        Stream.of(applicationContext.getBeanDefinitionNames())
                .sorted()
                .filter(s -> s.indexOf("Processor") != -1)
                .filter(s -> s.indexOf("springframework") == -1)
                .filter(s -> s.indexOf("Chain") != -1)
                .forEach(s -> System.out.println(s));
    }
}
