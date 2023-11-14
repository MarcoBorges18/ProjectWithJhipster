package com.reactvyhealthy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.reactvyhealthy.IntegrationTest;
import com.reactvyhealthy.domain.Paciente;
import com.reactvyhealthy.repository.EntityManager;
import com.reactvyhealthy.repository.PacienteRepository;
import com.reactvyhealthy.service.dto.PacienteDTO;
import com.reactvyhealthy.service.mapper.PacienteMapper;
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
 * Integration tests for the {@link PacienteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PacienteResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final String ENTITY_API_URL = "/api/pacientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PacienteMapper pacienteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Paciente paciente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createEntity(EntityManager em) {
        Paciente paciente = new Paciente()
            .nome(DEFAULT_NOME)
            .email(DEFAULT_EMAIL)
            .cpf(DEFAULT_CPF)
            .telefone(DEFAULT_TELEFONE)
            .ativo(DEFAULT_ATIVO);
        return paciente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createUpdatedEntity(EntityManager em) {
        Paciente paciente = new Paciente()
            .nome(UPDATED_NOME)
            .email(UPDATED_EMAIL)
            .cpf(UPDATED_CPF)
            .telefone(UPDATED_TELEFONE)
            .ativo(UPDATED_ATIVO);
        return paciente;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Paciente.class).block();
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
        paciente = createEntity(em);
    }

    @Test
    void createPaciente() throws Exception {
        int databaseSizeBeforeCreate = pacienteRepository.findAll().collectList().block().size();
        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll().collectList().block();
        assertThat(pacienteList).hasSize(databaseSizeBeforeCreate + 1);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPaciente.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPaciente.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testPaciente.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
        assertThat(testPaciente.getAtivo()).isEqualTo(DEFAULT_ATIVO);
    }

    @Test
    void createPacienteWithExistingId() throws Exception {
        // Create the Paciente with an existing ID
        paciente.setId(1L);
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        int databaseSizeBeforeCreate = pacienteRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll().collectList().block();
        assertThat(pacienteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pacienteRepository.findAll().collectList().block().size();
        // set the field null
        paciente.setNome(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Paciente> pacienteList = pacienteRepository.findAll().collectList().block();
        assertThat(pacienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = pacienteRepository.findAll().collectList().block().size();
        // set the field null
        paciente.setEmail(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Paciente> pacienteList = pacienteRepository.findAll().collectList().block();
        assertThat(pacienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAtivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pacienteRepository.findAll().collectList().block().size();
        // set the field null
        paciente.setAtivo(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Paciente> pacienteList = pacienteRepository.findAll().collectList().block();
        assertThat(pacienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPacientes() {
        // Initialize the database
        pacienteRepository.save(paciente).block();

        // Get all the pacienteList
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
            .value(hasItem(paciente.getId().intValue()))
            .jsonPath("$.[*].nome")
            .value(hasItem(DEFAULT_NOME))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].cpf")
            .value(hasItem(DEFAULT_CPF))
            .jsonPath("$.[*].telefone")
            .value(hasItem(DEFAULT_TELEFONE))
            .jsonPath("$.[*].ativo")
            .value(hasItem(DEFAULT_ATIVO.booleanValue()));
    }

    @Test
    void getPaciente() {
        // Initialize the database
        pacienteRepository.save(paciente).block();

        // Get the paciente
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, paciente.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(paciente.getId().intValue()))
            .jsonPath("$.nome")
            .value(is(DEFAULT_NOME))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.cpf")
            .value(is(DEFAULT_CPF))
            .jsonPath("$.telefone")
            .value(is(DEFAULT_TELEFONE))
            .jsonPath("$.ativo")
            .value(is(DEFAULT_ATIVO.booleanValue()));
    }

    @Test
    void getNonExistingPaciente() {
        // Get the paciente
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPaciente() throws Exception {
        // Initialize the database
        pacienteRepository.save(paciente).block();

        int databaseSizeBeforeUpdate = pacienteRepository.findAll().collectList().block().size();

        // Update the paciente
        Paciente updatedPaciente = pacienteRepository.findById(paciente.getId()).block();
        updatedPaciente.nome(UPDATED_NOME).email(UPDATED_EMAIL).cpf(UPDATED_CPF).telefone(UPDATED_TELEFONE).ativo(UPDATED_ATIVO);
        PacienteDTO pacienteDTO = pacienteMapper.toDto(updatedPaciente);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pacienteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll().collectList().block();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPaciente.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPaciente.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testPaciente.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testPaciente.getAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    void putNonExistingPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().collectList().block().size();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pacienteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll().collectList().block();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().collectList().block().size();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll().collectList().block();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().collectList().block().size();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll().collectList().block();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePacienteWithPatch() throws Exception {
        // Initialize the database
        pacienteRepository.save(paciente).block();

        int databaseSizeBeforeUpdate = pacienteRepository.findAll().collectList().block().size();

        // Update the paciente using partial update
        Paciente partialUpdatedPaciente = new Paciente();
        partialUpdatedPaciente.setId(paciente.getId());

        partialUpdatedPaciente.nome(UPDATED_NOME).email(UPDATED_EMAIL).cpf(UPDATED_CPF).telefone(UPDATED_TELEFONE).ativo(UPDATED_ATIVO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPaciente.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPaciente))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll().collectList().block();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPaciente.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPaciente.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testPaciente.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testPaciente.getAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    void fullUpdatePacienteWithPatch() throws Exception {
        // Initialize the database
        pacienteRepository.save(paciente).block();

        int databaseSizeBeforeUpdate = pacienteRepository.findAll().collectList().block().size();

        // Update the paciente using partial update
        Paciente partialUpdatedPaciente = new Paciente();
        partialUpdatedPaciente.setId(paciente.getId());

        partialUpdatedPaciente.nome(UPDATED_NOME).email(UPDATED_EMAIL).cpf(UPDATED_CPF).telefone(UPDATED_TELEFONE).ativo(UPDATED_ATIVO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPaciente.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPaciente))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll().collectList().block();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPaciente.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPaciente.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testPaciente.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testPaciente.getAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    void patchNonExistingPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().collectList().block().size();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, pacienteDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll().collectList().block();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().collectList().block().size();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll().collectList().block();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().collectList().block().size();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll().collectList().block();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePaciente() {
        // Initialize the database
        pacienteRepository.save(paciente).block();

        int databaseSizeBeforeDelete = pacienteRepository.findAll().collectList().block().size();

        // Delete the paciente
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, paciente.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Paciente> pacienteList = pacienteRepository.findAll().collectList().block();
        assertThat(pacienteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
