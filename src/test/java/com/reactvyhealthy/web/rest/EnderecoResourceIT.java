package com.reactvyhealthy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.reactvyhealthy.IntegrationTest;
import com.reactvyhealthy.domain.Endereco;
import com.reactvyhealthy.repository.EnderecoRepository;
import com.reactvyhealthy.repository.EntityManager;
import com.reactvyhealthy.service.dto.EnderecoDTO;
import com.reactvyhealthy.service.mapper.EnderecoMapper;
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
 * Integration tests for the {@link EnderecoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EnderecoResourceIT {

    private static final String DEFAULT_LOGRADOURO = "AAAAAAAAAA";
    private static final String UPDATED_LOGRADOURO = "BBBBBBBBBB";

    private static final String DEFAULT_BAIRRO = "AAAAAAAAAA";
    private static final String UPDATED_BAIRRO = "BBBBBBBBBB";

    private static final String DEFAULT_CEP = "AAAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_COMPLEMENTO = "AAAAAAAAAA";
    private static final String UPDATED_COMPLEMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_UF = "AAAAAAAAAA";
    private static final String UPDATED_UF = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/enderecos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EnderecoMapper enderecoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Endereco endereco;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Endereco createEntity(EntityManager em) {
        Endereco endereco = new Endereco()
            .logradouro(DEFAULT_LOGRADOURO)
            .bairro(DEFAULT_BAIRRO)
            .cep(DEFAULT_CEP)
            .numero(DEFAULT_NUMERO)
            .complemento(DEFAULT_COMPLEMENTO)
            .cidade(DEFAULT_CIDADE)
            .uf(DEFAULT_UF);
        return endereco;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Endereco createUpdatedEntity(EntityManager em) {
        Endereco endereco = new Endereco()
            .logradouro(UPDATED_LOGRADOURO)
            .bairro(UPDATED_BAIRRO)
            .cep(UPDATED_CEP)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .cidade(UPDATED_CIDADE)
            .uf(UPDATED_UF);
        return endereco;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Endereco.class).block();
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
        endereco = createEntity(em);
    }

    @Test
    void createEndereco() throws Exception {
        int databaseSizeBeforeCreate = enderecoRepository.findAll().collectList().block().size();
        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll().collectList().block();
        assertThat(enderecoList).hasSize(databaseSizeBeforeCreate + 1);
        Endereco testEndereco = enderecoList.get(enderecoList.size() - 1);
        assertThat(testEndereco.getLogradouro()).isEqualTo(DEFAULT_LOGRADOURO);
        assertThat(testEndereco.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testEndereco.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testEndereco.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testEndereco.getComplemento()).isEqualTo(DEFAULT_COMPLEMENTO);
        assertThat(testEndereco.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testEndereco.getUf()).isEqualTo(DEFAULT_UF);
    }

    @Test
    void createEnderecoWithExistingId() throws Exception {
        // Create the Endereco with an existing ID
        endereco.setId(1L);
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        int databaseSizeBeforeCreate = enderecoRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll().collectList().block();
        assertThat(enderecoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkLogradouroIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().collectList().block().size();
        // set the field null
        endereco.setLogradouro(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Endereco> enderecoList = enderecoRepository.findAll().collectList().block();
        assertThat(enderecoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkBairroIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().collectList().block().size();
        // set the field null
        endereco.setBairro(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Endereco> enderecoList = enderecoRepository.findAll().collectList().block();
        assertThat(enderecoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCepIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().collectList().block().size();
        // set the field null
        endereco.setCep(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Endereco> enderecoList = enderecoRepository.findAll().collectList().block();
        assertThat(enderecoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().collectList().block().size();
        // set the field null
        endereco.setNumero(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Endereco> enderecoList = enderecoRepository.findAll().collectList().block();
        assertThat(enderecoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkComplementoIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().collectList().block().size();
        // set the field null
        endereco.setComplemento(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Endereco> enderecoList = enderecoRepository.findAll().collectList().block();
        assertThat(enderecoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().collectList().block().size();
        // set the field null
        endereco.setCidade(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Endereco> enderecoList = enderecoRepository.findAll().collectList().block();
        assertThat(enderecoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUfIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().collectList().block().size();
        // set the field null
        endereco.setUf(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Endereco> enderecoList = enderecoRepository.findAll().collectList().block();
        assertThat(enderecoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllEnderecos() {
        // Initialize the database
        enderecoRepository.save(endereco).block();

        // Get all the enderecoList
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
            .value(hasItem(endereco.getId().intValue()))
            .jsonPath("$.[*].logradouro")
            .value(hasItem(DEFAULT_LOGRADOURO))
            .jsonPath("$.[*].bairro")
            .value(hasItem(DEFAULT_BAIRRO))
            .jsonPath("$.[*].cep")
            .value(hasItem(DEFAULT_CEP))
            .jsonPath("$.[*].numero")
            .value(hasItem(DEFAULT_NUMERO))
            .jsonPath("$.[*].complemento")
            .value(hasItem(DEFAULT_COMPLEMENTO))
            .jsonPath("$.[*].cidade")
            .value(hasItem(DEFAULT_CIDADE))
            .jsonPath("$.[*].uf")
            .value(hasItem(DEFAULT_UF));
    }

    @Test
    void getEndereco() {
        // Initialize the database
        enderecoRepository.save(endereco).block();

        // Get the endereco
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, endereco.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(endereco.getId().intValue()))
            .jsonPath("$.logradouro")
            .value(is(DEFAULT_LOGRADOURO))
            .jsonPath("$.bairro")
            .value(is(DEFAULT_BAIRRO))
            .jsonPath("$.cep")
            .value(is(DEFAULT_CEP))
            .jsonPath("$.numero")
            .value(is(DEFAULT_NUMERO))
            .jsonPath("$.complemento")
            .value(is(DEFAULT_COMPLEMENTO))
            .jsonPath("$.cidade")
            .value(is(DEFAULT_CIDADE))
            .jsonPath("$.uf")
            .value(is(DEFAULT_UF));
    }

    @Test
    void getNonExistingEndereco() {
        // Get the endereco
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEndereco() throws Exception {
        // Initialize the database
        enderecoRepository.save(endereco).block();

        int databaseSizeBeforeUpdate = enderecoRepository.findAll().collectList().block().size();

        // Update the endereco
        Endereco updatedEndereco = enderecoRepository.findById(endereco.getId()).block();
        updatedEndereco
            .logradouro(UPDATED_LOGRADOURO)
            .bairro(UPDATED_BAIRRO)
            .cep(UPDATED_CEP)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .cidade(UPDATED_CIDADE)
            .uf(UPDATED_UF);
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(updatedEndereco);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, enderecoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll().collectList().block();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
        Endereco testEndereco = enderecoList.get(enderecoList.size() - 1);
        assertThat(testEndereco.getLogradouro()).isEqualTo(UPDATED_LOGRADOURO);
        assertThat(testEndereco.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testEndereco.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testEndereco.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testEndereco.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testEndereco.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testEndereco.getUf()).isEqualTo(UPDATED_UF);
    }

    @Test
    void putNonExistingEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().collectList().block().size();
        endereco.setId(longCount.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, enderecoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll().collectList().block();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().collectList().block().size();
        endereco.setId(longCount.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll().collectList().block();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().collectList().block().size();
        endereco.setId(longCount.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll().collectList().block();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEnderecoWithPatch() throws Exception {
        // Initialize the database
        enderecoRepository.save(endereco).block();

        int databaseSizeBeforeUpdate = enderecoRepository.findAll().collectList().block().size();

        // Update the endereco using partial update
        Endereco partialUpdatedEndereco = new Endereco();
        partialUpdatedEndereco.setId(endereco.getId());

        partialUpdatedEndereco.logradouro(UPDATED_LOGRADOURO).numero(UPDATED_NUMERO).complemento(UPDATED_COMPLEMENTO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEndereco.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEndereco))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll().collectList().block();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
        Endereco testEndereco = enderecoList.get(enderecoList.size() - 1);
        assertThat(testEndereco.getLogradouro()).isEqualTo(UPDATED_LOGRADOURO);
        assertThat(testEndereco.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testEndereco.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testEndereco.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testEndereco.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testEndereco.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testEndereco.getUf()).isEqualTo(DEFAULT_UF);
    }

    @Test
    void fullUpdateEnderecoWithPatch() throws Exception {
        // Initialize the database
        enderecoRepository.save(endereco).block();

        int databaseSizeBeforeUpdate = enderecoRepository.findAll().collectList().block().size();

        // Update the endereco using partial update
        Endereco partialUpdatedEndereco = new Endereco();
        partialUpdatedEndereco.setId(endereco.getId());

        partialUpdatedEndereco
            .logradouro(UPDATED_LOGRADOURO)
            .bairro(UPDATED_BAIRRO)
            .cep(UPDATED_CEP)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .cidade(UPDATED_CIDADE)
            .uf(UPDATED_UF);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEndereco.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEndereco))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll().collectList().block();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
        Endereco testEndereco = enderecoList.get(enderecoList.size() - 1);
        assertThat(testEndereco.getLogradouro()).isEqualTo(UPDATED_LOGRADOURO);
        assertThat(testEndereco.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testEndereco.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testEndereco.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testEndereco.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testEndereco.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testEndereco.getUf()).isEqualTo(UPDATED_UF);
    }

    @Test
    void patchNonExistingEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().collectList().block().size();
        endereco.setId(longCount.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, enderecoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll().collectList().block();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().collectList().block().size();
        endereco.setId(longCount.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll().collectList().block();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().collectList().block().size();
        endereco.setId(longCount.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll().collectList().block();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEndereco() {
        // Initialize the database
        enderecoRepository.save(endereco).block();

        int databaseSizeBeforeDelete = enderecoRepository.findAll().collectList().block().size();

        // Delete the endereco
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, endereco.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Endereco> enderecoList = enderecoRepository.findAll().collectList().block();
        assertThat(enderecoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
