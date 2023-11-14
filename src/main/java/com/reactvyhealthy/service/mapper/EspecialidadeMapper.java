package com.reactvyhealthy.service.mapper;

import com.reactvyhealthy.domain.Especialidade;
import com.reactvyhealthy.service.dto.EspecialidadeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Especialidade} and its DTO {@link EspecialidadeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EspecialidadeMapper extends EntityMapper<EspecialidadeDTO, Especialidade> {}
