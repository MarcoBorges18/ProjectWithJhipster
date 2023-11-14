package com.reactvyhealthy.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class EnderecoSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("logradouro", table, columnPrefix + "_logradouro"));
        columns.add(Column.aliased("bairro", table, columnPrefix + "_bairro"));
        columns.add(Column.aliased("cep", table, columnPrefix + "_cep"));
        columns.add(Column.aliased("numero", table, columnPrefix + "_numero"));
        columns.add(Column.aliased("complemento", table, columnPrefix + "_complemento"));
        columns.add(Column.aliased("cidade", table, columnPrefix + "_cidade"));
        columns.add(Column.aliased("uf", table, columnPrefix + "_uf"));

        return columns;
    }
}
