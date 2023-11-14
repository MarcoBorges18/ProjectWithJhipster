package com.reactvyhealthy.service.mapper;

import com.reactvyhealthy.domain.Endereco;
import com.reactvyhealthy.service.dto.EnderecoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Endereco} and its DTO {@link EnderecoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EnderecoMapper extends EntityMapper<EnderecoDTO, Endereco> {}
