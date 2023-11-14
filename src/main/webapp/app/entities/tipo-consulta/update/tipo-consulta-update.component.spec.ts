import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TipoConsultaService } from '../service/tipo-consulta.service';
import { ITipoConsulta } from '../tipo-consulta.model';
import { TipoConsultaFormService } from './tipo-consulta-form.service';

import { TipoConsultaUpdateComponent } from './tipo-consulta-update.component';

describe('TipoConsulta Management Update Component', () => {
  let comp: TipoConsultaUpdateComponent;
  let fixture: ComponentFixture<TipoConsultaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoConsultaFormService: TipoConsultaFormService;
  let tipoConsultaService: TipoConsultaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TipoConsultaUpdateComponent],
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
      .overrideTemplate(TipoConsultaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoConsultaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoConsultaFormService = TestBed.inject(TipoConsultaFormService);
    tipoConsultaService = TestBed.inject(TipoConsultaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tipoConsulta: ITipoConsulta = { id: 456 };

      activatedRoute.data = of({ tipoConsulta });
      comp.ngOnInit();

      expect(comp.tipoConsulta).toEqual(tipoConsulta);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoConsulta>>();
      const tipoConsulta = { id: 123 };
      jest.spyOn(tipoConsultaFormService, 'getTipoConsulta').mockReturnValue(tipoConsulta);
      jest.spyOn(tipoConsultaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoConsulta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoConsulta }));
      saveSubject.complete();

      // THEN
      expect(tipoConsultaFormService.getTipoConsulta).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoConsultaService.update).toHaveBeenCalledWith(expect.objectContaining(tipoConsulta));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoConsulta>>();
      const tipoConsulta = { id: 123 };
      jest.spyOn(tipoConsultaFormService, 'getTipoConsulta').mockReturnValue({ id: null });
      jest.spyOn(tipoConsultaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoConsulta: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoConsulta }));
      saveSubject.complete();

      // THEN
      expect(tipoConsultaFormService.getTipoConsulta).toHaveBeenCalled();
      expect(tipoConsultaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoConsulta>>();
      const tipoConsulta = { id: 123 };
      jest.spyOn(tipoConsultaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoConsulta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoConsultaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
