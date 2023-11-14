package com.reactvyhealthy.domain;

import static com.reactvyhealthy.domain.TipoConsultaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reactvyhealthy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoConsultaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoConsulta.class);
        TipoConsulta tipoConsulta1 = getTipoConsultaSample1();
        TipoConsulta tipoConsulta2 = new TipoConsulta();
        assertThat(tipoConsulta1).isNotEqualTo(tipoConsulta2);

        tipoConsulta2.setId(tipoConsulta1.getId());
        assertThat(tipoConsulta1).isEqualTo(tipoConsulta2);

        tipoConsulta2 = getTipoConsultaSample2();
        assertThat(tipoConsulta1).isNotEqualTo(tipoConsulta2);
    }
}
