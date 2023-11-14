package com.reactvyhealthy.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.reactvyhealthy.domain.Especialidade} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EspecialidadeDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nome;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EspecialidadeDTO)) {
            return false;
        }

        EspecialidadeDTO especialidadeDTO = (EspecialidadeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, especialidadeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EspecialidadeDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
