package com.reactvyhealthy.service.mapper;

import com.reactvyhealthy.domain.Agendamento;
import com.reactvyhealthy.domain.Medico;
import com.reactvyhealthy.domain.Paciente;
import com.reactvyhealthy.domain.TipoConsulta;
import com.reactvyhealthy.service.dto.AgendamentoDTO;
import com.reactvyhealthy.service.dto.MedicoDTO;
import com.reactvyhealthy.service.dto.PacienteDTO;
import com.reactvyhealthy.service.dto.TipoConsultaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Agendamento} and its DTO {@link AgendamentoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AgendamentoMapper extends EntityMapper<AgendamentoDTO, Agendamento> {
    @Mapping(target = "medico", source = "medico", qualifiedByName = "medicoId")
    @Mapping(target = "paciente", source = "paciente", qualifiedByName = "pacienteId")
    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "tipoConsultaId")
    AgendamentoDTO toDto(Agendamento s);

    @Named("medicoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MedicoDTO toDtoMedicoId(Medico medico);

    @Named("pacienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PacienteDTO toDtoPacienteId(Paciente paciente);

    @Named("tipoConsultaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TipoConsultaDTO toDtoTipoConsultaId(TipoConsulta tipoConsulta);
}
