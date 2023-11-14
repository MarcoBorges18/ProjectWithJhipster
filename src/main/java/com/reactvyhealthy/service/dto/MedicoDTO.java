package com.reactvyhealthy.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.reactvyhealthy.domain.Medico} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicoDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nome;

    @NotNull(message = "must not be null")
    private String email;

    @NotNull(message = "must not be null")
    private String crm;

    @NotNull(message = "must not be null")
    private Boolean ativo;

    private EspecialidadeDTO especialidade;

    private EnderecoDTO endereco;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public EspecialidadeDTO getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(EspecialidadeDTO especialidade) {
        this.especialidade = especialidade;
    }

    public EnderecoDTO getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoDTO endereco) {
        this.endereco = endereco;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicoDTO)) {
            return false;
        }

        MedicoDTO medicoDTO = (MedicoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicoDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", email='" + getEmail() + "'" +
            ", crm='" + getCrm() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", especialidade=" + getEspecialidade() +
            ", endereco=" + getEndereco() +
            "}";
    }
}
