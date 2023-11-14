package com.reactvyhealthy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Agendamento.
 */
@Table("agendamento")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Agendamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("valor_final")
    private Double valorFinal;

    @Transient
    @JsonIgnoreProperties(value = { "especialidade", "endereco" }, allowSetters = true)
    private Medico medico;

    @Transient
    @JsonIgnoreProperties(value = { "plano", "endereco" }, allowSetters = true)
    private Paciente paciente;

    @Transient
    private TipoConsulta tipo;

    @Column("medico_id")
    private Long medicoId;

    @Column("paciente_id")
    private Long pacienteId;

    @Column("tipo_id")
    private Long tipoId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Agendamento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValorFinal() {
        return this.valorFinal;
    }

    public Agendamento valorFinal(Double valorFinal) {
        this.setValorFinal(valorFinal);
        return this;
    }

    public void setValorFinal(Double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public Medico getMedico() {
        return this.medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
        this.medicoId = medico != null ? medico.getId() : null;
    }

    public Agendamento medico(Medico medico) {
        this.setMedico(medico);
        return this;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        this.pacienteId = paciente != null ? paciente.getId() : null;
    }

    public Agendamento paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    public TipoConsulta getTipo() {
        return this.tipo;
    }

    public void setTipo(TipoConsulta tipoConsulta) {
        this.tipo = tipoConsulta;
        this.tipoId = tipoConsulta != null ? tipoConsulta.getId() : null;
    }

    public Agendamento tipo(TipoConsulta tipoConsulta) {
        this.setTipo(tipoConsulta);
        return this;
    }

    public Long getMedicoId() {
        return this.medicoId;
    }

    public void setMedicoId(Long medico) {
        this.medicoId = medico;
    }

    public Long getPacienteId() {
        return this.pacienteId;
    }

    public void setPacienteId(Long paciente) {
        this.pacienteId = paciente;
    }

    public Long getTipoId() {
        return this.tipoId;
    }

    public void setTipoId(Long tipoConsulta) {
        this.tipoId = tipoConsulta;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Agendamento)) {
            return false;
        }
        return getId() != null && getId().equals(((Agendamento) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Agendamento{" +
            "id=" + getId() +
            ", valorFinal=" + getValorFinal() +
            "}";
    }
}
