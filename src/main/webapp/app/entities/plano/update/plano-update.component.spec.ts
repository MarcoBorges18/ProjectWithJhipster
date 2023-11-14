import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PlanoService } from '../service/plano.service';
import { IPlano } from '../plano.model';
import { PlanoFormService } from './plano-form.service';

import { PlanoUpdateComponent } from './plano-update.component';

describe('Plano Management Update Component', () => {
  let comp: PlanoUpdateComponent;
  let fixture: ComponentFixture<PlanoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let planoFormService: PlanoFormService;
  let planoService: PlanoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PlanoUpdateComponent],
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
      .overrideTemplate(PlanoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlanoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    planoFormService = TestBed.inject(PlanoFormService);
    planoService = TestBed.inject(PlanoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const plano: IPlano = { id: 456 };

      activatedRoute.data = of({ plano });
      comp.ngOnInit();

      expect(comp.plano).toEqual(plano);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlano>>();
      const plano = { id: 123 };
      jest.spyOn(planoFormService, 'getPlano').mockReturnValue(plano);
      jest.spyOn(planoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ plano });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: plano }));
      saveSubject.complete();

      // THEN
      expect(planoFormService.getPlano).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(planoService.update).toHaveBeenCalledWith(expect.objectContaining(plano));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlano>>();
      const plano = { id: 123 };
      jest.spyOn(planoFormService, 'getPlano').mockReturnValue({ id: null });
      jest.spyOn(planoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ plano: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: plano }));
      saveSubject.complete();

      // THEN
      expect(planoFormService.getPlano).toHaveBeenCalled();
      expect(planoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlano>>();
      const plano = { id: 123 };
      jest.spyOn(planoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ plano });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(planoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
