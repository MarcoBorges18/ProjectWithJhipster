import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EspecialidadeService } from '../service/especialidade.service';
import { IEspecialidade } from '../especialidade.model';
import { EspecialidadeFormService } from './especialidade-form.service';

import { EspecialidadeUpdateComponent } from './especialidade-update.component';

describe('Especialidade Management Update Component', () => {
  let comp: EspecialidadeUpdateComponent;
  let fixture: ComponentFixture<EspecialidadeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let especialidadeFormService: EspecialidadeFormService;
  let especialidadeService: EspecialidadeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), EspecialidadeUpdateComponent],
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
      .overrideTemplate(EspecialidadeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EspecialidadeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    especialidadeFormService = TestBed.inject(EspecialidadeFormService);
    especialidadeService = TestBed.inject(EspecialidadeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const especialidade: IEspecialidade = { id: 456 };

      activatedRoute.data = of({ especialidade });
      comp.ngOnInit();

      expect(comp.especialidade).toEqual(especialidade);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEspecialidade>>();
      const especialidade = { id: 123 };
      jest.spyOn(especialidadeFormService, 'getEspecialidade').mockReturnValue(especialidade);
      jest.spyOn(especialidadeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ especialidade });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: especialidade }));
      saveSubject.complete();

      // THEN
      expect(especialidadeFormService.getEspecialidade).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(especialidadeService.update).toHaveBeenCalledWith(expect.objectContaining(especialidade));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEspecialidade>>();
      const especialidade = { id: 123 };
      jest.spyOn(especialidadeFormService, 'getEspecialidade').mockReturnValue({ id: null });
      jest.spyOn(especialidadeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ especialidade: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: especialidade }));
      saveSubject.complete();

      // THEN
      expect(especialidadeFormService.getEspecialidade).toHaveBeenCalled();
      expect(especialidadeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEspecialidade>>();
      const especialidade = { id: 123 };
      jest.spyOn(especialidadeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ especialidade });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(especialidadeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
