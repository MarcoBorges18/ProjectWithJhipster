import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../especialidade.test-samples';

import { EspecialidadeFormService } from './especialidade-form.service';

describe('Especialidade Form Service', () => {
  let service: EspecialidadeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EspecialidadeFormService);
  });

  describe('Service methods', () => {
    describe('createEspecialidadeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEspecialidadeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
          }),
        );
      });

      it('passing IEspecialidade should create a new form with FormGroup', () => {
        const formGroup = service.createEspecialidadeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
          }),
        );
      });
    });

    describe('getEspecialidade', () => {
      it('should return NewEspecialidade for default Especialidade initial value', () => {
        const formGroup = service.createEspecialidadeFormGroup(sampleWithNewData);

        const especialidade = service.getEspecialidade(formGroup) as any;

        expect(especialidade).toMatchObject(sampleWithNewData);
      });

      it('should return NewEspecialidade for empty Especialidade initial value', () => {
        const formGroup = service.createEspecialidadeFormGroup();

        const especialidade = service.getEspecialidade(formGroup) as any;

        expect(especialidade).toMatchObject({});
      });

      it('should return IEspecialidade', () => {
        const formGroup = service.createEspecialidadeFormGroup(sampleWithRequiredData);

        const especialidade = service.getEspecialidade(formGroup) as any;

        expect(especialidade).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEspecialidade should not enable id FormControl', () => {
        const formGroup = service.createEspecialidadeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEspecialidade should disable id FormControl', () => {
        const formGroup = service.createEspecialidadeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
