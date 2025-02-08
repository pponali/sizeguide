package com.sizeguide.datamigration.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfiguration {

    private final DataSource oracleDataSource;

    public BatchConfig(@Qualifier("oracleDataSource") DataSource oracleDataSource) {
        this.oracleDataSource = oracleDataSource;
    }

    @Override
    protected DataSource getDataSource() {
        return oracleDataSource;
    }
}
