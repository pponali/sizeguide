package com.sizeguide.datamigration.runner;

import com.sizeguide.datamigration.service.DataImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "migration.auto-run", havingValue = "true")
public class DataMigrationRunner implements CommandLineRunner {

    private final DataImportService dataImportService;

    @Override
    public void run(String... args) {
        log.info("Starting data migration...");
        dataImportService.startImport();
    }
}
