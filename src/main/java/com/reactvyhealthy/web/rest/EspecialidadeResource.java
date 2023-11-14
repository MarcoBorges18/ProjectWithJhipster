package com.reactvyhealthy.web.rest;

import com.reactvyhealthy.repository.EspecialidadeRepository;
import com.reactvyhealthy.service.EspecialidadeService;
import com.reactvyhealthy.service.dto.EspecialidadeDTO;
import com.reactvyhealthy.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.reactvyhealthy.domain.Especialidade}.
 */
@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadeResource {

    private final Logger log = LoggerFactory.getLogger(EspecialidadeResource.class);

    private static final String ENTITY_NAME = "especialidade";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EspecialidadeService especialidadeService;

    private final EspecialidadeRepository especialidadeRepository;

    public EspecialidadeResource(EspecialidadeService especialidadeService, EspecialidadeRepository especialidadeRepository) {
        this.especialidadeService = especialidadeService;
        this.especialidadeRepository = especialidadeRepository;
    }

    /**
     * {@code POST  /especialidades} : Create a new especialidade.
     *
     * @param especialidadeDTO the especialidadeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new especialidadeDTO, or with status {@code 400 (Bad Request)} if the especialidade has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<EspecialidadeDTO>> createEspecialidade(@Valid @RequestBody EspecialidadeDTO especialidadeDTO)
        throws URISyntaxException {
        log.debug("REST request to save Especialidade : {}", especialidadeDTO);
        if (especialidadeDTO.getId() != null) {
            throw new BadRequestAlertException("A new especialidade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return especialidadeService
            .save(especialidadeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/especialidades/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /especialidades/:id} : Updates an existing especialidade.
     *
     * @param id the id of the especialidadeDTO to save.
     * @param especialidadeDTO the especialidadeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated especialidadeDTO,
     * or with status {@code 400 (Bad Request)} if the especialidadeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the especialidadeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<EspecialidadeDTO>> updateEspecialidade(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EspecialidadeDTO especialidadeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Especialidade : {}, {}", id, especialidadeDTO);
        if (especialidadeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, especialidadeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return especialidadeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return especialidadeService
                    .update(especialidadeDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /especialidades/:id} : Partial updates given fields of an existing especialidade, field will ignore if it is null
     *
     * @param id the id of the especialidadeDTO to save.
     * @param especialidadeDTO the especialidadeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated especialidadeDTO,
     * or with status {@code 400 (Bad Request)} if the especialidadeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the especialidadeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the especialidadeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<EspecialidadeDTO>> partialUpdateEspecialidade(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EspecialidadeDTO especialidadeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Especialidade partially : {}, {}", id, especialidadeDTO);
        if (especialidadeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, especialidadeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return especialidadeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<EspecialidadeDTO> result = especialidadeService.partialUpdate(especialidadeDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /especialidades} : get all the especialidades.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of especialidades in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<EspecialidadeDTO>>> getAllEspecialidades(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false) String filter
    ) {
        if ("medico-is-null".equals(filter)) {
            log.debug("REST request to get all Especialidades where medico is null");
            return especialidadeService.findAllWhereMedicoIsNull().collectList().map(ResponseEntity::ok);
        }
        log.debug("REST request to get a page of Especialidades");
        return especialidadeService
            .countAll()
            .zipWith(especialidadeService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /especialidades/:id} : get the "id" especialidade.
     *
     * @param id the id of the especialidadeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the especialidadeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<EspecialidadeDTO>> getEspecialidade(@PathVariable Long id) {
        log.debug("REST request to get Especialidade : {}", id);
        Mono<EspecialidadeDTO> especialidadeDTO = especialidadeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(especialidadeDTO);
    }

    /**
     * {@code DELETE  /especialidades/:id} : delete the "id" especialidade.
     *
     * @param id the id of the especialidadeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteEspecialidade(@PathVariable Long id) {
        log.debug("REST request to delete Especialidade : {}", id);
        return especialidadeService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
