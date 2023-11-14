package com.reactvyhealthy.service;

import com.reactvyhealthy.repository.EspecialidadeRepository;
import com.reactvyhealthy.service.dto.EspecialidadeDTO;
import com.reactvyhealthy.service.mapper.EspecialidadeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.reactvyhealthy.domain.Especialidade}.
 */
@Service
@Transactional
public class EspecialidadeService {

    private final Logger log = LoggerFactory.getLogger(EspecialidadeService.class);

    private final EspecialidadeRepository especialidadeRepository;

    private final EspecialidadeMapper especialidadeMapper;

    public EspecialidadeService(EspecialidadeRepository especialidadeRepository, EspecialidadeMapper especialidadeMapper) {
        this.especialidadeRepository = especialidadeRepository;
        this.especialidadeMapper = especialidadeMapper;
    }

    /**
     * Save a especialidade.
     *
     * @param especialidadeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<EspecialidadeDTO> save(EspecialidadeDTO especialidadeDTO) {
        log.debug("Request to save Especialidade : {}", especialidadeDTO);
        return especialidadeRepository.save(especialidadeMapper.toEntity(especialidadeDTO)).map(especialidadeMapper::toDto);
    }

    /**
     * Update a especialidade.
     *
     * @param especialidadeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<EspecialidadeDTO> update(EspecialidadeDTO especialidadeDTO) {
        log.debug("Request to update Especialidade : {}", especialidadeDTO);
        return especialidadeRepository.save(especialidadeMapper.toEntity(especialidadeDTO)).map(especialidadeMapper::toDto);
    }

    /**
     * Partially update a especialidade.
     *
     * @param especialidadeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<EspecialidadeDTO> partialUpdate(EspecialidadeDTO especialidadeDTO) {
        log.debug("Request to partially update Especialidade : {}", especialidadeDTO);

        return especialidadeRepository
            .findById(especialidadeDTO.getId())
            .map(existingEspecialidade -> {
                especialidadeMapper.partialUpdate(existingEspecialidade, especialidadeDTO);

                return existingEspecialidade;
            })
            .flatMap(especialidadeRepository::save)
            .map(especialidadeMapper::toDto);
    }

    /**
     * Get all the especialidades.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<EspecialidadeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Especialidades");
        return especialidadeRepository.findAllBy(pageable).map(especialidadeMapper::toDto);
    }

    /**
     *  Get all the especialidades where Medico is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<EspecialidadeDTO> findAllWhereMedicoIsNull() {
        log.debug("Request to get all especialidades where Medico is null");
        return especialidadeRepository.findAllWhereMedicoIsNull().map(especialidadeMapper::toDto);
    }

    /**
     * Returns the number of especialidades available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return especialidadeRepository.count();
    }

    /**
     * Get one especialidade by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<EspecialidadeDTO> findOne(Long id) {
        log.debug("Request to get Especialidade : {}", id);
        return especialidadeRepository.findById(id).map(especialidadeMapper::toDto);
    }

    /**
     * Delete the especialidade by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Especialidade : {}", id);
        return especialidadeRepository.deleteById(id);
    }
}
