package com.reactvyhealthy.service.mapper;

import com.reactvyhealthy.domain.Endereco;
import com.reactvyhealthy.domain.Paciente;
import com.reactvyhealthy.domain.Plano;
import com.reactvyhealthy.service.dto.EnderecoDTO;
import com.reactvyhealthy.service.dto.PacienteDTO;
import com.reactvyhealthy.service.dto.PlanoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Paciente} and its DTO {@link PacienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface PacienteMapper extends EntityMapper<PacienteDTO, Paciente> {
    @Mapping(target = "plano", source = "plano", qualifiedByName = "planoId")
    @Mapping(target = "endereco", source = "endereco", qualifiedByName = "enderecoId")
    PacienteDTO toDto(Paciente s);

    @Named("planoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlanoDTO toDtoPlanoId(Plano plano);

    @Named("enderecoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EnderecoDTO toDtoEnderecoId(Endereco endereco);
}
