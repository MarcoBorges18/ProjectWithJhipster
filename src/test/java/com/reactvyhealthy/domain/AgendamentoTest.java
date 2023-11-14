package com.reactvyhealthy.domain;

import static com.reactvyhealthy.domain.AgendamentoTestSamples.*;
import static com.reactvyhealthy.domain.MedicoTestSamples.*;
import static com.reactvyhealthy.domain.PacienteTestSamples.*;
import static com.reactvyhealthy.domain.TipoConsultaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reactvyhealthy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgendamentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agendamento.class);
        Agendamento agendamento1 = getAgendamentoSample1();
        Agendamento agendamento2 = new Agendamento();
        assertThat(agendamento1).isNotEqualTo(agendamento2);

        agendamento2.setId(agendamento1.getId());
        assertThat(agendamento1).isEqualTo(agendamento2);

        agendamento2 = getAgendamentoSample2();
        assertThat(agendamento1).isNotEqualTo(agendamento2);
    }

    @Test
    void medicoTest() throws Exception {
        Agendamento agendamento = getAgendamentoRandomSampleGenerator();
        Medico medicoBack = getMedicoRandomSampleGenerator();

        agendamento.setMedico(medicoBack);
        assertThat(agendamento.getMedico()).isEqualTo(medicoBack);

        agendamento.medico(null);
        assertThat(agendamento.getMedico()).isNull();
    }

    @Test
    void pacienteTest() throws Exception {
        Agendamento agendamento = getAgendamentoRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        agendamento.setPaciente(pacienteBack);
        assertThat(agendamento.getPaciente()).isEqualTo(pacienteBack);

        agendamento.paciente(null);
        assertThat(agendamento.getPaciente()).isNull();
    }

    @Test
    void tipoTest() throws Exception {
        Agendamento agendamento = getAgendamentoRandomSampleGenerator();
        TipoConsulta tipoConsultaBack = getTipoConsultaRandomSampleGenerator();

        agendamento.setTipo(tipoConsultaBack);
        assertThat(agendamento.getTipo()).isEqualTo(tipoConsultaBack);

        agendamento.tipo(null);
        assertThat(agendamento.getTipo()).isNull();
    }
}
