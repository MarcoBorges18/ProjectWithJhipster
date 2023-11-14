package com.reactvyhealthy.repository.rowmapper;

import com.reactvyhealthy.domain.Medico;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Medico}, with proper type conversions.
 */
@Service
public class MedicoRowMapper implements BiFunction<Row, String, Medico> {

    private final ColumnConverter converter;

    public MedicoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Medico} stored in the database.
     */
    @Override
    public Medico apply(Row row, String prefix) {
        Medico entity = new Medico();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNome(converter.fromRow(row, prefix + "_nome", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setCrm(converter.fromRow(row, prefix + "_crm", String.class));
        entity.setAtivo(converter.fromRow(row, prefix + "_ativo", Boolean.class));
        entity.setEspecialidadeId(converter.fromRow(row, prefix + "_especialidade_id", Long.class));
        entity.setEnderecoId(converter.fromRow(row, prefix + "_endereco_id", Long.class));
        return entity;
    }
}
