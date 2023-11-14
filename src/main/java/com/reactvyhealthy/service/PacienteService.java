package com.reactvyhealthy.service;

import com.reactvyhealthy.repository.PacienteRepository;
import com.reactvyhealthy.service.dto.PacienteDTO;
import com.reactvyhealthy.service.mapper.PacienteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.reactvyhealthy.domain.Paciente}.
 */
@Service
@Transactional
public class PacienteService {

    private final Logger log = LoggerFactory.getLogger(PacienteService.class);

    private final PacienteRepository pacienteRepository;

    private final PacienteMapper pacienteMapper;

    public PacienteService(PacienteRepository pacienteRepository, PacienteMapper pacienteMapper) {
        this.pacienteRepository = pacienteRepository;
        this.pacienteMapper = pacienteMapper;
    }

    /**
     * Save a paciente.
     *
     * @param pacienteDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PacienteDTO> save(PacienteDTO pacienteDTO) {
        log.debug("Request to save Paciente : {}", pacienteDTO);
        return pacienteRepository.save(pacienteMapper.toEntity(pacienteDTO)).map(pacienteMapper::toDto);
    }

    /**
     * Update a paciente.
     *
     * @param pacienteDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PacienteDTO> update(PacienteDTO pacienteDTO) {
        log.debug("Request to update Paciente : {}", pacienteDTO);
        return pacienteRepository.save(pacienteMapper.toEntity(pacienteDTO)).map(pacienteMapper::toDto);
    }

    /**
     * Partially update a paciente.
     *
     * @param pacienteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PacienteDTO> partialUpdate(PacienteDTO pacienteDTO) {
        log.debug("Request to partially update Paciente : {}", pacienteDTO);

        return pacienteRepository
            .findById(pacienteDTO.getId())
            .map(existingPaciente -> {
                pacienteMapper.partialUpdate(existingPaciente, pacienteDTO);

                return existingPaciente;
            })
            .flatMap(pacienteRepository::save)
            .map(pacienteMapper::toDto);
    }

    /**
     * Get all the pacientes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PacienteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pacientes");
        return pacienteRepository.findAllBy(pageable).map(pacienteMapper::toDto);
    }

    /**
     * Returns the number of pacientes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return pacienteRepository.count();
    }

    /**
     * Get one paciente by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PacienteDTO> findOne(Long id) {
        log.debug("Request to get Paciente : {}", id);
        return pacienteRepository.findById(id).map(pacienteMapper::toDto);
    }

    /**
     * Delete the paciente by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Paciente : {}", id);
        return pacienteRepository.deleteById(id);
    }
}
