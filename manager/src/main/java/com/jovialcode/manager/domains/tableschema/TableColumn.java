package com.jovialcode.manager.domains.tableschema;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TableColumn {
    private String columnName;
    private String dataType;
    private boolean isNullable;
    private Integer characterMaximumLength;
}
