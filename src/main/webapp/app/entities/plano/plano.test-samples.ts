import { IPlano, NewPlano } from './plano.model';

export const sampleWithRequiredData: IPlano = {
  id: 10451,
  convenio: 'supposing pfft',
  desconto: 1236.07,
};

export const sampleWithPartialData: IPlano = {
  id: 727,
  convenio: 'this before composed',
  desconto: 25777.95,
};

export const sampleWithFullData: IPlano = {
  id: 9467,
  convenio: 'although joshingly',
  desconto: 6627,
};

export const sampleWithNewData: NewPlano = {
  convenio: 'annually ouch drum',
  desconto: 23224.86,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
