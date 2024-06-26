package com.jovialcode.manager.infra.configuration;

import com.jovialcode.manager.domains.crawler.CrawlerRepository;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;


@Configuration
@RequiredArgsConstructor
@EnableR2dbcRepositories(
    basePackageClasses = {CrawlerRepository.class},
    entityOperationsRef = "crawlerEntityTemplate")
public class CrawlerRepositoryConfig {

    private final DatabaseConfig databaseConfig;


    @Bean(value = "crawlerConnectionFactory")
    public ConnectionFactory crawlerConnectionFactory() {
        ConnectionFactoryOptions options = databaseConfig.getConnectionFactoryOptionsBuilder("crawler").build();
        return ConnectionFactories.get(options);
    }

    @Bean(value = "crawlerEntityTemplate")
    public R2dbcEntityOperations crawlerEntityTemplate(@Qualifier("crawlerConnectionFactory") ConnectionFactory connectionFactory) {
        return databaseConfig.createR2dbcEntityTemplate(connectionFactory);
    }
}
