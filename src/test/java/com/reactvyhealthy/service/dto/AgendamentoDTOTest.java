package com.reactvyhealthy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.reactvyhealthy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgendamentoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgendamentoDTO.class);
        AgendamentoDTO agendamentoDTO1 = new AgendamentoDTO();
        agendamentoDTO1.setId(1L);
        AgendamentoDTO agendamentoDTO2 = new AgendamentoDTO();
        assertThat(agendamentoDTO1).isNotEqualTo(agendamentoDTO2);
        agendamentoDTO2.setId(agendamentoDTO1.getId());
        assertThat(agendamentoDTO1).isEqualTo(agendamentoDTO2);
        agendamentoDTO2.setId(2L);
        assertThat(agendamentoDTO1).isNotEqualTo(agendamentoDTO2);
        agendamentoDTO1.setId(null);
        assertThat(agendamentoDTO1).isNotEqualTo(agendamentoDTO2);
    }
}
