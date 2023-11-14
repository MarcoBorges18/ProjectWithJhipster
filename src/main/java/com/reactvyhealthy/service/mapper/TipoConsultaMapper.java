package com.reactvyhealthy.service.mapper;

import com.reactvyhealthy.domain.TipoConsulta;
import com.reactvyhealthy.service.dto.TipoConsultaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoConsulta} and its DTO {@link TipoConsultaDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoConsultaMapper extends EntityMapper<TipoConsultaDTO, TipoConsulta> {}
