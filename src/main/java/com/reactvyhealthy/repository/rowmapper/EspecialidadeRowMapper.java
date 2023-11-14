package com.reactvyhealthy.repository.rowmapper;

import com.reactvyhealthy.domain.Especialidade;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Especialidade}, with proper type conversions.
 */
@Service
public class EspecialidadeRowMapper implements BiFunction<Row, String, Especialidade> {

    private final ColumnConverter converter;

    public EspecialidadeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Especialidade} stored in the database.
     */
    @Override
    public Especialidade apply(Row row, String prefix) {
        Especialidade entity = new Especialidade();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNome(converter.fromRow(row, prefix + "_nome", String.class));
        return entity;
    }
}
