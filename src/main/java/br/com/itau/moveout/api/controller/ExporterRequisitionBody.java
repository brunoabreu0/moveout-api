package br.com.itau.moveout.api.controller;

import lombok.Data;

@Data
public class ExporterRequisitionBody {
    private String dbName;
    private String tableName;
    private String filterClause;
    private String selectedColumns;
}
