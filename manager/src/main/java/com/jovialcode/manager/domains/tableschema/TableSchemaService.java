package com.jovialcode.manager.domains.tableschema;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class TableSchemaService {

    public static final String COLUMN_NAME = "COLUMN_NAME";
    public static final String DATA_TYPE = "DATA_TYPE";
    public static final String IS_NULLABLE = "IS_NULLABLE";
    public static final String CHARACTER_MAXIMUM_LENGTH = "CHARACTER_MAXIMUM_LENGTH";
    public static final String YES = "YES";

    private final DatabaseClient databaseClient;

    public TableSchemaService(@Qualifier("crawlDataEntityTemplate") R2dbcEntityOperations r2dbcEntityOperations) {
        this.databaseClient = r2dbcEntityOperations.getDatabaseClient();
    }

    public Flux<TableColumn> getTableSchema(String tableName) {
        String query =
            "SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, CHARACTER_MAXIMUM_LENGTH " +
            "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = :tableName";

        Flux<TableColumn> result = databaseClient
            .sql(query)
            .bind("tableName", "%" + tableName + "%")
            .map(row -> new TableColumn(
                row.get(COLUMN_NAME, String.class),
                row.get(DATA_TYPE, String.class),
                YES.equals(row.get(IS_NULLABLE, String.class)),
                row.get(CHARACTER_MAXIMUM_LENGTH, Integer.class)
            ))
            .all();

        result.doOnError(throwable -> log.error("err : {}", throwable.getMessage()))
            .log();

        return result;
    }
}
