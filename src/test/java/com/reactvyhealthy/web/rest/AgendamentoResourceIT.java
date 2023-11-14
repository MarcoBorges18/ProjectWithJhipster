package com.reactvyhealthy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.reactvyhealthy.IntegrationTest;
import com.reactvyhealthy.domain.Agendamento;
import com.reactvyhealthy.repository.AgendamentoRepository;
import com.reactvyhealthy.repository.EntityManager;
import com.reactvyhealthy.service.dto.AgendamentoDTO;
import com.reactvyhealthy.service.mapper.AgendamentoMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link AgendamentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AgendamentoResourceIT {

    private static final Double DEFAULT_VALOR_FINAL = 1D;
    private static final Double UPDATED_VALOR_FINAL = 2D;

    private static final String ENTITY_API_URL = "/api/agendamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private AgendamentoMapper agendamentoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Agendamento agendamento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agendamento createEntity(EntityManager em) {
        Agendamento agendamento = new Agendamento().valorFinal(DEFAULT_VALOR_FINAL);
        return agendamento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agendamento createUpdatedEntity(EntityManager em) {
        Agendamento agendamento = new Agendamento().valorFinal(UPDATED_VALOR_FINAL);
        return agendamento;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Agendamento.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        agendamento = createEntity(em);
    }

    @Test
    void createAgendamento() throws Exception {
        int databaseSizeBeforeCreate = agendamentoRepository.findAll().collectList().block().size();
        // Create the Agendamento
        AgendamentoDTO agendamentoDTO = agendamentoMapper.toDto(agendamento);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agendamentoDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Agendamento in the database
        List<Agendamento> agendamentoList = agendamentoRepository.findAll().collectList().block();
        assertThat(agendamentoList).hasSize(databaseSizeBeforeCreate + 1);
        Agendamento testAgendamento = agendamentoList.get(agendamentoList.size() - 1);
        assertThat(testAgendamento.getValorFinal()).isEqualTo(DEFAULT_VALOR_FINAL);
    }

    @Test
    void createAgendamentoWithExistingId() throws Exception {
        // Create the Agendamento with an existing ID
        agendamento.setId(1L);
        AgendamentoDTO agendamentoDTO = agendamentoMapper.toDto(agendamento);

        int databaseSizeBeforeCreate = agendamentoRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agendamentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Agendamento in the database
        List<Agendamento> agendamentoList = agendamentoRepository.findAll().collectList().block();
        assertThat(agendamentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkValorFinalIsRequired() throws Exception {
        int databaseSizeBeforeTest = agendamentoRepository.findAll().collectList().block().size();
        // set the field null
        agendamento.setValorFinal(null);

        // Create the Agendamento, which fails.
        AgendamentoDTO agendamentoDTO = agendamentoMapper.toDto(agendamento);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agendamentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Agendamento> agendamentoList = agendamentoRepository.findAll().collectList().block();
        assertThat(agendamentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAgendamentos() {
        // Initialize the database
        agendamentoRepository.save(agendamento).block();

        // Get all the agendamentoList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(agendamento.getId().intValue()))
            .jsonPath("$.[*].valorFinal")
            .value(hasItem(DEFAULT_VALOR_FINAL.doubleValue()));
    }

    @Test
    void getAgendamento() {
        // Initialize the database
        agendamentoRepository.save(agendamento).block();

        // Get the agendamento
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, agendamento.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(agendamento.getId().intValue()))
            .jsonPath("$.valorFinal")
            .value(is(DEFAULT_VALOR_FINAL.doubleValue()));
    }

    @Test
    void getNonExistingAgendamento() {
        // Get the agendamento
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAgendamento() throws Exception {
        // Initialize the database
        agendamentoRepository.save(agendamento).block();

        int databaseSizeBeforeUpdate = agendamentoRepository.findAll().collectList().block().size();

        // Update the agendamento
        Agendamento updatedAgendamento = agendamentoRepository.findById(agendamento.getId()).block();
        updatedAgendamento.valorFinal(UPDATED_VALOR_FINAL);
        AgendamentoDTO agendamentoDTO = agendamentoMapper.toDto(updatedAgendamento);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, agendamentoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agendamentoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Agendamento in the database
        List<Agendamento> agendamentoList = agendamentoRepository.findAll().collectList().block();
        assertThat(agendamentoList).hasSize(databaseSizeBeforeUpdate);
        Agendamento testAgendamento = agendamentoList.get(agendamentoList.size() - 1);
        assertThat(testAgendamento.getValorFinal()).isEqualTo(UPDATED_VALOR_FINAL);
    }

    @Test
    void putNonExistingAgendamento() throws Exception {
        int databaseSizeBeforeUpdate = agendamentoRepository.findAll().collectList().block().size();
        agendamento.setId(longCount.incrementAndGet());

        // Create the Agendamento
        AgendamentoDTO agendamentoDTO = agendamentoMapper.toDto(agendamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, agendamentoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agendamentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Agendamento in the database
        List<Agendamento> agendamentoList = agendamentoRepository.findAll().collectList().block();
        assertThat(agendamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAgendamento() throws Exception {
        int databaseSizeBeforeUpdate = agendamentoRepository.findAll().collectList().block().size();
        agendamento.setId(longCount.incrementAndGet());

        // Create the Agendamento
        AgendamentoDTO agendamentoDTO = agendamentoMapper.toDto(agendamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agendamentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Agendamento in the database
        List<Agendamento> agendamentoList = agendamentoRepository.findAll().collectList().block();
        assertThat(agendamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAgendamento() throws Exception {
        int databaseSizeBeforeUpdate = agendamentoRepository.findAll().collectList().block().size();
        agendamento.setId(longCount.incrementAndGet());

        // Create the Agendamento
        AgendamentoDTO agendamentoDTO = agendamentoMapper.toDto(agendamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agendamentoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Agendamento in the database
        List<Agendamento> agendamentoList = agendamentoRepository.findAll().collectList().block();
        assertThat(agendamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAgendamentoWithPatch() throws Exception {
        // Initialize the database
        agendamentoRepository.save(agendamento).block();

        int databaseSizeBeforeUpdate = agendamentoRepository.findAll().collectList().block().size();

        // Update the agendamento using partial update
        Agendamento partialUpdatedAgendamento = new Agendamento();
        partialUpdatedAgendamento.setId(agendamento.getId());

        partialUpdatedAgendamento.valorFinal(UPDATED_VALOR_FINAL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAgendamento.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAgendamento))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Agendamento in the database
        List<Agendamento> agendamentoList = agendamentoRepository.findAll().collectList().block();
        assertThat(agendamentoList).hasSize(databaseSizeBeforeUpdate);
        Agendamento testAgendamento = agendamentoList.get(agendamentoList.size() - 1);
        assertThat(testAgendamento.getValorFinal()).isEqualTo(UPDATED_VALOR_FINAL);
    }

    @Test
    void fullUpdateAgendamentoWithPatch() throws Exception {
        // Initialize the database
        agendamentoRepository.save(agendamento).block();

        int databaseSizeBeforeUpdate = agendamentoRepository.findAll().collectList().block().size();

        // Update the agendamento using partial update
        Agendamento partialUpdatedAgendamento = new Agendamento();
        partialUpdatedAgendamento.setId(agendamento.getId());

        partialUpdatedAgendamento.valorFinal(UPDATED_VALOR_FINAL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAgendamento.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAgendamento))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Agendamento in the database
        List<Agendamento> agendamentoList = agendamentoRepository.findAll().collectList().block();
        assertThat(agendamentoList).hasSize(databaseSizeBeforeUpdate);
        Agendamento testAgendamento = agendamentoList.get(agendamentoList.size() - 1);
        assertThat(testAgendamento.getValorFinal()).isEqualTo(UPDATED_VALOR_FINAL);
    }

    @Test
    void patchNonExistingAgendamento() throws Exception {
        int databaseSizeBeforeUpdate = agendamentoRepository.findAll().collectList().block().size();
        agendamento.setId(longCount.incrementAndGet());

        // Create the Agendamento
        AgendamentoDTO agendamentoDTO = agendamentoMapper.toDto(agendamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, agendamentoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(agendamentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Agendamento in the database
        List<Agendamento> agendamentoList = agendamentoRepository.findAll().collectList().block();
        assertThat(agendamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAgendamento() throws Exception {
        int databaseSizeBeforeUpdate = agendamentoRepository.findAll().collectList().block().size();
        agendamento.setId(longCount.incrementAndGet());

        // Create the Agendamento
        AgendamentoDTO agendamentoDTO = agendamentoMapper.toDto(agendamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(agendamentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Agendamento in the database
        List<Agendamento> agendamentoList = agendamentoRepository.findAll().collectList().block();
        assertThat(agendamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAgendamento() throws Exception {
        int databaseSizeBeforeUpdate = agendamentoRepository.findAll().collectList().block().size();
        agendamento.setId(longCount.incrementAndGet());

        // Create the Agendamento
        AgendamentoDTO agendamentoDTO = agendamentoMapper.toDto(agendamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(agendamentoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Agendamento in the database
        List<Agendamento> agendamentoList = agendamentoRepository.findAll().collectList().block();
        assertThat(agendamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAgendamento() {
        // Initialize the database
        agendamentoRepository.save(agendamento).block();

        int databaseSizeBeforeDelete = agendamentoRepository.findAll().collectList().block().size();

        // Delete the agendamento
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, agendamento.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Agendamento> agendamentoList = agendamentoRepository.findAll().collectList().block();
        assertThat(agendamentoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
