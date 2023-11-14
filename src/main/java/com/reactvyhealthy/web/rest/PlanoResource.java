package com.reactvyhealthy.web.rest;

import com.reactvyhealthy.repository.PlanoRepository;
import com.reactvyhealthy.service.PlanoService;
import com.reactvyhealthy.service.dto.PlanoDTO;
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
 * REST controller for managing {@link com.reactvyhealthy.domain.Plano}.
 */
@RestController
@RequestMapping("/api/planos")
public class PlanoResource {

    private final Logger log = LoggerFactory.getLogger(PlanoResource.class);

    private static final String ENTITY_NAME = "plano";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanoService planoService;

    private final PlanoRepository planoRepository;

    public PlanoResource(PlanoService planoService, PlanoRepository planoRepository) {
        this.planoService = planoService;
        this.planoRepository = planoRepository;
    }

    /**
     * {@code POST  /planos} : Create a new plano.
     *
     * @param planoDTO the planoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planoDTO, or with status {@code 400 (Bad Request)} if the plano has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PlanoDTO>> createPlano(@Valid @RequestBody PlanoDTO planoDTO) throws URISyntaxException {
        log.debug("REST request to save Plano : {}", planoDTO);
        if (planoDTO.getId() != null) {
            throw new BadRequestAlertException("A new plano cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return planoService
            .save(planoDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/planos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /planos/:id} : Updates an existing plano.
     *
     * @param id the id of the planoDTO to save.
     * @param planoDTO the planoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planoDTO,
     * or with status {@code 400 (Bad Request)} if the planoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PlanoDTO>> updatePlano(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlanoDTO planoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Plano : {}, {}", id, planoDTO);
        if (planoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return planoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return planoService
                    .update(planoDTO)
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
     * {@code PATCH  /planos/:id} : Partial updates given fields of an existing plano, field will ignore if it is null
     *
     * @param id the id of the planoDTO to save.
     * @param planoDTO the planoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planoDTO,
     * or with status {@code 400 (Bad Request)} if the planoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the planoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the planoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PlanoDTO>> partialUpdatePlano(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlanoDTO planoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Plano partially : {}, {}", id, planoDTO);
        if (planoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return planoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PlanoDTO> result = planoService.partialUpdate(planoDTO);

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
     * {@code GET  /planos} : get all the planos.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of planos in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<PlanoDTO>>> getAllPlanos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false) String filter
    ) {
        if ("paciente-is-null".equals(filter)) {
            log.debug("REST request to get all Planos where paciente is null");
            return planoService.findAllWherePacienteIsNull().collectList().map(ResponseEntity::ok);
        }
        log.debug("REST request to get a page of Planos");
        return planoService
            .countAll()
            .zipWith(planoService.findAll(pageable).collectList())
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
     * {@code GET  /planos/:id} : get the "id" plano.
     *
     * @param id the id of the planoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PlanoDTO>> getPlano(@PathVariable Long id) {
        log.debug("REST request to get Plano : {}", id);
        Mono<PlanoDTO> planoDTO = planoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planoDTO);
    }

    /**
     * {@code DELETE  /planos/:id} : delete the "id" plano.
     *
     * @param id the id of the planoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePlano(@PathVariable Long id) {
        log.debug("REST request to delete Plano : {}", id);
        return planoService
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
