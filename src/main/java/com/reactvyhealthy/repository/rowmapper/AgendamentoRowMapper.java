package com.reactvyhealthy.repository.rowmapper;

import com.reactvyhealthy.domain.Agendamento;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Agendamento}, with proper type conversions.
 */
@Service
public class AgendamentoRowMapper implements BiFunction<Row, String, Agendamento> {

    private final ColumnConverter converter;

    public AgendamentoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Agendamento} stored in the database.
     */
    @Override
    public Agendamento apply(Row row, String prefix) {
        Agendamento entity = new Agendamento();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setValorFinal(converter.fromRow(row, prefix + "_valor_final", Double.class));
        entity.setMedicoId(converter.fromRow(row, prefix + "_medico_id", Long.class));
        entity.setPacienteId(converter.fromRow(row, prefix + "_paciente_id", Long.class));
        entity.setTipoId(converter.fromRow(row, prefix + "_tipo_id", Long.class));
        return entity;
    }
}
