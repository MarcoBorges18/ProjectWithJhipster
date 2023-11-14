import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPlano, NewPlano } from '../plano.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPlano for edit and NewPlanoFormGroupInput for create.
 */
type PlanoFormGroupInput = IPlano | PartialWithRequiredKeyOf<NewPlano>;

type PlanoFormDefaults = Pick<NewPlano, 'id'>;

type PlanoFormGroupContent = {
  id: FormControl<IPlano['id'] | NewPlano['id']>;
  convenio: FormControl<IPlano['convenio']>;
  desconto: FormControl<IPlano['desconto']>;
};

export type PlanoFormGroup = FormGroup<PlanoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PlanoFormService {
  createPlanoFormGroup(plano: PlanoFormGroupInput = { id: null }): PlanoFormGroup {
    const planoRawValue = {
      ...this.getFormDefaults(),
      ...plano,
    };
    return new FormGroup<PlanoFormGroupContent>({
      id: new FormControl(
        { value: planoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      convenio: new FormControl(planoRawValue.convenio, {
        validators: [Validators.required],
      }),
      desconto: new FormControl(planoRawValue.desconto, {
        validators: [Validators.required],
      }),
    });
  }

  getPlano(form: PlanoFormGroup): IPlano | NewPlano {
    return form.getRawValue() as IPlano | NewPlano;
  }

  resetForm(form: PlanoFormGroup, plano: PlanoFormGroupInput): void {
    const planoRawValue = { ...this.getFormDefaults(), ...plano };
    form.reset(
      {
        ...planoRawValue,
        id: { value: planoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PlanoFormDefaults {
    return {
      id: null,
    };
  }
}
