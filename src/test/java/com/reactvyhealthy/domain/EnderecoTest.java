package com.reactvyhealthy.domain;

import static com.reactvyhealthy.domain.EnderecoTestSamples.*;
import static com.reactvyhealthy.domain.MedicoTestSamples.*;
import static com.reactvyhealthy.domain.PacienteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reactvyhealthy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnderecoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Endereco.class);
        Endereco endereco1 = getEnderecoSample1();
        Endereco endereco2 = new Endereco();
        assertThat(endereco1).isNotEqualTo(endereco2);

        endereco2.setId(endereco1.getId());
        assertThat(endereco1).isEqualTo(endereco2);

        endereco2 = getEnderecoSample2();
        assertThat(endereco1).isNotEqualTo(endereco2);
    }

    @Test
    void pacienteTest() throws Exception {
        Endereco endereco = getEnderecoRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        endereco.setPaciente(pacienteBack);
        assertThat(endereco.getPaciente()).isEqualTo(pacienteBack);
        assertThat(pacienteBack.getEndereco()).isEqualTo(endereco);

        endereco.paciente(null);
        assertThat(endereco.getPaciente()).isNull();
        assertThat(pacienteBack.getEndereco()).isNull();
    }

    @Test
    void medicoTest() throws Exception {
        Endereco endereco = getEnderecoRandomSampleGenerator();
        Medico medicoBack = getMedicoRandomSampleGenerator();

        endereco.setMedico(medicoBack);
        assertThat(endereco.getMedico()).isEqualTo(medicoBack);
        assertThat(medicoBack.getEndereco()).isEqualTo(endereco);

        endereco.medico(null);
        assertThat(endereco.getMedico()).isNull();
        assertThat(medicoBack.getEndereco()).isNull();
    }
}
