package com.reactvyhealthy.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Plano.
 */
@Table("plano")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Plano implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("convenio")
    private String convenio;

    @NotNull(message = "must not be null")
    @Column("desconto")
    private Float desconto;

    @Transient
    private Paciente paciente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Plano id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConvenio() {
        return this.convenio;
    }

    public Plano convenio(String convenio) {
        this.setConvenio(convenio);
        return this;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public Float getDesconto() {
        return this.desconto;
    }

    public Plano desconto(Float desconto) {
        this.setDesconto(desconto);
        return this;
    }

    public void setDesconto(Float desconto) {
        this.desconto = desconto;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        if (this.paciente != null) {
            this.paciente.setPlano(null);
        }
        if (paciente != null) {
            paciente.setPlano(this);
        }
        this.paciente = paciente;
    }

    public Plano paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plano)) {
            return false;
        }
        return getId() != null && getId().equals(((Plano) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Plano{" +
            "id=" + getId() +
            ", convenio='" + getConvenio() + "'" +
            ", desconto=" + getDesconto() +
            "}";
    }
}
