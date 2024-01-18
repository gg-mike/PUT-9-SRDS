package pl.put.srdsproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan
public class SrdsProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SrdsProjectApplication.class, args);
    }

}
