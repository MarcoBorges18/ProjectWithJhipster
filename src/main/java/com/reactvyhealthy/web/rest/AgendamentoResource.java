package com.reactvyhealthy.web.rest;

import com.reactvyhealthy.repository.AgendamentoRepository;
import com.reactvyhealthy.service.AgendamentoService;
import com.reactvyhealthy.service.dto.AgendamentoDTO;
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
 * REST controller for managing {@link com.reactvyhealthy.domain.Agendamento}.
 */
@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoResource {

    private final Logger log = LoggerFactory.getLogger(AgendamentoResource.class);

    private static final String ENTITY_NAME = "agendamento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgendamentoService agendamentoService;

    private final AgendamentoRepository agendamentoRepository;

    public AgendamentoResource(AgendamentoService agendamentoService, AgendamentoRepository agendamentoRepository) {
        this.agendamentoService = agendamentoService;
        this.agendamentoRepository = agendamentoRepository;
    }

    /**
     * {@code POST  /agendamentos} : Create a new agendamento.
     *
     * @param agendamentoDTO the agendamentoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agendamentoDTO, or with status {@code 400 (Bad Request)} if the agendamento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AgendamentoDTO>> createAgendamento(@Valid @RequestBody AgendamentoDTO agendamentoDTO)
        throws URISyntaxException {
        log.debug("REST request to save Agendamento : {}", agendamentoDTO);
        if (agendamentoDTO.getId() != null) {
            throw new BadRequestAlertException("A new agendamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return agendamentoService
            .save(agendamentoDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/agendamentos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /agendamentos/:id} : Updates an existing agendamento.
     *
     * @param id the id of the agendamentoDTO to save.
     * @param agendamentoDTO the agendamentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agendamentoDTO,
     * or with status {@code 400 (Bad Request)} if the agendamentoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agendamentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AgendamentoDTO>> updateAgendamento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AgendamentoDTO agendamentoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Agendamento : {}, {}", id, agendamentoDTO);
        if (agendamentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agendamentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return agendamentoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return agendamentoService
                    .update(agendamentoDTO)
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
     * {@code PATCH  /agendamentos/:id} : Partial updates given fields of an existing agendamento, field will ignore if it is null
     *
     * @param id the id of the agendamentoDTO to save.
     * @param agendamentoDTO the agendamentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agendamentoDTO,
     * or with status {@code 400 (Bad Request)} if the agendamentoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the agendamentoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the agendamentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AgendamentoDTO>> partialUpdateAgendamento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AgendamentoDTO agendamentoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Agendamento partially : {}, {}", id, agendamentoDTO);
        if (agendamentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agendamentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return agendamentoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AgendamentoDTO> result = agendamentoService.partialUpdate(agendamentoDTO);

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
     * {@code GET  /agendamentos} : get all the agendamentos.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agendamentos in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<AgendamentoDTO>>> getAllAgendamentos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Agendamentos");
        return agendamentoService
            .countAll()
            .zipWith(agendamentoService.findAll(pageable).collectList())
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
     * {@code GET  /agendamentos/:id} : get the "id" agendamento.
     *
     * @param id the id of the agendamentoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agendamentoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AgendamentoDTO>> getAgendamento(@PathVariable Long id) {
        log.debug("REST request to get Agendamento : {}", id);
        Mono<AgendamentoDTO> agendamentoDTO = agendamentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agendamentoDTO);
    }

    /**
     * {@code DELETE  /agendamentos/:id} : delete the "id" agendamento.
     *
     * @param id the id of the agendamentoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAgendamento(@PathVariable Long id) {
        log.debug("REST request to delete Agendamento : {}", id);
        return agendamentoService
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
