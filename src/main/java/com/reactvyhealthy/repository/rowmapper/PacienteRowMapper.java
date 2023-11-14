package com.reactvyhealthy.repository.rowmapper;

import com.reactvyhealthy.domain.Paciente;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Paciente}, with proper type conversions.
 */
@Service
public class PacienteRowMapper implements BiFunction<Row, String, Paciente> {

    private final ColumnConverter converter;

    public PacienteRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Paciente} stored in the database.
     */
    @Override
    public Paciente apply(Row row, String prefix) {
        Paciente entity = new Paciente();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNome(converter.fromRow(row, prefix + "_nome", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setCpf(converter.fromRow(row, prefix + "_cpf", String.class));
        entity.setTelefone(converter.fromRow(row, prefix + "_telefone", String.class));
        entity.setAtivo(converter.fromRow(row, prefix + "_ativo", Boolean.class));
        entity.setPlanoId(converter.fromRow(row, prefix + "_plano_id", Long.class));
        entity.setEnderecoId(converter.fromRow(row, prefix + "_endereco_id", Long.class));
        return entity;
    }
}
