package com.rbkmoney.threeds.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication(scanBasePackages = "com.rbkmoney.threeds.server")
public class ThreeDsServerApplication extends SpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThreeDsServerApplication.class, args);
    }
}
