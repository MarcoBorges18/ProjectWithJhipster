package com.reactvyhealthy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.reactvyhealthy.IntegrationTest;
import com.reactvyhealthy.domain.Especialidade;
import com.reactvyhealthy.repository.EntityManager;
import com.reactvyhealthy.repository.EspecialidadeRepository;
import com.reactvyhealthy.service.dto.EspecialidadeDTO;
import com.reactvyhealthy.service.mapper.EspecialidadeMapper;
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
 * Integration tests for the {@link EspecialidadeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EspecialidadeResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/especialidades";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Autowired
    private EspecialidadeMapper especialidadeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Especialidade especialidade;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Especialidade createEntity(EntityManager em) {
        Especialidade especialidade = new Especialidade().nome(DEFAULT_NOME);
        return especialidade;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Especialidade createUpdatedEntity(EntityManager em) {
        Especialidade especialidade = new Especialidade().nome(UPDATED_NOME);
        return especialidade;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Especialidade.class).block();
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
        especialidade = createEntity(em);
    }

    @Test
    void createEspecialidade() throws Exception {
        int databaseSizeBeforeCreate = especialidadeRepository.findAll().collectList().block().size();
        // Create the Especialidade
        EspecialidadeDTO especialidadeDTO = especialidadeMapper.toDto(especialidade);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(especialidadeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll().collectList().block();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeCreate + 1);
        Especialidade testEspecialidade = especialidadeList.get(especialidadeList.size() - 1);
        assertThat(testEspecialidade.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    void createEspecialidadeWithExistingId() throws Exception {
        // Create the Especialidade with an existing ID
        especialidade.setId(1L);
        EspecialidadeDTO especialidadeDTO = especialidadeMapper.toDto(especialidade);

        int databaseSizeBeforeCreate = especialidadeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(especialidadeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll().collectList().block();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = especialidadeRepository.findAll().collectList().block().size();
        // set the field null
        especialidade.setNome(null);

        // Create the Especialidade, which fails.
        EspecialidadeDTO especialidadeDTO = especialidadeMapper.toDto(especialidade);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(especialidadeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Especialidade> especialidadeList = especialidadeRepository.findAll().collectList().block();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllEspecialidades() {
        // Initialize the database
        especialidadeRepository.save(especialidade).block();

        // Get all the especialidadeList
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
            .value(hasItem(especialidade.getId().intValue()))
            .jsonPath("$.[*].nome")
            .value(hasItem(DEFAULT_NOME));
    }

    @Test
    void getEspecialidade() {
        // Initialize the database
        especialidadeRepository.save(especialidade).block();

        // Get the especialidade
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, especialidade.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(especialidade.getId().intValue()))
            .jsonPath("$.nome")
            .value(is(DEFAULT_NOME));
    }

    @Test
    void getNonExistingEspecialidade() {
        // Get the especialidade
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEspecialidade() throws Exception {
        // Initialize the database
        especialidadeRepository.save(especialidade).block();

        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().collectList().block().size();

        // Update the especialidade
        Especialidade updatedEspecialidade = especialidadeRepository.findById(especialidade.getId()).block();
        updatedEspecialidade.nome(UPDATED_NOME);
        EspecialidadeDTO especialidadeDTO = especialidadeMapper.toDto(updatedEspecialidade);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, especialidadeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(especialidadeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll().collectList().block();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
        Especialidade testEspecialidade = especialidadeList.get(especialidadeList.size() - 1);
        assertThat(testEspecialidade.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    void putNonExistingEspecialidade() throws Exception {
        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().collectList().block().size();
        especialidade.setId(longCount.incrementAndGet());

        // Create the Especialidade
        EspecialidadeDTO especialidadeDTO = especialidadeMapper.toDto(especialidade);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, especialidadeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(especialidadeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll().collectList().block();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEspecialidade() throws Exception {
        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().collectList().block().size();
        especialidade.setId(longCount.incrementAndGet());

        // Create the Especialidade
        EspecialidadeDTO especialidadeDTO = especialidadeMapper.toDto(especialidade);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(especialidadeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll().collectList().block();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEspecialidade() throws Exception {
        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().collectList().block().size();
        especialidade.setId(longCount.incrementAndGet());

        // Create the Especialidade
        EspecialidadeDTO especialidadeDTO = especialidadeMapper.toDto(especialidade);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(especialidadeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll().collectList().block();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEspecialidadeWithPatch() throws Exception {
        // Initialize the database
        especialidadeRepository.save(especialidade).block();

        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().collectList().block().size();

        // Update the especialidade using partial update
        Especialidade partialUpdatedEspecialidade = new Especialidade();
        partialUpdatedEspecialidade.setId(especialidade.getId());

        partialUpdatedEspecialidade.nome(UPDATED_NOME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEspecialidade.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEspecialidade))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll().collectList().block();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
        Especialidade testEspecialidade = especialidadeList.get(especialidadeList.size() - 1);
        assertThat(testEspecialidade.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    void fullUpdateEspecialidadeWithPatch() throws Exception {
        // Initialize the database
        especialidadeRepository.save(especialidade).block();

        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().collectList().block().size();

        // Update the especialidade using partial update
        Especialidade partialUpdatedEspecialidade = new Especialidade();
        partialUpdatedEspecialidade.setId(especialidade.getId());

        partialUpdatedEspecialidade.nome(UPDATED_NOME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEspecialidade.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEspecialidade))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll().collectList().block();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
        Especialidade testEspecialidade = especialidadeList.get(especialidadeList.size() - 1);
        assertThat(testEspecialidade.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    void patchNonExistingEspecialidade() throws Exception {
        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().collectList().block().size();
        especialidade.setId(longCount.incrementAndGet());

        // Create the Especialidade
        EspecialidadeDTO especialidadeDTO = especialidadeMapper.toDto(especialidade);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, especialidadeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(especialidadeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll().collectList().block();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEspecialidade() throws Exception {
        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().collectList().block().size();
        especialidade.setId(longCount.incrementAndGet());

        // Create the Especialidade
        EspecialidadeDTO especialidadeDTO = especialidadeMapper.toDto(especialidade);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(especialidadeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll().collectList().block();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEspecialidade() throws Exception {
        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().collectList().block().size();
        especialidade.setId(longCount.incrementAndGet());

        // Create the Especialidade
        EspecialidadeDTO especialidadeDTO = especialidadeMapper.toDto(especialidade);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(especialidadeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll().collectList().block();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEspecialidade() {
        // Initialize the database
        especialidadeRepository.save(especialidade).block();

        int databaseSizeBeforeDelete = especialidadeRepository.findAll().collectList().block().size();

        // Delete the especialidade
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, especialidade.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Especialidade> especialidadeList = especialidadeRepository.findAll().collectList().block();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
