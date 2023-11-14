package com.reactvyhealthy.repository;

import com.reactvyhealthy.domain.Agendamento;
import com.reactvyhealthy.repository.rowmapper.AgendamentoRowMapper;
import com.reactvyhealthy.repository.rowmapper.MedicoRowMapper;
import com.reactvyhealthy.repository.rowmapper.PacienteRowMapper;
import com.reactvyhealthy.repository.rowmapper.TipoConsultaRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Agendamento entity.
 */
@SuppressWarnings("unused")
class AgendamentoRepositoryInternalImpl extends SimpleR2dbcRepository<Agendamento, Long> implements AgendamentoRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final MedicoRowMapper medicoMapper;
    private final PacienteRowMapper pacienteMapper;
    private final TipoConsultaRowMapper tipoconsultaMapper;
    private final AgendamentoRowMapper agendamentoMapper;

    private static final Table entityTable = Table.aliased("agendamento", EntityManager.ENTITY_ALIAS);
    private static final Table medicoTable = Table.aliased("medico", "medico");
    private static final Table pacienteTable = Table.aliased("paciente", "paciente");
    private static final Table tipoTable = Table.aliased("tipo_consulta", "tipo");

    public AgendamentoRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        MedicoRowMapper medicoMapper,
        PacienteRowMapper pacienteMapper,
        TipoConsultaRowMapper tipoconsultaMapper,
        AgendamentoRowMapper agendamentoMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Agendamento.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.medicoMapper = medicoMapper;
        this.pacienteMapper = pacienteMapper;
        this.tipoconsultaMapper = tipoconsultaMapper;
        this.agendamentoMapper = agendamentoMapper;
    }

    @Override
    public Flux<Agendamento> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Agendamento> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = AgendamentoSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(MedicoSqlHelper.getColumns(medicoTable, "medico"));
        columns.addAll(PacienteSqlHelper.getColumns(pacienteTable, "paciente"));
        columns.addAll(TipoConsultaSqlHelper.getColumns(tipoTable, "tipo"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(medicoTable)
            .on(Column.create("medico_id", entityTable))
            .equals(Column.create("id", medicoTable))
            .leftOuterJoin(pacienteTable)
            .on(Column.create("paciente_id", entityTable))
            .equals(Column.create("id", pacienteTable))
            .leftOuterJoin(tipoTable)
            .on(Column.create("tipo_id", entityTable))
            .equals(Column.create("id", tipoTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Agendamento.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Agendamento> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Agendamento> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Agendamento process(Row row, RowMetadata metadata) {
        Agendamento entity = agendamentoMapper.apply(row, "e");
        entity.setMedico(medicoMapper.apply(row, "medico"));
        entity.setPaciente(pacienteMapper.apply(row, "paciente"));
        entity.setTipo(tipoconsultaMapper.apply(row, "tipo"));
        return entity;
    }

    @Override
    public <S extends Agendamento> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
