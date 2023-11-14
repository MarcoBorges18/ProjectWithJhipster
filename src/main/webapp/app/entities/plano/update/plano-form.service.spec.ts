import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../plano.test-samples';

import { PlanoFormService } from './plano-form.service';

describe('Plano Form Service', () => {
  let service: PlanoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlanoFormService);
  });

  describe('Service methods', () => {
    describe('createPlanoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPlanoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            convenio: expect.any(Object),
            desconto: expect.any(Object),
          }),
        );
      });

      it('passing IPlano should create a new form with FormGroup', () => {
        const formGroup = service.createPlanoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            convenio: expect.any(Object),
            desconto: expect.any(Object),
          }),
        );
      });
    });

    describe('getPlano', () => {
      it('should return NewPlano for default Plano initial value', () => {
        const formGroup = service.createPlanoFormGroup(sampleWithNewData);

        const plano = service.getPlano(formGroup) as any;

        expect(plano).toMatchObject(sampleWithNewData);
      });

      it('should return NewPlano for empty Plano initial value', () => {
        const formGroup = service.createPlanoFormGroup();

        const plano = service.getPlano(formGroup) as any;

        expect(plano).toMatchObject({});
      });

      it('should return IPlano', () => {
        const formGroup = service.createPlanoFormGroup(sampleWithRequiredData);

        const plano = service.getPlano(formGroup) as any;

        expect(plano).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPlano should not enable id FormControl', () => {
        const formGroup = service.createPlanoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPlano should disable id FormControl', () => {
        const formGroup = service.createPlanoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
