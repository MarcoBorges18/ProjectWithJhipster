package com.reactvyhealthy.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.reactvyhealthy.domain.TipoConsulta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TipoConsultaDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nome;

    @NotNull(message = "must not be null")
    private LocalDate tempo;

    @NotNull(message = "must not be null")
    private Double valorConsulta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getTempo() {
        return tempo;
    }

    public void setTempo(LocalDate tempo) {
        this.tempo = tempo;
    }

    public Double getValorConsulta() {
        return valorConsulta;
    }

    public void setValorConsulta(Double valorConsulta) {
        this.valorConsulta = valorConsulta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoConsultaDTO)) {
            return false;
        }

        TipoConsultaDTO tipoConsultaDTO = (TipoConsultaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tipoConsultaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoConsultaDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", tempo='" + getTempo() + "'" +
            ", valorConsulta=" + getValorConsulta() +
            "}";
    }
}
