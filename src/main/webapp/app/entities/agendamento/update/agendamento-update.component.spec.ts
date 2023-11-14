import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IMedico } from 'app/entities/medico/medico.model';
import { MedicoService } from 'app/entities/medico/service/medico.service';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { ITipoConsulta } from 'app/entities/tipo-consulta/tipo-consulta.model';
import { TipoConsultaService } from 'app/entities/tipo-consulta/service/tipo-consulta.service';
import { IAgendamento } from '../agendamento.model';
import { AgendamentoService } from '../service/agendamento.service';
import { AgendamentoFormService } from './agendamento-form.service';

import { AgendamentoUpdateComponent } from './agendamento-update.component';

describe('Agendamento Management Update Component', () => {
  let comp: AgendamentoUpdateComponent;
  let fixture: ComponentFixture<AgendamentoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let agendamentoFormService: AgendamentoFormService;
  let agendamentoService: AgendamentoService;
  let medicoService: MedicoService;
  let pacienteService: PacienteService;
  let tipoConsultaService: TipoConsultaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AgendamentoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AgendamentoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AgendamentoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    agendamentoFormService = TestBed.inject(AgendamentoFormService);
    agendamentoService = TestBed.inject(AgendamentoService);
    medicoService = TestBed.inject(MedicoService);
    pacienteService = TestBed.inject(PacienteService);
    tipoConsultaService = TestBed.inject(TipoConsultaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Medico query and add missing value', () => {
      const agendamento: IAgendamento = { id: 456 };
      const medico: IMedico = { id: 17510 };
      agendamento.medico = medico;

      const medicoCollection: IMedico[] = [{ id: 25651 }];
      jest.spyOn(medicoService, 'query').mockReturnValue(of(new HttpResponse({ body: medicoCollection })));
      const additionalMedicos = [medico];
      const expectedCollection: IMedico[] = [...additionalMedicos, ...medicoCollection];
      jest.spyOn(medicoService, 'addMedicoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ agendamento });
      comp.ngOnInit();

      expect(medicoService.query).toHaveBeenCalled();
      expect(medicoService.addMedicoToCollectionIfMissing).toHaveBeenCalledWith(
        medicoCollection,
        ...additionalMedicos.map(expect.objectContaining),
      );
      expect(comp.medicosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Paciente query and add missing value', () => {
      const agendamento: IAgendamento = { id: 456 };
      const paciente: IPaciente = { id: 16340 };
      agendamento.paciente = paciente;

      const pacienteCollection: IPaciente[] = [{ id: 24302 }];
      jest.spyOn(pacienteService, 'query').mockReturnValue(of(new HttpResponse({ body: pacienteCollection })));
      const additionalPacientes = [paciente];
      const expectedCollection: IPaciente[] = [...additionalPacientes, ...pacienteCollection];
      jest.spyOn(pacienteService, 'addPacienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ agendamento });
      comp.ngOnInit();

      expect(pacienteService.query).toHaveBeenCalled();
      expect(pacienteService.addPacienteToCollectionIfMissing).toHaveBeenCalledWith(
        pacienteCollection,
        ...additionalPacientes.map(expect.objectContaining),
      );
      expect(comp.pacientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TipoConsulta query and add missing value', () => {
      const agendamento: IAgendamento = { id: 456 };
      const tipo: ITipoConsulta = { id: 11408 };
      agendamento.tipo = tipo;

      const tipoConsultaCollection: ITipoConsulta[] = [{ id: 31031 }];
      jest.spyOn(tipoConsultaService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoConsultaCollection })));
      const additionalTipoConsultas = [tipo];
      const expectedCollection: ITipoConsulta[] = [...additionalTipoConsultas, ...tipoConsultaCollection];
      jest.spyOn(tipoConsultaService, 'addTipoConsultaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ agendamento });
      comp.ngOnInit();

      expect(tipoConsultaService.query).toHaveBeenCalled();
      expect(tipoConsultaService.addTipoConsultaToCollectionIfMissing).toHaveBeenCalledWith(
        tipoConsultaCollection,
        ...additionalTipoConsultas.map(expect.objectContaining),
      );
      expect(comp.tipoConsultasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const agendamento: IAgendamento = { id: 456 };
      const medico: IMedico = { id: 21411 };
      agendamento.medico = medico;
      const paciente: IPaciente = { id: 12485 };
      agendamento.paciente = paciente;
      const tipo: ITipoConsulta = { id: 15110 };
      agendamento.tipo = tipo;

      activatedRoute.data = of({ agendamento });
      comp.ngOnInit();

      expect(comp.medicosSharedCollection).toContain(medico);
      expect(comp.pacientesSharedCollection).toContain(paciente);
      expect(comp.tipoConsultasSharedCollection).toContain(tipo);
      expect(comp.agendamento).toEqual(agendamento);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgendamento>>();
      const agendamento = { id: 123 };
      jest.spyOn(agendamentoFormService, 'getAgendamento').mockReturnValue(agendamento);
      jest.spyOn(agendamentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agendamento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agendamento }));
      saveSubject.complete();

      // THEN
      expect(agendamentoFormService.getAgendamento).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(agendamentoService.update).toHaveBeenCalledWith(expect.objectContaining(agendamento));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgendamento>>();
      const agendamento = { id: 123 };
      jest.spyOn(agendamentoFormService, 'getAgendamento').mockReturnValue({ id: null });
      jest.spyOn(agendamentoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agendamento: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agendamento }));
      saveSubject.complete();

      // THEN
      expect(agendamentoFormService.getAgendamento).toHaveBeenCalled();
      expect(agendamentoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgendamento>>();
      const agendamento = { id: 123 };
      jest.spyOn(agendamentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agendamento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(agendamentoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMedico', () => {
      it('Should forward to medicoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(medicoService, 'compareMedico');
        comp.compareMedico(entity, entity2);
        expect(medicoService.compareMedico).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePaciente', () => {
      it('Should forward to pacienteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(pacienteService, 'comparePaciente');
        comp.comparePaciente(entity, entity2);
        expect(pacienteService.comparePaciente).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTipoConsulta', () => {
      it('Should forward to tipoConsultaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(tipoConsultaService, 'compareTipoConsulta');
        comp.compareTipoConsulta(entity, entity2);
        expect(tipoConsultaService.compareTipoConsulta).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
