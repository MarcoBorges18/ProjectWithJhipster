package com.reactvyhealthy.service;

import com.reactvyhealthy.repository.MedicoRepository;
import com.reactvyhealthy.service.dto.MedicoDTO;
import com.reactvyhealthy.service.mapper.MedicoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.reactvyhealthy.domain.Medico}.
 */
@Service
@Transactional
public class MedicoService {

    private final Logger log = LoggerFactory.getLogger(MedicoService.class);

    private final MedicoRepository medicoRepository;

    private final MedicoMapper medicoMapper;

    public MedicoService(MedicoRepository medicoRepository, MedicoMapper medicoMapper) {
        this.medicoRepository = medicoRepository;
        this.medicoMapper = medicoMapper;
    }

    /**
     * Save a medico.
     *
     * @param medicoDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<MedicoDTO> save(MedicoDTO medicoDTO) {
        log.debug("Request to save Medico : {}", medicoDTO);
        return medicoRepository.save(medicoMapper.toEntity(medicoDTO)).map(medicoMapper::toDto);
    }

    /**
     * Update a medico.
     *
     * @param medicoDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<MedicoDTO> update(MedicoDTO medicoDTO) {
        log.debug("Request to update Medico : {}", medicoDTO);
        return medicoRepository.save(medicoMapper.toEntity(medicoDTO)).map(medicoMapper::toDto);
    }

    /**
     * Partially update a medico.
     *
     * @param medicoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<MedicoDTO> partialUpdate(MedicoDTO medicoDTO) {
        log.debug("Request to partially update Medico : {}", medicoDTO);

        return medicoRepository
            .findById(medicoDTO.getId())
            .map(existingMedico -> {
                medicoMapper.partialUpdate(existingMedico, medicoDTO);

                return existingMedico;
            })
            .flatMap(medicoRepository::save)
            .map(medicoMapper::toDto);
    }

    /**
     * Get all the medicos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<MedicoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Medicos");
        return medicoRepository.findAllBy(pageable).map(medicoMapper::toDto);
    }

    /**
     * Returns the number of medicos available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return medicoRepository.count();
    }

    /**
     * Get one medico by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<MedicoDTO> findOne(Long id) {
        log.debug("Request to get Medico : {}", id);
        return medicoRepository.findById(id).map(medicoMapper::toDto);
    }

    /**
     * Delete the medico by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Medico : {}", id);
        return medicoRepository.deleteById(id);
    }
}
