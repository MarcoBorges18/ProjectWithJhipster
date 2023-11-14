package com.reactvyhealthy.repository;

import com.reactvyhealthy.domain.Endereco;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Endereco entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnderecoRepository extends ReactiveCrudRepository<Endereco, Long>, EnderecoRepositoryInternal {
    Flux<Endereco> findAllBy(Pageable pageable);

    @Query("SELECT * FROM endereco entity WHERE entity.id not in (select paciente_id from paciente)")
    Flux<Endereco> findAllWherePacienteIsNull();

    @Query("SELECT * FROM endereco entity WHERE entity.id not in (select medico_id from medico)")
    Flux<Endereco> findAllWhereMedicoIsNull();

    @Override
    <S extends Endereco> Mono<S> save(S entity);

    @Override
    Flux<Endereco> findAll();

    @Override
    Mono<Endereco> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EnderecoRepositoryInternal {
    <S extends Endereco> Mono<S> save(S entity);

    Flux<Endereco> findAllBy(Pageable pageable);

    Flux<Endereco> findAll();

    Mono<Endereco> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Endereco> findAllBy(Pageable pageable, Criteria criteria);
}
