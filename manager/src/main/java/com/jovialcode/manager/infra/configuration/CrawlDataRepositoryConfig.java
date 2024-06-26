package com.jovialcode.manager.infra.configuration;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@RequiredArgsConstructor
@Configuration
@EnableR2dbcRepositories(entityOperationsRef = "crawlDataEntityTemplate")
public class CrawlDataRepositoryConfig {

    private final DatabaseConfig databaseConfig;

    @Bean(value = "crawlDataConnectionFactory")
    public ConnectionFactory crawlDataConnectionFactory() {
        ConnectionFactoryOptions options = databaseConfig.getConnectionFactoryOptionsBuilder("crawl_data").build();
        return ConnectionFactories.get(options);
    }

    @Bean(value = "crawlDataEntityTemplate")
    public R2dbcEntityOperations crawlDataEntityTemplate(@Qualifier("crawlDataConnectionFactory") ConnectionFactory connectionFactory) {
        return databaseConfig.createR2dbcEntityTemplate(connectionFactory);
    }
}
