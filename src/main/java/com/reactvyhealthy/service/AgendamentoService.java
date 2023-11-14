package com.reactvyhealthy.service;

import com.reactvyhealthy.repository.AgendamentoRepository;
import com.reactvyhealthy.service.dto.AgendamentoDTO;
import com.reactvyhealthy.service.mapper.AgendamentoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.reactvyhealthy.domain.Agendamento}.
 */
@Service
@Transactional
public class AgendamentoService {

    private final Logger log = LoggerFactory.getLogger(AgendamentoService.class);

    private final AgendamentoRepository agendamentoRepository;

    private final AgendamentoMapper agendamentoMapper;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, AgendamentoMapper agendamentoMapper) {
        this.agendamentoRepository = agendamentoRepository;
        this.agendamentoMapper = agendamentoMapper;
    }

    /**
     * Save a agendamento.
     *
     * @param agendamentoDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AgendamentoDTO> save(AgendamentoDTO agendamentoDTO) {
        log.debug("Request to save Agendamento : {}", agendamentoDTO);
        return agendamentoRepository.save(agendamentoMapper.toEntity(agendamentoDTO)).map(agendamentoMapper::toDto);
    }

    /**
     * Update a agendamento.
     *
     * @param agendamentoDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AgendamentoDTO> update(AgendamentoDTO agendamentoDTO) {
        log.debug("Request to update Agendamento : {}", agendamentoDTO);
        return agendamentoRepository.save(agendamentoMapper.toEntity(agendamentoDTO)).map(agendamentoMapper::toDto);
    }

    /**
     * Partially update a agendamento.
     *
     * @param agendamentoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<AgendamentoDTO> partialUpdate(AgendamentoDTO agendamentoDTO) {
        log.debug("Request to partially update Agendamento : {}", agendamentoDTO);

        return agendamentoRepository
            .findById(agendamentoDTO.getId())
            .map(existingAgendamento -> {
                agendamentoMapper.partialUpdate(existingAgendamento, agendamentoDTO);

                return existingAgendamento;
            })
            .flatMap(agendamentoRepository::save)
            .map(agendamentoMapper::toDto);
    }

    /**
     * Get all the agendamentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<AgendamentoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Agendamentos");
        return agendamentoRepository.findAllBy(pageable).map(agendamentoMapper::toDto);
    }

    /**
     * Returns the number of agendamentos available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return agendamentoRepository.count();
    }

    /**
     * Get one agendamento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<AgendamentoDTO> findOne(Long id) {
        log.debug("Request to get Agendamento : {}", id);
        return agendamentoRepository.findById(id).map(agendamentoMapper::toDto);
    }

    /**
     * Delete the agendamento by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Agendamento : {}", id);
        return agendamentoRepository.deleteById(id);
    }
}
