package com.reactvyhealthy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.reactvyhealthy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EspecialidadeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EspecialidadeDTO.class);
        EspecialidadeDTO especialidadeDTO1 = new EspecialidadeDTO();
        especialidadeDTO1.setId(1L);
        EspecialidadeDTO especialidadeDTO2 = new EspecialidadeDTO();
        assertThat(especialidadeDTO1).isNotEqualTo(especialidadeDTO2);
        especialidadeDTO2.setId(especialidadeDTO1.getId());
        assertThat(especialidadeDTO1).isEqualTo(especialidadeDTO2);
        especialidadeDTO2.setId(2L);
        assertThat(especialidadeDTO1).isNotEqualTo(especialidadeDTO2);
        especialidadeDTO1.setId(null);
        assertThat(especialidadeDTO1).isNotEqualTo(especialidadeDTO2);
    }
}
