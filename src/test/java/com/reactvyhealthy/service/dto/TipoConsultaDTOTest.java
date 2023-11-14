package com.reactvyhealthy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.reactvyhealthy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoConsultaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoConsultaDTO.class);
        TipoConsultaDTO tipoConsultaDTO1 = new TipoConsultaDTO();
        tipoConsultaDTO1.setId(1L);
        TipoConsultaDTO tipoConsultaDTO2 = new TipoConsultaDTO();
        assertThat(tipoConsultaDTO1).isNotEqualTo(tipoConsultaDTO2);
        tipoConsultaDTO2.setId(tipoConsultaDTO1.getId());
        assertThat(tipoConsultaDTO1).isEqualTo(tipoConsultaDTO2);
        tipoConsultaDTO2.setId(2L);
        assertThat(tipoConsultaDTO1).isNotEqualTo(tipoConsultaDTO2);
        tipoConsultaDTO1.setId(null);
        assertThat(tipoConsultaDTO1).isNotEqualTo(tipoConsultaDTO2);
    }
}
