package com.reactvyhealthy.repository;

import com.reactvyhealthy.domain.Agendamento;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Agendamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgendamentoRepository extends ReactiveCrudRepository<Agendamento, Long>, AgendamentoRepositoryInternal {
    Flux<Agendamento> findAllBy(Pageable pageable);

    @Query("SELECT * FROM agendamento entity WHERE entity.medico_id = :id")
    Flux<Agendamento> findByMedico(Long id);

    @Query("SELECT * FROM agendamento entity WHERE entity.medico_id IS NULL")
    Flux<Agendamento> findAllWhereMedicoIsNull();

    @Query("SELECT * FROM agendamento entity WHERE entity.paciente_id = :id")
    Flux<Agendamento> findByPaciente(Long id);

    @Query("SELECT * FROM agendamento entity WHERE entity.paciente_id IS NULL")
    Flux<Agendamento> findAllWherePacienteIsNull();

    @Query("SELECT * FROM agendamento entity WHERE entity.tipo_id = :id")
    Flux<Agendamento> findByTipo(Long id);

    @Query("SELECT * FROM agendamento entity WHERE entity.tipo_id IS NULL")
    Flux<Agendamento> findAllWhereTipoIsNull();

    @Override
    <S extends Agendamento> Mono<S> save(S entity);

    @Override
    Flux<Agendamento> findAll();

    @Override
    Mono<Agendamento> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AgendamentoRepositoryInternal {
    <S extends Agendamento> Mono<S> save(S entity);

    Flux<Agendamento> findAllBy(Pageable pageable);

    Flux<Agendamento> findAll();

    Mono<Agendamento> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Agendamento> findAllBy(Pageable pageable, Criteria criteria);
}
