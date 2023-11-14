package com.reactvyhealthy.service.mapper;

import com.reactvyhealthy.domain.Endereco;
import com.reactvyhealthy.domain.Especialidade;
import com.reactvyhealthy.domain.Medico;
import com.reactvyhealthy.service.dto.EnderecoDTO;
import com.reactvyhealthy.service.dto.EspecialidadeDTO;
import com.reactvyhealthy.service.dto.MedicoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Medico} and its DTO {@link MedicoDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicoMapper extends EntityMapper<MedicoDTO, Medico> {
    @Mapping(target = "especialidade", source = "especialidade", qualifiedByName = "especialidadeId")
    @Mapping(target = "endereco", source = "endereco", qualifiedByName = "enderecoId")
    MedicoDTO toDto(Medico s);

    @Named("especialidadeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EspecialidadeDTO toDtoEspecialidadeId(Especialidade especialidade);

    @Named("enderecoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EnderecoDTO toDtoEnderecoId(Endereco endereco);
}
