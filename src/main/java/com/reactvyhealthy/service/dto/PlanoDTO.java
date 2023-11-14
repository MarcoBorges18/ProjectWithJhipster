package com.reactvyhealthy.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.reactvyhealthy.domain.Plano} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlanoDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String convenio;

    @NotNull(message = "must not be null")
    private Float desconto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public Float getDesconto() {
        return desconto;
    }

    public void setDesconto(Float desconto) {
        this.desconto = desconto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanoDTO)) {
            return false;
        }

        PlanoDTO planoDTO = (PlanoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, planoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanoDTO{" +
            "id=" + getId() +
            ", convenio='" + getConvenio() + "'" +
            ", desconto=" + getDesconto() +
            "}";
    }
}
