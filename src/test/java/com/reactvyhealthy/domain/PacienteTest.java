package com.reactvyhealthy.domain;

import static com.reactvyhealthy.domain.EnderecoTestSamples.*;
import static com.reactvyhealthy.domain.PacienteTestSamples.*;
import static com.reactvyhealthy.domain.PlanoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reactvyhealthy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PacienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Paciente.class);
        Paciente paciente1 = getPacienteSample1();
        Paciente paciente2 = new Paciente();
        assertThat(paciente1).isNotEqualTo(paciente2);

        paciente2.setId(paciente1.getId());
        assertThat(paciente1).isEqualTo(paciente2);

        paciente2 = getPacienteSample2();
        assertThat(paciente1).isNotEqualTo(paciente2);
    }

    @Test
    void planoTest() throws Exception {
        Paciente paciente = getPacienteRandomSampleGenerator();
        Plano planoBack = getPlanoRandomSampleGenerator();

        paciente.setPlano(planoBack);
        assertThat(paciente.getPlano()).isEqualTo(planoBack);

        paciente.plano(null);
        assertThat(paciente.getPlano()).isNull();
    }

    @Test
    void enderecoTest() throws Exception {
        Paciente paciente = getPacienteRandomSampleGenerator();
        Endereco enderecoBack = getEnderecoRandomSampleGenerator();

        paciente.setEndereco(enderecoBack);
        assertThat(paciente.getEndereco()).isEqualTo(enderecoBack);

        paciente.endereco(null);
        assertThat(paciente.getEndereco()).isNull();
    }
}
