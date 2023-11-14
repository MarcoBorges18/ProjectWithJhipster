package com.reactvyhealthy.repository;

import com.reactvyhealthy.domain.Medico;
import com.reactvyhealthy.repository.rowmapper.EnderecoRowMapper;
import com.reactvyhealthy.repository.rowmapper.EspecialidadeRowMapper;
import com.reactvyhealthy.repository.rowmapper.MedicoRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Medico entity.
 */
@SuppressWarnings("unused")
class MedicoRepositoryInternalImpl extends SimpleR2dbcRepository<Medico, Long> implements MedicoRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final EspecialidadeRowMapper especialidadeMapper;
    private final EnderecoRowMapper enderecoMapper;
    private final MedicoRowMapper medicoMapper;

    private static final Table entityTable = Table.aliased("medico", EntityManager.ENTITY_ALIAS);
    private static final Table especialidadeTable = Table.aliased("especialidade", "especialidade");
    private static final Table enderecoTable = Table.aliased("endereco", "endereco");

    public MedicoRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        EspecialidadeRowMapper especialidadeMapper,
        EnderecoRowMapper enderecoMapper,
        MedicoRowMapper medicoMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Medico.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.especialidadeMapper = especialidadeMapper;
        this.enderecoMapper = enderecoMapper;
        this.medicoMapper = medicoMapper;
    }

    @Override
    public Flux<Medico> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Medico> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = MedicoSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(EspecialidadeSqlHelper.getColumns(especialidadeTable, "especialidade"));
        columns.addAll(EnderecoSqlHelper.getColumns(enderecoTable, "endereco"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(especialidadeTable)
            .on(Column.create("especialidade_id", entityTable))
            .equals(Column.create("id", especialidadeTable))
            .leftOuterJoin(enderecoTable)
            .on(Column.create("endereco_id", entityTable))
            .equals(Column.create("id", enderecoTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Medico.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Medico> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Medico> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Medico process(Row row, RowMetadata metadata) {
        Medico entity = medicoMapper.apply(row, "e");
        entity.setEspecialidade(especialidadeMapper.apply(row, "especialidade"));
        entity.setEndereco(enderecoMapper.apply(row, "endereco"));
        return entity;
    }

    @Override
    public <S extends Medico> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
