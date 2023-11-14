package com.reactvyhealthy.repository.rowmapper;

import com.reactvyhealthy.domain.TipoConsulta;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TipoConsulta}, with proper type conversions.
 */
@Service
public class TipoConsultaRowMapper implements BiFunction<Row, String, TipoConsulta> {

    private final ColumnConverter converter;

    public TipoConsultaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TipoConsulta} stored in the database.
     */
    @Override
    public TipoConsulta apply(Row row, String prefix) {
        TipoConsulta entity = new TipoConsulta();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNome(converter.fromRow(row, prefix + "_nome", String.class));
        entity.setTempo(converter.fromRow(row, prefix + "_tempo", LocalDate.class));
        entity.setValorConsulta(converter.fromRow(row, prefix + "_valor_consulta", Double.class));
        return entity;
    }
}
