package com.sizeguide.config;

import com.sizeguide.job.ValidationJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail validationJobDetail() {
        return JobBuilder.newJob(ValidationJob.class)
                .withIdentity("validationJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger validationJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(validationJobDetail())
                .withIdentity("validationTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(5)
                        .repeatForever())
                .build();
    }
}
