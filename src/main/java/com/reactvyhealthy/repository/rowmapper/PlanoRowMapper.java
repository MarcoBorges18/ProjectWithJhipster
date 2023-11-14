package com.reactvyhealthy.repository.rowmapper;

import com.reactvyhealthy.domain.Plano;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Plano}, with proper type conversions.
 */
@Service
public class PlanoRowMapper implements BiFunction<Row, String, Plano> {

    private final ColumnConverter converter;

    public PlanoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Plano} stored in the database.
     */
    @Override
    public Plano apply(Row row, String prefix) {
        Plano entity = new Plano();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setConvenio(converter.fromRow(row, prefix + "_convenio", String.class));
        entity.setDesconto(converter.fromRow(row, prefix + "_desconto", Float.class));
        return entity;
    }
}
