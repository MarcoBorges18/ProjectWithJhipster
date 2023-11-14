import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPaciente, NewPaciente } from '../paciente.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaciente for edit and NewPacienteFormGroupInput for create.
 */
type PacienteFormGroupInput = IPaciente | PartialWithRequiredKeyOf<NewPaciente>;

type PacienteFormDefaults = Pick<NewPaciente, 'id' | 'ativo'>;

type PacienteFormGroupContent = {
  id: FormControl<IPaciente['id'] | NewPaciente['id']>;
  nome: FormControl<IPaciente['nome']>;
  email: FormControl<IPaciente['email']>;
  cpf: FormControl<IPaciente['cpf']>;
  telefone: FormControl<IPaciente['telefone']>;
  ativo: FormControl<IPaciente['ativo']>;
  plano: FormControl<IPaciente['plano']>;
  endereco: FormControl<IPaciente['endereco']>;
};

export type PacienteFormGroup = FormGroup<PacienteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PacienteFormService {
  createPacienteFormGroup(paciente: PacienteFormGroupInput = { id: null }): PacienteFormGroup {
    const pacienteRawValue = {
      ...this.getFormDefaults(),
      ...paciente,
    };
    return new FormGroup<PacienteFormGroupContent>({
      id: new FormControl(
        { value: pacienteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(pacienteRawValue.nome, {
        validators: [Validators.required],
      }),
      email: new FormControl(pacienteRawValue.email, {
        validators: [Validators.required],
      }),
      cpf: new FormControl(pacienteRawValue.cpf),
      telefone: new FormControl(pacienteRawValue.telefone),
      ativo: new FormControl(pacienteRawValue.ativo, {
        validators: [Validators.required],
      }),
      plano: new FormControl(pacienteRawValue.plano),
      endereco: new FormControl(pacienteRawValue.endereco),
    });
  }

  getPaciente(form: PacienteFormGroup): IPaciente | NewPaciente {
    return form.getRawValue() as IPaciente | NewPaciente;
  }

  resetForm(form: PacienteFormGroup, paciente: PacienteFormGroupInput): void {
    const pacienteRawValue = { ...this.getFormDefaults(), ...paciente };
    form.reset(
      {
        ...pacienteRawValue,
        id: { value: pacienteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PacienteFormDefaults {
    return {
      id: null,
      ativo: false,
    };
  }
}
