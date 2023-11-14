package com.reactvyhealthy.service;

import com.reactvyhealthy.repository.TipoConsultaRepository;
import com.reactvyhealthy.service.dto.TipoConsultaDTO;
import com.reactvyhealthy.service.mapper.TipoConsultaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.reactvyhealthy.domain.TipoConsulta}.
 */
@Service
@Transactional
public class TipoConsultaService {

    private final Logger log = LoggerFactory.getLogger(TipoConsultaService.class);

    private final TipoConsultaRepository tipoConsultaRepository;

    private final TipoConsultaMapper tipoConsultaMapper;

    public TipoConsultaService(TipoConsultaRepository tipoConsultaRepository, TipoConsultaMapper tipoConsultaMapper) {
        this.tipoConsultaRepository = tipoConsultaRepository;
        this.tipoConsultaMapper = tipoConsultaMapper;
    }

    /**
     * Save a tipoConsulta.
     *
     * @param tipoConsultaDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TipoConsultaDTO> save(TipoConsultaDTO tipoConsultaDTO) {
        log.debug("Request to save TipoConsulta : {}", tipoConsultaDTO);
        return tipoConsultaRepository.save(tipoConsultaMapper.toEntity(tipoConsultaDTO)).map(tipoConsultaMapper::toDto);
    }

    /**
     * Update a tipoConsulta.
     *
     * @param tipoConsultaDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TipoConsultaDTO> update(TipoConsultaDTO tipoConsultaDTO) {
        log.debug("Request to update TipoConsulta : {}", tipoConsultaDTO);
        return tipoConsultaRepository.save(tipoConsultaMapper.toEntity(tipoConsultaDTO)).map(tipoConsultaMapper::toDto);
    }

    /**
     * Partially update a tipoConsulta.
     *
     * @param tipoConsultaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<TipoConsultaDTO> partialUpdate(TipoConsultaDTO tipoConsultaDTO) {
        log.debug("Request to partially update TipoConsulta : {}", tipoConsultaDTO);

        return tipoConsultaRepository
            .findById(tipoConsultaDTO.getId())
            .map(existingTipoConsulta -> {
                tipoConsultaMapper.partialUpdate(existingTipoConsulta, tipoConsultaDTO);

                return existingTipoConsulta;
            })
            .flatMap(tipoConsultaRepository::save)
            .map(tipoConsultaMapper::toDto);
    }

    /**
     * Get all the tipoConsultas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TipoConsultaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TipoConsultas");
        return tipoConsultaRepository.findAllBy(pageable).map(tipoConsultaMapper::toDto);
    }

    /**
     * Returns the number of tipoConsultas available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return tipoConsultaRepository.count();
    }

    /**
     * Get one tipoConsulta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<TipoConsultaDTO> findOne(Long id) {
        log.debug("Request to get TipoConsulta : {}", id);
        return tipoConsultaRepository.findById(id).map(tipoConsultaMapper::toDto);
    }

    /**
     * Delete the tipoConsulta by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete TipoConsulta : {}", id);
        return tipoConsultaRepository.deleteById(id);
    }
}
