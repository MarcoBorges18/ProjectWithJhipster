package com.reactvyhealthy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.reactvyhealthy.IntegrationTest;
import com.reactvyhealthy.domain.TipoConsulta;
import com.reactvyhealthy.repository.EntityManager;
import com.reactvyhealthy.repository.TipoConsultaRepository;
import com.reactvyhealthy.service.dto.TipoConsultaDTO;
import com.reactvyhealthy.service.mapper.TipoConsultaMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link TipoConsultaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TipoConsultaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TEMPO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TEMPO = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_VALOR_CONSULTA = 1D;
    private static final Double UPDATED_VALOR_CONSULTA = 2D;

    private static final String ENTITY_API_URL = "/api/tipo-consultas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoConsultaRepository tipoConsultaRepository;

    @Autowired
    private TipoConsultaMapper tipoConsultaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TipoConsulta tipoConsulta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoConsulta createEntity(EntityManager em) {
        TipoConsulta tipoConsulta = new TipoConsulta().nome(DEFAULT_NOME).tempo(DEFAULT_TEMPO).valorConsulta(DEFAULT_VALOR_CONSULTA);
        return tipoConsulta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoConsulta createUpdatedEntity(EntityManager em) {
        TipoConsulta tipoConsulta = new TipoConsulta().nome(UPDATED_NOME).tempo(UPDATED_TEMPO).valorConsulta(UPDATED_VALOR_CONSULTA);
        return tipoConsulta;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TipoConsulta.class).block();
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
        tipoConsulta = createEntity(em);
    }

    @Test
    void createTipoConsulta() throws Exception {
        int databaseSizeBeforeCreate = tipoConsultaRepository.findAll().collectList().block().size();
        // Create the TipoConsulta
        TipoConsultaDTO tipoConsultaDTO = tipoConsultaMapper.toDto(tipoConsulta);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tipoConsultaDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TipoConsulta in the database
        List<TipoConsulta> tipoConsultaList = tipoConsultaRepository.findAll().collectList().block();
        assertThat(tipoConsultaList).hasSize(databaseSizeBeforeCreate + 1);
        TipoConsulta testTipoConsulta = tipoConsultaList.get(tipoConsultaList.size() - 1);
        assertThat(testTipoConsulta.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTipoConsulta.getTempo()).isEqualTo(DEFAULT_TEMPO);
        assertThat(testTipoConsulta.getValorConsulta()).isEqualTo(DEFAULT_VALOR_CONSULTA);
    }

    @Test
    void createTipoConsultaWithExistingId() throws Exception {
        // Create the TipoConsulta with an existing ID
        tipoConsulta.setId(1L);
        TipoConsultaDTO tipoConsultaDTO = tipoConsultaMapper.toDto(tipoConsulta);

        int databaseSizeBeforeCreate = tipoConsultaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tipoConsultaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TipoConsulta in the database
        List<TipoConsulta> tipoConsultaList = tipoConsultaRepository.findAll().collectList().block();
        assertThat(tipoConsultaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoConsultaRepository.findAll().collectList().block().size();
        // set the field null
        tipoConsulta.setNome(null);

        // Create the TipoConsulta, which fails.
        TipoConsultaDTO tipoConsultaDTO = tipoConsultaMapper.toDto(tipoConsulta);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tipoConsultaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TipoConsulta> tipoConsultaList = tipoConsultaRepository.findAll().collectList().block();
        assertThat(tipoConsultaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTempoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoConsultaRepository.findAll().collectList().block().size();
        // set the field null
        tipoConsulta.setTempo(null);

        // Create the TipoConsulta, which fails.
        TipoConsultaDTO tipoConsultaDTO = tipoConsultaMapper.toDto(tipoConsulta);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tipoConsultaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TipoConsulta> tipoConsultaList = tipoConsultaRepository.findAll().collectList().block();
        assertThat(tipoConsultaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkValorConsultaIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoConsultaRepository.findAll().collectList().block().size();
        // set the field null
        tipoConsulta.setValorConsulta(null);

        // Create the TipoConsulta, which fails.
        TipoConsultaDTO tipoConsultaDTO = tipoConsultaMapper.toDto(tipoConsulta);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tipoConsultaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TipoConsulta> tipoConsultaList = tipoConsultaRepository.findAll().collectList().block();
        assertThat(tipoConsultaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTipoConsultas() {
        // Initialize the database
        tipoConsultaRepository.save(tipoConsulta).block();

        // Get all the tipoConsultaList
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
            .value(hasItem(tipoConsulta.getId().intValue()))
            .jsonPath("$.[*].nome")
            .value(hasItem(DEFAULT_NOME))
            .jsonPath("$.[*].tempo")
            .value(hasItem(DEFAULT_TEMPO.toString()))
            .jsonPath("$.[*].valorConsulta")
            .value(hasItem(DEFAULT_VALOR_CONSULTA.doubleValue()));
    }

    @Test
    void getTipoConsulta() {
        // Initialize the database
        tipoConsultaRepository.save(tipoConsulta).block();

        // Get the tipoConsulta
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, tipoConsulta.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(tipoConsulta.getId().intValue()))
            .jsonPath("$.nome")
            .value(is(DEFAULT_NOME))
            .jsonPath("$.tempo")
            .value(is(DEFAULT_TEMPO.toString()))
            .jsonPath("$.valorConsulta")
            .value(is(DEFAULT_VALOR_CONSULTA.doubleValue()));
    }

    @Test
    void getNonExistingTipoConsulta() {
        // Get the tipoConsulta
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTipoConsulta() throws Exception {
        // Initialize the database
        tipoConsultaRepository.save(tipoConsulta).block();

        int databaseSizeBeforeUpdate = tipoConsultaRepository.findAll().collectList().block().size();

        // Update the tipoConsulta
        TipoConsulta updatedTipoConsulta = tipoConsultaRepository.findById(tipoConsulta.getId()).block();
        updatedTipoConsulta.nome(UPDATED_NOME).tempo(UPDATED_TEMPO).valorConsulta(UPDATED_VALOR_CONSULTA);
        TipoConsultaDTO tipoConsultaDTO = tipoConsultaMapper.toDto(updatedTipoConsulta);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tipoConsultaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tipoConsultaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TipoConsulta in the database
        List<TipoConsulta> tipoConsultaList = tipoConsultaRepository.findAll().collectList().block();
        assertThat(tipoConsultaList).hasSize(databaseSizeBeforeUpdate);
        TipoConsulta testTipoConsulta = tipoConsultaList.get(tipoConsultaList.size() - 1);
        assertThat(testTipoConsulta.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTipoConsulta.getTempo()).isEqualTo(UPDATED_TEMPO);
        assertThat(testTipoConsulta.getValorConsulta()).isEqualTo(UPDATED_VALOR_CONSULTA);
    }

    @Test
    void putNonExistingTipoConsulta() throws Exception {
        int databaseSizeBeforeUpdate = tipoConsultaRepository.findAll().collectList().block().size();
        tipoConsulta.setId(longCount.incrementAndGet());

        // Create the TipoConsulta
        TipoConsultaDTO tipoConsultaDTO = tipoConsultaMapper.toDto(tipoConsulta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tipoConsultaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tipoConsultaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TipoConsulta in the database
        List<TipoConsulta> tipoConsultaList = tipoConsultaRepository.findAll().collectList().block();
        assertThat(tipoConsultaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTipoConsulta() throws Exception {
        int databaseSizeBeforeUpdate = tipoConsultaRepository.findAll().collectList().block().size();
        tipoConsulta.setId(longCount.incrementAndGet());

        // Create the TipoConsulta
        TipoConsultaDTO tipoConsultaDTO = tipoConsultaMapper.toDto(tipoConsulta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tipoConsultaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TipoConsulta in the database
        List<TipoConsulta> tipoConsultaList = tipoConsultaRepository.findAll().collectList().block();
        assertThat(tipoConsultaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTipoConsulta() throws Exception {
        int databaseSizeBeforeUpdate = tipoConsultaRepository.findAll().collectList().block().size();
        tipoConsulta.setId(longCount.incrementAndGet());

        // Create the TipoConsulta
        TipoConsultaDTO tipoConsultaDTO = tipoConsultaMapper.toDto(tipoConsulta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tipoConsultaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TipoConsulta in the database
        List<TipoConsulta> tipoConsultaList = tipoConsultaRepository.findAll().collectList().block();
        assertThat(tipoConsultaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTipoConsultaWithPatch() throws Exception {
        // Initialize the database
        tipoConsultaRepository.save(tipoConsulta).block();

        int databaseSizeBeforeUpdate = tipoConsultaRepository.findAll().collectList().block().size();

        // Update the tipoConsulta using partial update
        TipoConsulta partialUpdatedTipoConsulta = new TipoConsulta();
        partialUpdatedTipoConsulta.setId(tipoConsulta.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTipoConsulta.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoConsulta))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TipoConsulta in the database
        List<TipoConsulta> tipoConsultaList = tipoConsultaRepository.findAll().collectList().block();
        assertThat(tipoConsultaList).hasSize(databaseSizeBeforeUpdate);
        TipoConsulta testTipoConsulta = tipoConsultaList.get(tipoConsultaList.size() - 1);
        assertThat(testTipoConsulta.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTipoConsulta.getTempo()).isEqualTo(DEFAULT_TEMPO);
        assertThat(testTipoConsulta.getValorConsulta()).isEqualTo(DEFAULT_VALOR_CONSULTA);
    }

    @Test
    void fullUpdateTipoConsultaWithPatch() throws Exception {
        // Initialize the database
        tipoConsultaRepository.save(tipoConsulta).block();

        int databaseSizeBeforeUpdate = tipoConsultaRepository.findAll().collectList().block().size();

        // Update the tipoConsulta using partial update
        TipoConsulta partialUpdatedTipoConsulta = new TipoConsulta();
        partialUpdatedTipoConsulta.setId(tipoConsulta.getId());

        partialUpdatedTipoConsulta.nome(UPDATED_NOME).tempo(UPDATED_TEMPO).valorConsulta(UPDATED_VALOR_CONSULTA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTipoConsulta.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoConsulta))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TipoConsulta in the database
        List<TipoConsulta> tipoConsultaList = tipoConsultaRepository.findAll().collectList().block();
        assertThat(tipoConsultaList).hasSize(databaseSizeBeforeUpdate);
        TipoConsulta testTipoConsulta = tipoConsultaList.get(tipoConsultaList.size() - 1);
        assertThat(testTipoConsulta.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTipoConsulta.getTempo()).isEqualTo(UPDATED_TEMPO);
        assertThat(testTipoConsulta.getValorConsulta()).isEqualTo(UPDATED_VALOR_CONSULTA);
    }

    @Test
    void patchNonExistingTipoConsulta() throws Exception {
        int databaseSizeBeforeUpdate = tipoConsultaRepository.findAll().collectList().block().size();
        tipoConsulta.setId(longCount.incrementAndGet());

        // Create the TipoConsulta
        TipoConsultaDTO tipoConsultaDTO = tipoConsultaMapper.toDto(tipoConsulta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, tipoConsultaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tipoConsultaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TipoConsulta in the database
        List<TipoConsulta> tipoConsultaList = tipoConsultaRepository.findAll().collectList().block();
        assertThat(tipoConsultaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTipoConsulta() throws Exception {
        int databaseSizeBeforeUpdate = tipoConsultaRepository.findAll().collectList().block().size();
        tipoConsulta.setId(longCount.incrementAndGet());

        // Create the TipoConsulta
        TipoConsultaDTO tipoConsultaDTO = tipoConsultaMapper.toDto(tipoConsulta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tipoConsultaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TipoConsulta in the database
        List<TipoConsulta> tipoConsultaList = tipoConsultaRepository.findAll().collectList().block();
        assertThat(tipoConsultaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTipoConsulta() throws Exception {
        int databaseSizeBeforeUpdate = tipoConsultaRepository.findAll().collectList().block().size();
        tipoConsulta.setId(longCount.incrementAndGet());

        // Create the TipoConsulta
        TipoConsultaDTO tipoConsultaDTO = tipoConsultaMapper.toDto(tipoConsulta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tipoConsultaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TipoConsulta in the database
        List<TipoConsulta> tipoConsultaList = tipoConsultaRepository.findAll().collectList().block();
        assertThat(tipoConsultaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTipoConsulta() {
        // Initialize the database
        tipoConsultaRepository.save(tipoConsulta).block();

        int databaseSizeBeforeDelete = tipoConsultaRepository.findAll().collectList().block().size();

        // Delete the tipoConsulta
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, tipoConsulta.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TipoConsulta> tipoConsultaList = tipoConsultaRepository.findAll().collectList().block();
        assertThat(tipoConsultaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
