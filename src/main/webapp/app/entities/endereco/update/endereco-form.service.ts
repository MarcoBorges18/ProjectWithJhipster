import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEndereco, NewEndereco } from '../endereco.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEndereco for edit and NewEnderecoFormGroupInput for create.
 */
type EnderecoFormGroupInput = IEndereco | PartialWithRequiredKeyOf<NewEndereco>;

type EnderecoFormDefaults = Pick<NewEndereco, 'id'>;

type EnderecoFormGroupContent = {
  id: FormControl<IEndereco['id'] | NewEndereco['id']>;
  logradouro: FormControl<IEndereco['logradouro']>;
  bairro: FormControl<IEndereco['bairro']>;
  cep: FormControl<IEndereco['cep']>;
  numero: FormControl<IEndereco['numero']>;
  complemento: FormControl<IEndereco['complemento']>;
  cidade: FormControl<IEndereco['cidade']>;
  uf: FormControl<IEndereco['uf']>;
};

export type EnderecoFormGroup = FormGroup<EnderecoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EnderecoFormService {
  createEnderecoFormGroup(endereco: EnderecoFormGroupInput = { id: null }): EnderecoFormGroup {
    const enderecoRawValue = {
      ...this.getFormDefaults(),
      ...endereco,
    };
    return new FormGroup<EnderecoFormGroupContent>({
      id: new FormControl(
        { value: enderecoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      logradouro: new FormControl(enderecoRawValue.logradouro, {
        validators: [Validators.required],
      }),
      bairro: new FormControl(enderecoRawValue.bairro, {
        validators: [Validators.required],
      }),
      cep: new FormControl(enderecoRawValue.cep, {
        validators: [Validators.required],
      }),
      numero: new FormControl(enderecoRawValue.numero, {
        validators: [Validators.required],
      }),
      complemento: new FormControl(enderecoRawValue.complemento, {
        validators: [Validators.required],
      }),
      cidade: new FormControl(enderecoRawValue.cidade, {
        validators: [Validators.required],
      }),
      uf: new FormControl(enderecoRawValue.uf, {
        validators: [Validators.required],
      }),
    });
  }

  getEndereco(form: EnderecoFormGroup): IEndereco | NewEndereco {
    return form.getRawValue() as IEndereco | NewEndereco;
  }

  resetForm(form: EnderecoFormGroup, endereco: EnderecoFormGroupInput): void {
    const enderecoRawValue = { ...this.getFormDefaults(), ...endereco };
    form.reset(
      {
        ...enderecoRawValue,
        id: { value: enderecoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EnderecoFormDefaults {
    return {
      id: null,
    };
  }
}
