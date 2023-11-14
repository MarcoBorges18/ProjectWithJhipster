package com.reactvyhealthy.domain;

import static com.reactvyhealthy.domain.EnderecoTestSamples.*;
import static com.reactvyhealthy.domain.EspecialidadeTestSamples.*;
import static com.reactvyhealthy.domain.MedicoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reactvyhealthy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medico.class);
        Medico medico1 = getMedicoSample1();
        Medico medico2 = new Medico();
        assertThat(medico1).isNotEqualTo(medico2);

        medico2.setId(medico1.getId());
        assertThat(medico1).isEqualTo(medico2);

        medico2 = getMedicoSample2();
        assertThat(medico1).isNotEqualTo(medico2);
    }

    @Test
    void especialidadeTest() throws Exception {
        Medico medico = getMedicoRandomSampleGenerator();
        Especialidade especialidadeBack = getEspecialidadeRandomSampleGenerator();

        medico.setEspecialidade(especialidadeBack);
        assertThat(medico.getEspecialidade()).isEqualTo(especialidadeBack);

        medico.especialidade(null);
        assertThat(medico.getEspecialidade()).isNull();
    }

    @Test
    void enderecoTest() throws Exception {
        Medico medico = getMedicoRandomSampleGenerator();
        Endereco enderecoBack = getEnderecoRandomSampleGenerator();

        medico.setEndereco(enderecoBack);
        assertThat(medico.getEndereco()).isEqualTo(enderecoBack);

        medico.endereco(null);
        assertThat(medico.getEndereco()).isNull();
    }
}
