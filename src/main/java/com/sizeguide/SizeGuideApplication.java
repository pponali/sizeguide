package com.sizeguide;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@OpenAPIDefinition(
    info = @Info(
        title = "Size Guide API",
        version = "1.0",
        description = "API for managing size guides with Excel upload and validation capabilities"
    )
)
public class SizeGuideApplication {
    public static void main(String[] args) {
        SpringApplication.run(SizeGuideApplication.class, args);
    }
}
