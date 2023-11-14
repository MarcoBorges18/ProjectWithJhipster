package com.reactvyhealthy.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PacienteSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nome", table, columnPrefix + "_nome"));
        columns.add(Column.aliased("email", table, columnPrefix + "_email"));
        columns.add(Column.aliased("cpf", table, columnPrefix + "_cpf"));
        columns.add(Column.aliased("telefone", table, columnPrefix + "_telefone"));
        columns.add(Column.aliased("ativo", table, columnPrefix + "_ativo"));

        columns.add(Column.aliased("plano_id", table, columnPrefix + "_plano_id"));
        columns.add(Column.aliased("endereco_id", table, columnPrefix + "_endereco_id"));
        return columns;
    }
}
