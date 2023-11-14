import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMedico, NewMedico } from '../medico.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMedico for edit and NewMedicoFormGroupInput for create.
 */
type MedicoFormGroupInput = IMedico | PartialWithRequiredKeyOf<NewMedico>;

type MedicoFormDefaults = Pick<NewMedico, 'id' | 'ativo'>;

type MedicoFormGroupContent = {
  id: FormControl<IMedico['id'] | NewMedico['id']>;
  nome: FormControl<IMedico['nome']>;
  email: FormControl<IMedico['email']>;
  crm: FormControl<IMedico['crm']>;
  ativo: FormControl<IMedico['ativo']>;
  especialidade: FormControl<IMedico['especialidade']>;
  endereco: FormControl<IMedico['endereco']>;
};

export type MedicoFormGroup = FormGroup<MedicoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MedicoFormService {
  createMedicoFormGroup(medico: MedicoFormGroupInput = { id: null }): MedicoFormGroup {
    const medicoRawValue = {
      ...this.getFormDefaults(),
      ...medico,
    };
    return new FormGroup<MedicoFormGroupContent>({
      id: new FormControl(
        { value: medicoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(medicoRawValue.nome, {
        validators: [Validators.required],
      }),
      email: new FormControl(medicoRawValue.email, {
        validators: [Validators.required],
      }),
      crm: new FormControl(medicoRawValue.crm, {
        validators: [Validators.required],
      }),
      ativo: new FormControl(medicoRawValue.ativo, {
        validators: [Validators.required],
      }),
      especialidade: new FormControl(medicoRawValue.especialidade),
      endereco: new FormControl(medicoRawValue.endereco),
    });
  }

  getMedico(form: MedicoFormGroup): IMedico | NewMedico {
    return form.getRawValue() as IMedico | NewMedico;
  }

  resetForm(form: MedicoFormGroup, medico: MedicoFormGroupInput): void {
    const medicoRawValue = { ...this.getFormDefaults(), ...medico };
    form.reset(
      {
        ...medicoRawValue,
        id: { value: medicoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MedicoFormDefaults {
    return {
      id: null,
      ativo: false,
    };
  }
}
