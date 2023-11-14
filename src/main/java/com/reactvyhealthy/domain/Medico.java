package com.reactvyhealthy.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Medico.
 */
@Table("medico")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Medico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nome")
    private String nome;

    @NotNull(message = "must not be null")
    @Column("email")
    private String email;

    @NotNull(message = "must not be null")
    @Column("crm")
    private String crm;

    @NotNull(message = "must not be null")
    @Column("ativo")
    private Boolean ativo;

    @Transient
    private Especialidade especialidade;

    @Transient
    private Endereco endereco;

    @Column("especialidade_id")
    private Long especialidadeId;

    @Column("endereco_id")
    private Long enderecoId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Medico id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Medico nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return this.email;
    }

    public Medico email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCrm() {
        return this.crm;
    }

    public Medico crm(String crm) {
        this.setCrm(crm);
        return this;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public Medico ativo(Boolean ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Especialidade getEspecialidade() {
        return this.especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
        this.especialidadeId = especialidade != null ? especialidade.getId() : null;
    }

    public Medico especialidade(Especialidade especialidade) {
        this.setEspecialidade(especialidade);
        return this;
    }

    public Endereco getEndereco() {
        return this.endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
        this.enderecoId = endereco != null ? endereco.getId() : null;
    }

    public Medico endereco(Endereco endereco) {
        this.setEndereco(endereco);
        return this;
    }

    public Long getEspecialidadeId() {
        return this.especialidadeId;
    }

    public void setEspecialidadeId(Long especialidade) {
        this.especialidadeId = especialidade;
    }

    public Long getEnderecoId() {
        return this.enderecoId;
    }

    public void setEnderecoId(Long endereco) {
        this.enderecoId = endereco;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medico)) {
            return false;
        }
        return getId() != null && getId().equals(((Medico) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medico{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", email='" + getEmail() + "'" +
            ", crm='" + getCrm() + "'" +
            ", ativo='" + getAtivo() + "'" +
            "}";
    }
}
