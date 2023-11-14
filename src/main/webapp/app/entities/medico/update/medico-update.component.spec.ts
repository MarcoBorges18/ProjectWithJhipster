import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IEspecialidade } from 'app/entities/especialidade/especialidade.model';
import { EspecialidadeService } from 'app/entities/especialidade/service/especialidade.service';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { EnderecoService } from 'app/entities/endereco/service/endereco.service';
import { IMedico } from '../medico.model';
import { MedicoService } from '../service/medico.service';
import { MedicoFormService } from './medico-form.service';

import { MedicoUpdateComponent } from './medico-update.component';

describe('Medico Management Update Component', () => {
  let comp: MedicoUpdateComponent;
  let fixture: ComponentFixture<MedicoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let medicoFormService: MedicoFormService;
  let medicoService: MedicoService;
  let especialidadeService: EspecialidadeService;
  let enderecoService: EnderecoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MedicoUpdateComponent],
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
      .overrideTemplate(MedicoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MedicoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    medicoFormService = TestBed.inject(MedicoFormService);
    medicoService = TestBed.inject(MedicoService);
    especialidadeService = TestBed.inject(EspecialidadeService);
    enderecoService = TestBed.inject(EnderecoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call especialidade query and add missing value', () => {
      const medico: IMedico = { id: 456 };
      const especialidade: IEspecialidade = { id: 19971 };
      medico.especialidade = especialidade;

      const especialidadeCollection: IEspecialidade[] = [{ id: 31556 }];
      jest.spyOn(especialidadeService, 'query').mockReturnValue(of(new HttpResponse({ body: especialidadeCollection })));
      const expectedCollection: IEspecialidade[] = [especialidade, ...especialidadeCollection];
      jest.spyOn(especialidadeService, 'addEspecialidadeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ medico });
      comp.ngOnInit();

      expect(especialidadeService.query).toHaveBeenCalled();
      expect(especialidadeService.addEspecialidadeToCollectionIfMissing).toHaveBeenCalledWith(especialidadeCollection, especialidade);
      expect(comp.especialidadesCollection).toEqual(expectedCollection);
    });

    it('Should call endereco query and add missing value', () => {
      const medico: IMedico = { id: 456 };
      const endereco: IEndereco = { id: 25225 };
      medico.endereco = endereco;

      const enderecoCollection: IEndereco[] = [{ id: 24937 }];
      jest.spyOn(enderecoService, 'query').mockReturnValue(of(new HttpResponse({ body: enderecoCollection })));
      const expectedCollection: IEndereco[] = [endereco, ...enderecoCollection];
      jest.spyOn(enderecoService, 'addEnderecoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ medico });
      comp.ngOnInit();

      expect(enderecoService.query).toHaveBeenCalled();
      expect(enderecoService.addEnderecoToCollectionIfMissing).toHaveBeenCalledWith(enderecoCollection, endereco);
      expect(comp.enderecosCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const medico: IMedico = { id: 456 };
      const especialidade: IEspecialidade = { id: 4701 };
      medico.especialidade = especialidade;
      const endereco: IEndereco = { id: 29648 };
      medico.endereco = endereco;

      activatedRoute.data = of({ medico });
      comp.ngOnInit();

      expect(comp.especialidadesCollection).toContain(especialidade);
      expect(comp.enderecosCollection).toContain(endereco);
      expect(comp.medico).toEqual(medico);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedico>>();
      const medico = { id: 123 };
      jest.spyOn(medicoFormService, 'getMedico').mockReturnValue(medico);
      jest.spyOn(medicoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medico });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medico }));
      saveSubject.complete();

      // THEN
      expect(medicoFormService.getMedico).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(medicoService.update).toHaveBeenCalledWith(expect.objectContaining(medico));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedico>>();
      const medico = { id: 123 };
      jest.spyOn(medicoFormService, 'getMedico').mockReturnValue({ id: null });
      jest.spyOn(medicoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medico: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medico }));
      saveSubject.complete();

      // THEN
      expect(medicoFormService.getMedico).toHaveBeenCalled();
      expect(medicoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedico>>();
      const medico = { id: 123 };
      jest.spyOn(medicoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medico });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(medicoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEspecialidade', () => {
      it('Should forward to especialidadeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(especialidadeService, 'compareEspecialidade');
        comp.compareEspecialidade(entity, entity2);
        expect(especialidadeService.compareEspecialidade).toHaveBeenCalledWith(entity, entity2);
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
