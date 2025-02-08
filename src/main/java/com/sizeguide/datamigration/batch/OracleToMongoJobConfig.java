package com.sizeguide.datamigration.batch;

import com.sizeguide.domain.SizeGuide;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class OracleToMongoJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final MongoTemplate mongoTemplate;
    private final DataSource oracleDataSource;

    @Value("${batch.chunk-size:100}")
    private int chunkSize;

    @Bean
    public JdbcCursorItemReader<SizeGuide> reader() {
        JdbcCursorItemReader<SizeGuide> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(oracleDataSource);
        reader.setSql("SELECT * FROM SIZE_GUIDE"); // Modify according to your Oracle table structure
        reader.setRowMapper(new BeanPropertyRowMapper<>(SizeGuide.class));
        return reader;
    }

    @Bean
    public SizeGuideProcessor processor() {
        return new SizeGuideProcessor();
    }

    @Bean
    public MongoItemWriter<SizeGuide> writer() {
        MongoItemWriter<SizeGuide> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("sizeGuide");
        return writer;
    }

    @Bean
    public Step oracleToMongoStep() {
        return new StepBuilder("oracleToMongoStep", jobRepository)
                .<SizeGuide, SizeGuide>chunk(chunkSize, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job oracleToMongoJob() {
        return new JobBuilder("oracleToMongoJob", jobRepository)
                .start(oracleToMongoStep())
                .build();
    }
}
