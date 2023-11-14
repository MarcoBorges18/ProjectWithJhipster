package com.reactvyhealthy.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.reactvyhealthy.domain.Agendamento} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgendamentoDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Double valorFinal;

    private MedicoDTO medico;

    private PacienteDTO paciente;

    private TipoConsultaDTO tipo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(Double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public MedicoDTO getMedico() {
        return medico;
    }

    public void setMedico(MedicoDTO medico) {
        this.medico = medico;
    }

    public PacienteDTO getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteDTO paciente) {
        this.paciente = paciente;
    }

    public TipoConsultaDTO getTipo() {
        return tipo;
    }

    public void setTipo(TipoConsultaDTO tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgendamentoDTO)) {
            return false;
        }

        AgendamentoDTO agendamentoDTO = (AgendamentoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, agendamentoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgendamentoDTO{" +
            "id=" + getId() +
            ", valorFinal=" + getValorFinal() +
            ", medico=" + getMedico() +
            ", paciente=" + getPaciente() +
            ", tipo=" + getTipo() +
            "}";
    }
}
