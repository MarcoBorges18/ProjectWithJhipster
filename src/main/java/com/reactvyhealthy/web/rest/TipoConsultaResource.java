package com.reactvyhealthy.web.rest;

import com.reactvyhealthy.repository.TipoConsultaRepository;
import com.reactvyhealthy.service.TipoConsultaService;
import com.reactvyhealthy.service.dto.TipoConsultaDTO;
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
 * REST controller for managing {@link com.reactvyhealthy.domain.TipoConsulta}.
 */
@RestController
@RequestMapping("/api/tipo-consultas")
public class TipoConsultaResource {

    private final Logger log = LoggerFactory.getLogger(TipoConsultaResource.class);

    private static final String ENTITY_NAME = "tipoConsulta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoConsultaService tipoConsultaService;

    private final TipoConsultaRepository tipoConsultaRepository;

    public TipoConsultaResource(TipoConsultaService tipoConsultaService, TipoConsultaRepository tipoConsultaRepository) {
        this.tipoConsultaService = tipoConsultaService;
        this.tipoConsultaRepository = tipoConsultaRepository;
    }

    /**
     * {@code POST  /tipo-consultas} : Create a new tipoConsulta.
     *
     * @param tipoConsultaDTO the tipoConsultaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoConsultaDTO, or with status {@code 400 (Bad Request)} if the tipoConsulta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<TipoConsultaDTO>> createTipoConsulta(@Valid @RequestBody TipoConsultaDTO tipoConsultaDTO)
        throws URISyntaxException {
        log.debug("REST request to save TipoConsulta : {}", tipoConsultaDTO);
        if (tipoConsultaDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoConsulta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return tipoConsultaService
            .save(tipoConsultaDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/tipo-consultas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /tipo-consultas/:id} : Updates an existing tipoConsulta.
     *
     * @param id the id of the tipoConsultaDTO to save.
     * @param tipoConsultaDTO the tipoConsultaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoConsultaDTO,
     * or with status {@code 400 (Bad Request)} if the tipoConsultaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoConsultaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<TipoConsultaDTO>> updateTipoConsulta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TipoConsultaDTO tipoConsultaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TipoConsulta : {}, {}", id, tipoConsultaDTO);
        if (tipoConsultaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoConsultaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return tipoConsultaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return tipoConsultaService
                    .update(tipoConsultaDTO)
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
     * {@code PATCH  /tipo-consultas/:id} : Partial updates given fields of an existing tipoConsulta, field will ignore if it is null
     *
     * @param id the id of the tipoConsultaDTO to save.
     * @param tipoConsultaDTO the tipoConsultaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoConsultaDTO,
     * or with status {@code 400 (Bad Request)} if the tipoConsultaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoConsultaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoConsultaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TipoConsultaDTO>> partialUpdateTipoConsulta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TipoConsultaDTO tipoConsultaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TipoConsulta partially : {}, {}", id, tipoConsultaDTO);
        if (tipoConsultaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoConsultaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return tipoConsultaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TipoConsultaDTO> result = tipoConsultaService.partialUpdate(tipoConsultaDTO);

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
     * {@code GET  /tipo-consultas} : get all the tipoConsultas.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoConsultas in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<TipoConsultaDTO>>> getAllTipoConsultas(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of TipoConsultas");
        return tipoConsultaService
            .countAll()
            .zipWith(tipoConsultaService.findAll(pageable).collectList())
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
     * {@code GET  /tipo-consultas/:id} : get the "id" tipoConsulta.
     *
     * @param id the id of the tipoConsultaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoConsultaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TipoConsultaDTO>> getTipoConsulta(@PathVariable Long id) {
        log.debug("REST request to get TipoConsulta : {}", id);
        Mono<TipoConsultaDTO> tipoConsultaDTO = tipoConsultaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoConsultaDTO);
    }

    /**
     * {@code DELETE  /tipo-consultas/:id} : delete the "id" tipoConsulta.
     *
     * @param id the id of the tipoConsultaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTipoConsulta(@PathVariable Long id) {
        log.debug("REST request to delete TipoConsulta : {}", id);
        return tipoConsultaService
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
