package com.reactvyhealthy.domain;

import static com.reactvyhealthy.domain.PacienteTestSamples.*;
import static com.reactvyhealthy.domain.PlanoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reactvyhealthy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlanoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Plano.class);
        Plano plano1 = getPlanoSample1();
        Plano plano2 = new Plano();
        assertThat(plano1).isNotEqualTo(plano2);

        plano2.setId(plano1.getId());
        assertThat(plano1).isEqualTo(plano2);

        plano2 = getPlanoSample2();
        assertThat(plano1).isNotEqualTo(plano2);
    }

    @Test
    void pacienteTest() throws Exception {
        Plano plano = getPlanoRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        plano.setPaciente(pacienteBack);
        assertThat(plano.getPaciente()).isEqualTo(pacienteBack);
        assertThat(pacienteBack.getPlano()).isEqualTo(plano);

        plano.paciente(null);
        assertThat(plano.getPaciente()).isNull();
        assertThat(pacienteBack.getPlano()).isNull();
    }
}
