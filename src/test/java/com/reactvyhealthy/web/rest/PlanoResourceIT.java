package com.reactvyhealthy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.reactvyhealthy.IntegrationTest;
import com.reactvyhealthy.domain.Plano;
import com.reactvyhealthy.repository.EntityManager;
import com.reactvyhealthy.repository.PlanoRepository;
import com.reactvyhealthy.service.dto.PlanoDTO;
import com.reactvyhealthy.service.mapper.PlanoMapper;
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
 * Integration tests for the {@link PlanoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PlanoResourceIT {

    private static final String DEFAULT_CONVENIO = "AAAAAAAAAA";
    private static final String UPDATED_CONVENIO = "BBBBBBBBBB";

    private static final Float DEFAULT_DESCONTO = 1F;
    private static final Float UPDATED_DESCONTO = 2F;

    private static final String ENTITY_API_URL = "/api/planos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlanoRepository planoRepository;

    @Autowired
    private PlanoMapper planoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Plano plano;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plano createEntity(EntityManager em) {
        Plano plano = new Plano().convenio(DEFAULT_CONVENIO).desconto(DEFAULT_DESCONTO);
        return plano;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plano createUpdatedEntity(EntityManager em) {
        Plano plano = new Plano().convenio(UPDATED_CONVENIO).desconto(UPDATED_DESCONTO);
        return plano;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Plano.class).block();
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
        plano = createEntity(em);
    }

    @Test
    void createPlano() throws Exception {
        int databaseSizeBeforeCreate = planoRepository.findAll().collectList().block().size();
        // Create the Plano
        PlanoDTO planoDTO = planoMapper.toDto(plano);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(planoDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll().collectList().block();
        assertThat(planoList).hasSize(databaseSizeBeforeCreate + 1);
        Plano testPlano = planoList.get(planoList.size() - 1);
        assertThat(testPlano.getConvenio()).isEqualTo(DEFAULT_CONVENIO);
        assertThat(testPlano.getDesconto()).isEqualTo(DEFAULT_DESCONTO);
    }

    @Test
    void createPlanoWithExistingId() throws Exception {
        // Create the Plano with an existing ID
        plano.setId(1L);
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        int databaseSizeBeforeCreate = planoRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(planoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll().collectList().block();
        assertThat(planoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkConvenioIsRequired() throws Exception {
        int databaseSizeBeforeTest = planoRepository.findAll().collectList().block().size();
        // set the field null
        plano.setConvenio(null);

        // Create the Plano, which fails.
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(planoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Plano> planoList = planoRepository.findAll().collectList().block();
        assertThat(planoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDescontoIsRequired() throws Exception {
        int databaseSizeBeforeTest = planoRepository.findAll().collectList().block().size();
        // set the field null
        plano.setDesconto(null);

        // Create the Plano, which fails.
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(planoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Plano> planoList = planoRepository.findAll().collectList().block();
        assertThat(planoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPlanos() {
        // Initialize the database
        planoRepository.save(plano).block();

        // Get all the planoList
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
            .value(hasItem(plano.getId().intValue()))
            .jsonPath("$.[*].convenio")
            .value(hasItem(DEFAULT_CONVENIO))
            .jsonPath("$.[*].desconto")
            .value(hasItem(DEFAULT_DESCONTO.doubleValue()));
    }

    @Test
    void getPlano() {
        // Initialize the database
        planoRepository.save(plano).block();

        // Get the plano
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, plano.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(plano.getId().intValue()))
            .jsonPath("$.convenio")
            .value(is(DEFAULT_CONVENIO))
            .jsonPath("$.desconto")
            .value(is(DEFAULT_DESCONTO.doubleValue()));
    }

    @Test
    void getNonExistingPlano() {
        // Get the plano
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPlano() throws Exception {
        // Initialize the database
        planoRepository.save(plano).block();

        int databaseSizeBeforeUpdate = planoRepository.findAll().collectList().block().size();

        // Update the plano
        Plano updatedPlano = planoRepository.findById(plano.getId()).block();
        updatedPlano.convenio(UPDATED_CONVENIO).desconto(UPDATED_DESCONTO);
        PlanoDTO planoDTO = planoMapper.toDto(updatedPlano);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, planoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(planoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll().collectList().block();
        assertThat(planoList).hasSize(databaseSizeBeforeUpdate);
        Plano testPlano = planoList.get(planoList.size() - 1);
        assertThat(testPlano.getConvenio()).isEqualTo(UPDATED_CONVENIO);
        assertThat(testPlano.getDesconto()).isEqualTo(UPDATED_DESCONTO);
    }

    @Test
    void putNonExistingPlano() throws Exception {
        int databaseSizeBeforeUpdate = planoRepository.findAll().collectList().block().size();
        plano.setId(longCount.incrementAndGet());

        // Create the Plano
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, planoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(planoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll().collectList().block();
        assertThat(planoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPlano() throws Exception {
        int databaseSizeBeforeUpdate = planoRepository.findAll().collectList().block().size();
        plano.setId(longCount.incrementAndGet());

        // Create the Plano
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(planoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll().collectList().block();
        assertThat(planoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPlano() throws Exception {
        int databaseSizeBeforeUpdate = planoRepository.findAll().collectList().block().size();
        plano.setId(longCount.incrementAndGet());

        // Create the Plano
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(planoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll().collectList().block();
        assertThat(planoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePlanoWithPatch() throws Exception {
        // Initialize the database
        planoRepository.save(plano).block();

        int databaseSizeBeforeUpdate = planoRepository.findAll().collectList().block().size();

        // Update the plano using partial update
        Plano partialUpdatedPlano = new Plano();
        partialUpdatedPlano.setId(plano.getId());

        partialUpdatedPlano.convenio(UPDATED_CONVENIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPlano.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPlano))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll().collectList().block();
        assertThat(planoList).hasSize(databaseSizeBeforeUpdate);
        Plano testPlano = planoList.get(planoList.size() - 1);
        assertThat(testPlano.getConvenio()).isEqualTo(UPDATED_CONVENIO);
        assertThat(testPlano.getDesconto()).isEqualTo(DEFAULT_DESCONTO);
    }

    @Test
    void fullUpdatePlanoWithPatch() throws Exception {
        // Initialize the database
        planoRepository.save(plano).block();

        int databaseSizeBeforeUpdate = planoRepository.findAll().collectList().block().size();

        // Update the plano using partial update
        Plano partialUpdatedPlano = new Plano();
        partialUpdatedPlano.setId(plano.getId());

        partialUpdatedPlano.convenio(UPDATED_CONVENIO).desconto(UPDATED_DESCONTO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPlano.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPlano))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll().collectList().block();
        assertThat(planoList).hasSize(databaseSizeBeforeUpdate);
        Plano testPlano = planoList.get(planoList.size() - 1);
        assertThat(testPlano.getConvenio()).isEqualTo(UPDATED_CONVENIO);
        assertThat(testPlano.getDesconto()).isEqualTo(UPDATED_DESCONTO);
    }

    @Test
    void patchNonExistingPlano() throws Exception {
        int databaseSizeBeforeUpdate = planoRepository.findAll().collectList().block().size();
        plano.setId(longCount.incrementAndGet());

        // Create the Plano
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, planoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(planoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll().collectList().block();
        assertThat(planoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPlano() throws Exception {
        int databaseSizeBeforeUpdate = planoRepository.findAll().collectList().block().size();
        plano.setId(longCount.incrementAndGet());

        // Create the Plano
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(planoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll().collectList().block();
        assertThat(planoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPlano() throws Exception {
        int databaseSizeBeforeUpdate = planoRepository.findAll().collectList().block().size();
        plano.setId(longCount.incrementAndGet());

        // Create the Plano
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(planoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll().collectList().block();
        assertThat(planoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePlano() {
        // Initialize the database
        planoRepository.save(plano).block();

        int databaseSizeBeforeDelete = planoRepository.findAll().collectList().block().size();

        // Delete the plano
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, plano.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Plano> planoList = planoRepository.findAll().collectList().block();
        assertThat(planoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
