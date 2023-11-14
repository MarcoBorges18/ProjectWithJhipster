import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IPlano } from 'app/entities/plano/plano.model';
import { PlanoService } from 'app/entities/plano/service/plano.service';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { EnderecoService } from 'app/entities/endereco/service/endereco.service';
import { IPaciente } from '../paciente.model';
import { PacienteService } from '../service/paciente.service';
import { PacienteFormService } from './paciente-form.service';

import { PacienteUpdateComponent } from './paciente-update.component';

describe('Paciente Management Update Component', () => {
  let comp: PacienteUpdateComponent;
  let fixture: ComponentFixture<PacienteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pacienteFormService: PacienteFormService;
  let pacienteService: PacienteService;
  let planoService: PlanoService;
  let enderecoService: EnderecoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PacienteUpdateComponent],
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
      .overrideTemplate(PacienteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PacienteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pacienteFormService = TestBed.inject(PacienteFormService);
    pacienteService = TestBed.inject(PacienteService);
    planoService = TestBed.inject(PlanoService);
    enderecoService = TestBed.inject(EnderecoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call plano query and add missing value', () => {
      const paciente: IPaciente = { id: 456 };
      const plano: IPlano = { id: 362 };
      paciente.plano = plano;

      const planoCollection: IPlano[] = [{ id: 13528 }];
      jest.spyOn(planoService, 'query').mockReturnValue(of(new HttpResponse({ body: planoCollection })));
      const expectedCollection: IPlano[] = [plano, ...planoCollection];
      jest.spyOn(planoService, 'addPlanoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paciente });
      comp.ngOnInit();

      expect(planoService.query).toHaveBeenCalled();
      expect(planoService.addPlanoToCollectionIfMissing).toHaveBeenCalledWith(planoCollection, plano);
      expect(comp.planosCollection).toEqual(expectedCollection);
    });

    it('Should call endereco query and add missing value', () => {
      const paciente: IPaciente = { id: 456 };
      const endereco: IEndereco = { id: 13197 };
      paciente.endereco = endereco;

      const enderecoCollection: IEndereco[] = [{ id: 3547 }];
      jest.spyOn(enderecoService, 'query').mockReturnValue(of(new HttpResponse({ body: enderecoCollection })));
      const expectedCollection: IEndereco[] = [endereco, ...enderecoCollection];
      jest.spyOn(enderecoService, 'addEnderecoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paciente });
      comp.ngOnInit();

      expect(enderecoService.query).toHaveBeenCalled();
      expect(enderecoService.addEnderecoToCollectionIfMissing).toHaveBeenCalledWith(enderecoCollection, endereco);
      expect(comp.enderecosCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const paciente: IPaciente = { id: 456 };
      const plano: IPlano = { id: 25761 };
      paciente.plano = plano;
      const endereco: IEndereco = { id: 19265 };
      paciente.endereco = endereco;

      activatedRoute.data = of({ paciente });
      comp.ngOnInit();

      expect(comp.planosCollection).toContain(plano);
      expect(comp.enderecosCollection).toContain(endereco);
      expect(comp.paciente).toEqual(paciente);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaciente>>();
      const paciente = { id: 123 };
      jest.spyOn(pacienteFormService, 'getPaciente').mockReturnValue(paciente);
      jest.spyOn(pacienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paciente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paciente }));
      saveSubject.complete();

      // THEN
      expect(pacienteFormService.getPaciente).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pacienteService.update).toHaveBeenCalledWith(expect.objectContaining(paciente));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaciente>>();
      const paciente = { id: 123 };
      jest.spyOn(pacienteFormService, 'getPaciente').mockReturnValue({ id: null });
      jest.spyOn(pacienteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paciente: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paciente }));
      saveSubject.complete();

      // THEN
      expect(pacienteFormService.getPaciente).toHaveBeenCalled();
      expect(pacienteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaciente>>();
      const paciente = { id: 123 };
      jest.spyOn(pacienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paciente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pacienteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePlano', () => {
      it('Should forward to planoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(planoService, 'comparePlano');
        comp.comparePlano(entity, entity2);
        expect(planoService.comparePlano).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEndereco', () => {
      it('Should forward to enderecoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(enderecoService, 'compareEndereco');
        comp.compareEndereco(entity, entity2);
        expect(enderecoService.compareEndereco).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
