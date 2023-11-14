package com.reactvyhealthy.service;

import com.reactvyhealthy.repository.PlanoRepository;
import com.reactvyhealthy.service.dto.PlanoDTO;
import com.reactvyhealthy.service.mapper.PlanoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.reactvyhealthy.domain.Plano}.
 */
@Service
@Transactional
public class PlanoService {

    private final Logger log = LoggerFactory.getLogger(PlanoService.class);

    private final PlanoRepository planoRepository;

    private final PlanoMapper planoMapper;

    public PlanoService(PlanoRepository planoRepository, PlanoMapper planoMapper) {
        this.planoRepository = planoRepository;
        this.planoMapper = planoMapper;
    }

    /**
     * Save a plano.
     *
     * @param planoDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PlanoDTO> save(PlanoDTO planoDTO) {
        log.debug("Request to save Plano : {}", planoDTO);
        return planoRepository.save(planoMapper.toEntity(planoDTO)).map(planoMapper::toDto);
    }

    /**
     * Update a plano.
     *
     * @param planoDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PlanoDTO> update(PlanoDTO planoDTO) {
        log.debug("Request to update Plano : {}", planoDTO);
        return planoRepository.save(planoMapper.toEntity(planoDTO)).map(planoMapper::toDto);
    }

    /**
     * Partially update a plano.
     *
     * @param planoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PlanoDTO> partialUpdate(PlanoDTO planoDTO) {
        log.debug("Request to partially update Plano : {}", planoDTO);

        return planoRepository
            .findById(planoDTO.getId())
            .map(existingPlano -> {
                planoMapper.partialUpdate(existingPlano, planoDTO);

                return existingPlano;
            })
            .flatMap(planoRepository::save)
            .map(planoMapper::toDto);
    }

    /**
     * Get all the planos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PlanoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Planos");
        return planoRepository.findAllBy(pageable).map(planoMapper::toDto);
    }

    /**
     *  Get all the planos where Paciente is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PlanoDTO> findAllWherePacienteIsNull() {
        log.debug("Request to get all planos where Paciente is null");
        return planoRepository.findAllWherePacienteIsNull().map(planoMapper::toDto);
    }

    /**
     * Returns the number of planos available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return planoRepository.count();
    }

    /**
     * Get one plano by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PlanoDTO> findOne(Long id) {
        log.debug("Request to get Plano : {}", id);
        return planoRepository.findById(id).map(planoMapper::toDto);
    }

    /**
     * Delete the plano by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Plano : {}", id);
        return planoRepository.deleteById(id);
    }
}
