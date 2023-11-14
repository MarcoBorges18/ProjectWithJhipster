package com.reactvyhealthy.repository;

import com.reactvyhealthy.domain.TipoConsulta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the TipoConsulta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoConsultaRepository extends ReactiveCrudRepository<TipoConsulta, Long>, TipoConsultaRepositoryInternal {
    Flux<TipoConsulta> findAllBy(Pageable pageable);

    @Override
    <S extends TipoConsulta> Mono<S> save(S entity);

    @Override
    Flux<TipoConsulta> findAll();

    @Override
    Mono<TipoConsulta> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TipoConsultaRepositoryInternal {
    <S extends TipoConsulta> Mono<S> save(S entity);

    Flux<TipoConsulta> findAllBy(Pageable pageable);

    Flux<TipoConsulta> findAll();

    Mono<TipoConsulta> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TipoConsulta> findAllBy(Pageable pageable, Criteria criteria);
}
