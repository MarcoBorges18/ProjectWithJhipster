package com.reactvyhealthy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.reactvyhealthy.IntegrationTest;
import com.reactvyhealthy.domain.Medico;
import com.reactvyhealthy.repository.EntityManager;
import com.reactvyhealthy.repository.MedicoRepository;
import com.reactvyhealthy.service.dto.MedicoDTO;
import com.reactvyhealthy.service.mapper.MedicoMapper;
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
 * Integration tests for the {@link MedicoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MedicoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CRM = "AAAAAAAAAA";
    private static final String UPDATED_CRM = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final String ENTITY_API_URL = "/api/medicos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private MedicoMapper medicoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Medico medico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medico createEntity(EntityManager em) {
        Medico medico = new Medico().nome(DEFAULT_NOME).email(DEFAULT_EMAIL).crm(DEFAULT_CRM).ativo(DEFAULT_ATIVO);
        return medico;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medico createUpdatedEntity(EntityManager em) {
        Medico medico = new Medico().nome(UPDATED_NOME).email(UPDATED_EMAIL).crm(UPDATED_CRM).ativo(UPDATED_ATIVO);
        return medico;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Medico.class).block();
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
        medico = createEntity(em);
    }

    @Test
    void createMedico() throws Exception {
        int databaseSizeBeforeCreate = medicoRepository.findAll().collectList().block().size();
        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll().collectList().block();
        assertThat(medicoList).hasSize(databaseSizeBeforeCreate + 1);
        Medico testMedico = medicoList.get(medicoList.size() - 1);
        assertThat(testMedico.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testMedico.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMedico.getCrm()).isEqualTo(DEFAULT_CRM);
        assertThat(testMedico.getAtivo()).isEqualTo(DEFAULT_ATIVO);
    }

    @Test
    void createMedicoWithExistingId() throws Exception {
        // Create the Medico with an existing ID
        medico.setId(1L);
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        int databaseSizeBeforeCreate = medicoRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll().collectList().block();
        assertThat(medicoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicoRepository.findAll().collectList().block().size();
        // set the field null
        medico.setNome(null);

        // Create the Medico, which fails.
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Medico> medicoList = medicoRepository.findAll().collectList().block();
        assertThat(medicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicoRepository.findAll().collectList().block().size();
        // set the field null
        medico.setEmail(null);

        // Create the Medico, which fails.
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Medico> medicoList = medicoRepository.findAll().collectList().block();
        assertThat(medicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCrmIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicoRepository.findAll().collectList().block().size();
        // set the field null
        medico.setCrm(null);

        // Create the Medico, which fails.
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Medico> medicoList = medicoRepository.findAll().collectList().block();
        assertThat(medicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAtivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicoRepository.findAll().collectList().block().size();
        // set the field null
        medico.setAtivo(null);

        // Create the Medico, which fails.
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Medico> medicoList = medicoRepository.findAll().collectList().block();
        assertThat(medicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllMedicos() {
        // Initialize the database
        medicoRepository.save(medico).block();

        // Get all the medicoList
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
            .value(hasItem(medico.getId().intValue()))
            .jsonPath("$.[*].nome")
            .value(hasItem(DEFAULT_NOME))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].crm")
            .value(hasItem(DEFAULT_CRM))
            .jsonPath("$.[*].ativo")
            .value(hasItem(DEFAULT_ATIVO.booleanValue()));
    }

    @Test
    void getMedico() {
        // Initialize the database
        medicoRepository.save(medico).block();

        // Get the medico
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, medico.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(medico.getId().intValue()))
            .jsonPath("$.nome")
            .value(is(DEFAULT_NOME))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.crm")
            .value(is(DEFAULT_CRM))
            .jsonPath("$.ativo")
            .value(is(DEFAULT_ATIVO.booleanValue()));
    }

    @Test
    void getNonExistingMedico() {
        // Get the medico
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMedico() throws Exception {
        // Initialize the database
        medicoRepository.save(medico).block();

        int databaseSizeBeforeUpdate = medicoRepository.findAll().collectList().block().size();

        // Update the medico
        Medico updatedMedico = medicoRepository.findById(medico.getId()).block();
        updatedMedico.nome(UPDATED_NOME).email(UPDATED_EMAIL).crm(UPDATED_CRM).ativo(UPDATED_ATIVO);
        MedicoDTO medicoDTO = medicoMapper.toDto(updatedMedico);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, medicoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll().collectList().block();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
        Medico testMedico = medicoList.get(medicoList.size() - 1);
        assertThat(testMedico.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testMedico.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMedico.getCrm()).isEqualTo(UPDATED_CRM);
        assertThat(testMedico.getAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    void putNonExistingMedico() throws Exception {
        int databaseSizeBeforeUpdate = medicoRepository.findAll().collectList().block().size();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, medicoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll().collectList().block();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMedico() throws Exception {
        int databaseSizeBeforeUpdate = medicoRepository.findAll().collectList().block().size();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll().collectList().block();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMedico() throws Exception {
        int databaseSizeBeforeUpdate = medicoRepository.findAll().collectList().block().size();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll().collectList().block();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMedicoWithPatch() throws Exception {
        // Initialize the database
        medicoRepository.save(medico).block();

        int databaseSizeBeforeUpdate = medicoRepository.findAll().collectList().block().size();

        // Update the medico using partial update
        Medico partialUpdatedMedico = new Medico();
        partialUpdatedMedico.setId(medico.getId());

        partialUpdatedMedico.nome(UPDATED_NOME).crm(UPDATED_CRM);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMedico.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMedico))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll().collectList().block();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
        Medico testMedico = medicoList.get(medicoList.size() - 1);
        assertThat(testMedico.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testMedico.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMedico.getCrm()).isEqualTo(UPDATED_CRM);
        assertThat(testMedico.getAtivo()).isEqualTo(DEFAULT_ATIVO);
    }

    @Test
    void fullUpdateMedicoWithPatch() throws Exception {
        // Initialize the database
        medicoRepository.save(medico).block();

        int databaseSizeBeforeUpdate = medicoRepository.findAll().collectList().block().size();

        // Update the medico using partial update
        Medico partialUpdatedMedico = new Medico();
        partialUpdatedMedico.setId(medico.getId());

        partialUpdatedMedico.nome(UPDATED_NOME).email(UPDATED_EMAIL).crm(UPDATED_CRM).ativo(UPDATED_ATIVO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMedico.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMedico))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll().collectList().block();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
        Medico testMedico = medicoList.get(medicoList.size() - 1);
        assertThat(testMedico.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testMedico.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMedico.getCrm()).isEqualTo(UPDATED_CRM);
        assertThat(testMedico.getAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    void patchNonExistingMedico() throws Exception {
        int databaseSizeBeforeUpdate = medicoRepository.findAll().collectList().block().size();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, medicoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll().collectList().block();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMedico() throws Exception {
        int databaseSizeBeforeUpdate = medicoRepository.findAll().collectList().block().size();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll().collectList().block();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMedico() throws Exception {
        int databaseSizeBeforeUpdate = medicoRepository.findAll().collectList().block().size();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll().collectList().block();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMedico() {
        // Initialize the database
        medicoRepository.save(medico).block();

        int databaseSizeBeforeDelete = medicoRepository.findAll().collectList().block().size();

        // Delete the medico
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, medico.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Medico> medicoList = medicoRepository.findAll().collectList().block();
        assertThat(medicoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
