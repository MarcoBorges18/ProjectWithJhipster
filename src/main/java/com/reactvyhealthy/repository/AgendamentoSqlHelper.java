package com.reactvyhealthy.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class AgendamentoSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("valor_final", table, columnPrefix + "_valor_final"));

        columns.add(Column.aliased("medico_id", table, columnPrefix + "_medico_id"));
        columns.add(Column.aliased("paciente_id", table, columnPrefix + "_paciente_id"));
        columns.add(Column.aliased("tipo_id", table, columnPrefix + "_tipo_id"));
        return columns;
    }
}
