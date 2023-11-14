package com.reactvyhealthy.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A TipoConsulta.
 */
@Table("tipo_consulta")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TipoConsulta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nome")
    private String nome;

    @NotNull(message = "must not be null")
    @Column("tempo")
    private LocalDate tempo;

    @NotNull(message = "must not be null")
    @Column("valor_consulta")
    private Double valorConsulta;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TipoConsulta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public TipoConsulta nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getTempo() {
        return this.tempo;
    }

    public TipoConsulta tempo(LocalDate tempo) {
        this.setTempo(tempo);
        return this;
    }

    public void setTempo(LocalDate tempo) {
        this.tempo = tempo;
    }

    public Double getValorConsulta() {
        return this.valorConsulta;
    }

    public TipoConsulta valorConsulta(Double valorConsulta) {
        this.setValorConsulta(valorConsulta);
        return this;
    }

    public void setValorConsulta(Double valorConsulta) {
        this.valorConsulta = valorConsulta;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoConsulta)) {
            return false;
        }
        return getId() != null && getId().equals(((TipoConsulta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoConsulta{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", tempo='" + getTempo() + "'" +
            ", valorConsulta=" + getValorConsulta() +
            "}";
    }
}
