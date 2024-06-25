package com.jovialcode.manager.infra.configuration;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.MySqlDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;

@Configuration
@EnableR2dbcRepositories(entityOperationsRef = "crawlDataEntityTemplate")
public class CrawlDataRepositoryConfig {

    @Value("${spring.r2dbc.host}")
    private String host;

    @Value("${spring.r2dbc.username}")
    private String username;

    @Value("${spring.r2dbc.password}")
    private String password;

    @Bean(value = "crawlDataConnectionFactory")
    public ConnectionFactory connectionFactory() {
        ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
            .option(ConnectionFactoryOptions.DRIVER, "mysql")
            .option(ConnectionFactoryOptions.HOST, host)
            .option(ConnectionFactoryOptions.PORT, 3306)
            .option(ConnectionFactoryOptions.DATABASE, "crawl_data")
            .option(ConnectionFactoryOptions.USER, username)
            .option(ConnectionFactoryOptions.PASSWORD, password)
            //.option(ConnectionFactoryOptions.CONNECT_TIMEOUT, Duration.ofSeconds(3))
            .build();
        return ConnectionFactories.get(options);
    }

    @Bean(value = "crawlDataEntityTemplate")
    public R2dbcEntityOperations entityTemplate(@Qualifier("crawlDataConnectionFactory") ConnectionFactory connectionFactory) {

        DefaultReactiveDataAccessStrategy strategy = new DefaultReactiveDataAccessStrategy(MySqlDialect.INSTANCE);
        DatabaseClient databaseClient = DatabaseClient.builder()
            .connectionFactory(connectionFactory)
            .bindMarkers(MySqlDialect.INSTANCE.getBindMarkersFactory())
            .build();

        return new R2dbcEntityTemplate(databaseClient, strategy);
    }
}
