package com.reactvyhealthy.repository.rowmapper;

import com.reactvyhealthy.domain.Endereco;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Endereco}, with proper type conversions.
 */
@Service
public class EnderecoRowMapper implements BiFunction<Row, String, Endereco> {

    private final ColumnConverter converter;

    public EnderecoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Endereco} stored in the database.
     */
    @Override
    public Endereco apply(Row row, String prefix) {
        Endereco entity = new Endereco();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setLogradouro(converter.fromRow(row, prefix + "_logradouro", String.class));
        entity.setBairro(converter.fromRow(row, prefix + "_bairro", String.class));
        entity.setCep(converter.fromRow(row, prefix + "_cep", String.class));
        entity.setNumero(converter.fromRow(row, prefix + "_numero", String.class));
        entity.setComplemento(converter.fromRow(row, prefix + "_complemento", String.class));
        entity.setCidade(converter.fromRow(row, prefix + "_cidade", String.class));
        entity.setUf(converter.fromRow(row, prefix + "_uf", String.class));
        return entity;
    }
}
