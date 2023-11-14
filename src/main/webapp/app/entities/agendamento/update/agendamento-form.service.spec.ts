import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../agendamento.test-samples';

import { AgendamentoFormService } from './agendamento-form.service';

describe('Agendamento Form Service', () => {
  let service: AgendamentoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AgendamentoFormService);
  });

  describe('Service methods', () => {
    describe('createAgendamentoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAgendamentoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            valorFinal: expect.any(Object),
            medico: expect.any(Object),
            paciente: expect.any(Object),
            tipo: expect.any(Object),
          }),
        );
      });

      it('passing IAgendamento should create a new form with FormGroup', () => {
        const formGroup = service.createAgendamentoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            valorFinal: expect.any(Object),
            medico: expect.any(Object),
            paciente: expect.any(Object),
            tipo: expect.any(Object),
          }),
        );
      });
    });

    describe('getAgendamento', () => {
      it('should return NewAgendamento for default Agendamento initial value', () => {
        const formGroup = service.createAgendamentoFormGroup(sampleWithNewData);

        const agendamento = service.getAgendamento(formGroup) as any;

        expect(agendamento).toMatchObject(sampleWithNewData);
      });

      it('should return NewAgendamento for empty Agendamento initial value', () => {
        const formGroup = service.createAgendamentoFormGroup();

        const agendamento = service.getAgendamento(formGroup) as any;

        expect(agendamento).toMatchObject({});
      });

      it('should return IAgendamento', () => {
        const formGroup = service.createAgendamentoFormGroup(sampleWithRequiredData);

        const agendamento = service.getAgendamento(formGroup) as any;

        expect(agendamento).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAgendamento should not enable id FormControl', () => {
        const formGroup = service.createAgendamentoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAgendamento should disable id FormControl', () => {
        const formGroup = service.createAgendamentoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
