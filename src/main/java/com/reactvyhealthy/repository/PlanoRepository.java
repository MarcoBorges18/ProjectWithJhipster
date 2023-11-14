package com.reactvyhealthy.repository;

import com.reactvyhealthy.domain.Plano;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Plano entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanoRepository extends ReactiveCrudRepository<Plano, Long>, PlanoRepositoryInternal {
    Flux<Plano> findAllBy(Pageable pageable);

    @Query("SELECT * FROM plano entity WHERE entity.id not in (select paciente_id from paciente)")
    Flux<Plano> findAllWherePacienteIsNull();

    @Override
    <S extends Plano> Mono<S> save(S entity);

    @Override
    Flux<Plano> findAll();

    @Override
    Mono<Plano> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PlanoRepositoryInternal {
    <S extends Plano> Mono<S> save(S entity);

    Flux<Plano> findAllBy(Pageable pageable);

    Flux<Plano> findAll();

    Mono<Plano> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Plano> findAllBy(Pageable pageable, Criteria criteria);
}
