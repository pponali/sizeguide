package com.sizeguide.datamigration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataImportService {

    private final JobLauncher jobLauncher;
    private final Job oracleToMongoJob;

    public void startImport() {
        try {
            log.info("Initializing data migration from Oracle to MongoDB");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            
            log.debug("Starting job with parameters: {}", jobParameters);
            JobExecution jobExecution = jobLauncher.run(oracleToMongoJob, jobParameters);
            
            log.info("Data import job started with execution id: {}", jobExecution.getId());
            log.debug("Job status: {}", jobExecution.getStatus());
            log.debug("Job start time: {}", jobExecution.getStartTime());
            
            // Log step executions
            jobExecution.getStepExecutions().forEach(stepExecution -> {
                log.debug("Step: {} - Status: {}", 
                    stepExecution.getStepName(),
                    stepExecution.getStatus());
                log.debug("Read count: {}, Write count: {}, Skip count: {}", 
                    stepExecution.getReadCount(),
                    stepExecution.getWriteCount(),
                    stepExecution.getSkipCount());
            });
            
        } catch (Exception e) {
            log.error("Error starting data import job", e);
            throw new RuntimeException("Failed to start data import job", e);
        }
    }
}
