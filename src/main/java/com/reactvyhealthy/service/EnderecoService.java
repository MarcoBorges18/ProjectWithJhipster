package com.reactvyhealthy.service;

import com.reactvyhealthy.repository.EnderecoRepository;
import com.reactvyhealthy.service.dto.EnderecoDTO;
import com.reactvyhealthy.service.mapper.EnderecoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.reactvyhealthy.domain.Endereco}.
 */
@Service
@Transactional
public class EnderecoService {

    private final Logger log = LoggerFactory.getLogger(EnderecoService.class);

    private final EnderecoRepository enderecoRepository;

    private final EnderecoMapper enderecoMapper;

    public EnderecoService(EnderecoRepository enderecoRepository, EnderecoMapper enderecoMapper) {
        this.enderecoRepository = enderecoRepository;
        this.enderecoMapper = enderecoMapper;
    }

    /**
     * Save a endereco.
     *
     * @param enderecoDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<EnderecoDTO> save(EnderecoDTO enderecoDTO) {
        log.debug("Request to save Endereco : {}", enderecoDTO);
        return enderecoRepository.save(enderecoMapper.toEntity(enderecoDTO)).map(enderecoMapper::toDto);
    }

    /**
     * Update a endereco.
     *
     * @param enderecoDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<EnderecoDTO> update(EnderecoDTO enderecoDTO) {
        log.debug("Request to update Endereco : {}", enderecoDTO);
        return enderecoRepository.save(enderecoMapper.toEntity(enderecoDTO)).map(enderecoMapper::toDto);
    }

    /**
     * Partially update a endereco.
     *
     * @param enderecoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<EnderecoDTO> partialUpdate(EnderecoDTO enderecoDTO) {
        log.debug("Request to partially update Endereco : {}", enderecoDTO);

        return enderecoRepository
            .findById(enderecoDTO.getId())
            .map(existingEndereco -> {
                enderecoMapper.partialUpdate(existingEndereco, enderecoDTO);

                return existingEndereco;
            })
            .flatMap(enderecoRepository::save)
            .map(enderecoMapper::toDto);
    }

    /**
     * Get all the enderecos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<EnderecoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Enderecos");
        return enderecoRepository.findAllBy(pageable).map(enderecoMapper::toDto);
    }

    /**
     *  Get all the enderecos where Paciente is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<EnderecoDTO> findAllWherePacienteIsNull() {
        log.debug("Request to get all enderecos where Paciente is null");
        return enderecoRepository.findAllWherePacienteIsNull().map(enderecoMapper::toDto);
    }

    /**
     *  Get all the enderecos where Medico is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<EnderecoDTO> findAllWhereMedicoIsNull() {
        log.debug("Request to get all enderecos where Medico is null");
        return enderecoRepository.findAllWhereMedicoIsNull().map(enderecoMapper::toDto);
    }

    /**
     * Returns the number of enderecos available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return enderecoRepository.count();
    }

    /**
     * Get one endereco by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<EnderecoDTO> findOne(Long id) {
        log.debug("Request to get Endereco : {}", id);
        return enderecoRepository.findById(id).map(enderecoMapper::toDto);
    }

    /**
     * Delete the endereco by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Endereco : {}", id);
        return enderecoRepository.deleteById(id);
    }
}
