package com.jovialcode.manager.web.parsing;

import com.jovialcode.manager.domains.tableschema.TableColumn;
import com.jovialcode.manager.domains.tableschema.TableSchemaService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class ParsingInfoTableHandler {
    private final TableSchemaService tableSchemaService;

    public @NonNull Mono<ServerResponse> table(ServerRequest request){
        Flux<TableColumn> tableSchema = tableSchemaService.getTableSchema("temp");

        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(tableSchema));
    }
}
