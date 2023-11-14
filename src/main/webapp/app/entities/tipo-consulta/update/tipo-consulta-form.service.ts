import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITipoConsulta, NewTipoConsulta } from '../tipo-consulta.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITipoConsulta for edit and NewTipoConsultaFormGroupInput for create.
 */
type TipoConsultaFormGroupInput = ITipoConsulta | PartialWithRequiredKeyOf<NewTipoConsulta>;

type TipoConsultaFormDefaults = Pick<NewTipoConsulta, 'id'>;

type TipoConsultaFormGroupContent = {
  id: FormControl<ITipoConsulta['id'] | NewTipoConsulta['id']>;
  nome: FormControl<ITipoConsulta['nome']>;
  tempo: FormControl<ITipoConsulta['tempo']>;
  valorConsulta: FormControl<ITipoConsulta['valorConsulta']>;
};

export type TipoConsultaFormGroup = FormGroup<TipoConsultaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TipoConsultaFormService {
  createTipoConsultaFormGroup(tipoConsulta: TipoConsultaFormGroupInput = { id: null }): TipoConsultaFormGroup {
    const tipoConsultaRawValue = {
      ...this.getFormDefaults(),
      ...tipoConsulta,
    };
    return new FormGroup<TipoConsultaFormGroupContent>({
      id: new FormControl(
        { value: tipoConsultaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(tipoConsultaRawValue.nome, {
        validators: [Validators.required],
      }),
      tempo: new FormControl(tipoConsultaRawValue.tempo, {
        validators: [Validators.required],
      }),
      valorConsulta: new FormControl(tipoConsultaRawValue.valorConsulta, {
        validators: [Validators.required],
      }),
    });
  }

  getTipoConsulta(form: TipoConsultaFormGroup): ITipoConsulta | NewTipoConsulta {
    return form.getRawValue() as ITipoConsulta | NewTipoConsulta;
  }

  resetForm(form: TipoConsultaFormGroup, tipoConsulta: TipoConsultaFormGroupInput): void {
    const tipoConsultaRawValue = { ...this.getFormDefaults(), ...tipoConsulta };
    form.reset(
      {
        ...tipoConsultaRawValue,
        id: { value: tipoConsultaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TipoConsultaFormDefaults {
    return {
      id: null,
    };
  }
}
