package com.reactvyhealthy.repository;

import com.reactvyhealthy.domain.Paciente;
import com.reactvyhealthy.repository.rowmapper.EnderecoRowMapper;
import com.reactvyhealthy.repository.rowmapper.PacienteRowMapper;
import com.reactvyhealthy.repository.rowmapper.PlanoRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Paciente entity.
 */
@SuppressWarnings("unused")
class PacienteRepositoryInternalImpl extends SimpleR2dbcRepository<Paciente, Long> implements PacienteRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PlanoRowMapper planoMapper;
    private final EnderecoRowMapper enderecoMapper;
    private final PacienteRowMapper pacienteMapper;

    private static final Table entityTable = Table.aliased("paciente", EntityManager.ENTITY_ALIAS);
    private static final Table planoTable = Table.aliased("plano", "plano");
    private static final Table enderecoTable = Table.aliased("endereco", "endereco");

    public PacienteRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PlanoRowMapper planoMapper,
        EnderecoRowMapper enderecoMapper,
        PacienteRowMapper pacienteMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Paciente.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.planoMapper = planoMapper;
        this.enderecoMapper = enderecoMapper;
        this.pacienteMapper = pacienteMapper;
    }

    @Override
    public Flux<Paciente> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Paciente> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PacienteSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PlanoSqlHelper.getColumns(planoTable, "plano"));
        columns.addAll(EnderecoSqlHelper.getColumns(enderecoTable, "endereco"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(planoTable)
            .on(Column.create("plano_id", entityTable))
            .equals(Column.create("id", planoTable))
            .leftOuterJoin(enderecoTable)
            .on(Column.create("endereco_id", entityTable))
            .equals(Column.create("id", enderecoTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Paciente.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Paciente> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Paciente> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Paciente process(Row row, RowMetadata metadata) {
        Paciente entity = pacienteMapper.apply(row, "e");
        entity.setPlano(planoMapper.apply(row, "plano"));
        entity.setEndereco(enderecoMapper.apply(row, "endereco"));
        return entity;
    }

    @Override
    public <S extends Paciente> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
