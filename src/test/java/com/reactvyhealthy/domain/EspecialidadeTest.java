package com.reactvyhealthy.domain;

import static com.reactvyhealthy.domain.EspecialidadeTestSamples.*;
import static com.reactvyhealthy.domain.MedicoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reactvyhealthy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EspecialidadeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Especialidade.class);
        Especialidade especialidade1 = getEspecialidadeSample1();
        Especialidade especialidade2 = new Especialidade();
        assertThat(especialidade1).isNotEqualTo(especialidade2);

        especialidade2.setId(especialidade1.getId());
        assertThat(especialidade1).isEqualTo(especialidade2);

        especialidade2 = getEspecialidadeSample2();
        assertThat(especialidade1).isNotEqualTo(especialidade2);
    }

    @Test
    void medicoTest() throws Exception {
        Especialidade especialidade = getEspecialidadeRandomSampleGenerator();
        Medico medicoBack = getMedicoRandomSampleGenerator();

        especialidade.setMedico(medicoBack);
        assertThat(especialidade.getMedico()).isEqualTo(medicoBack);
        assertThat(medicoBack.getEspecialidade()).isEqualTo(especialidade);

        especialidade.medico(null);
        assertThat(especialidade.getMedico()).isNull();
        assertThat(medicoBack.getEspecialidade()).isNull();
    }
}
