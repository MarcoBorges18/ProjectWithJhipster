package com.reactvyhealthy.service.mapper;

import com.reactvyhealthy.domain.Plano;
import com.reactvyhealthy.service.dto.PlanoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Plano} and its DTO {@link PlanoDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlanoMapper extends EntityMapper<PlanoDTO, Plano> {}
