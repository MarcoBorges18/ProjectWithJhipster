import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAgendamento, NewAgendamento } from '../agendamento.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAgendamento for edit and NewAgendamentoFormGroupInput for create.
 */
type AgendamentoFormGroupInput = IAgendamento | PartialWithRequiredKeyOf<NewAgendamento>;

type AgendamentoFormDefaults = Pick<NewAgendamento, 'id'>;

type AgendamentoFormGroupContent = {
  id: FormControl<IAgendamento['id'] | NewAgendamento['id']>;
  valorFinal: FormControl<IAgendamento['valorFinal']>;
  medico: FormControl<IAgendamento['medico']>;
  paciente: FormControl<IAgendamento['paciente']>;
  tipo: FormControl<IAgendamento['tipo']>;
};

export type AgendamentoFormGroup = FormGroup<AgendamentoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AgendamentoFormService {
  createAgendamentoFormGroup(agendamento: AgendamentoFormGroupInput = { id: null }): AgendamentoFormGroup {
    const agendamentoRawValue = {
      ...this.getFormDefaults(),
      ...agendamento,
    };
    return new FormGroup<AgendamentoFormGroupContent>({
      id: new FormControl(
        { value: agendamentoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      valorFinal: new FormControl(agendamentoRawValue.valorFinal, {
        validators: [Validators.required],
      }),
      medico: new FormControl(agendamentoRawValue.medico),
      paciente: new FormControl(agendamentoRawValue.paciente),
      tipo: new FormControl(agendamentoRawValue.tipo),
    });
  }

  getAgendamento(form: AgendamentoFormGroup): IAgendamento | NewAgendamento {
    return form.getRawValue() as IAgendamento | NewAgendamento;
  }

  resetForm(form: AgendamentoFormGroup, agendamento: AgendamentoFormGroupInput): void {
    const agendamentoRawValue = { ...this.getFormDefaults(), ...agendamento };
    form.reset(
      {
        ...agendamentoRawValue,
        id: { value: agendamentoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AgendamentoFormDefaults {
    return {
      id: null,
    };
  }
}
