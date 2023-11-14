import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tipo-consulta.test-samples';

import { TipoConsultaFormService } from './tipo-consulta-form.service';

describe('TipoConsulta Form Service', () => {
  let service: TipoConsultaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TipoConsultaFormService);
  });

  describe('Service methods', () => {
    describe('createTipoConsultaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTipoConsultaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            tempo: expect.any(Object),
            valorConsulta: expect.any(Object),
          }),
        );
      });

      it('passing ITipoConsulta should create a new form with FormGroup', () => {
        const formGroup = service.createTipoConsultaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            tempo: expect.any(Object),
            valorConsulta: expect.any(Object),
          }),
        );
      });
    });

    describe('getTipoConsulta', () => {
      it('should return NewTipoConsulta for default TipoConsulta initial value', () => {
        const formGroup = service.createTipoConsultaFormGroup(sampleWithNewData);

        const tipoConsulta = service.getTipoConsulta(formGroup) as any;

        expect(tipoConsulta).toMatchObject(sampleWithNewData);
      });

      it('should return NewTipoConsulta for empty TipoConsulta initial value', () => {
        const formGroup = service.createTipoConsultaFormGroup();

        const tipoConsulta = service.getTipoConsulta(formGroup) as any;

        expect(tipoConsulta).toMatchObject({});
      });

      it('should return ITipoConsulta', () => {
        const formGroup = service.createTipoConsultaFormGroup(sampleWithRequiredData);

        const tipoConsulta = service.getTipoConsulta(formGroup) as any;

        expect(tipoConsulta).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITipoConsulta should not enable id FormControl', () => {
        const formGroup = service.createTipoConsultaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTipoConsulta should disable id FormControl', () => {
        const formGroup = service.createTipoConsultaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
