import { IEndereco, NewEndereco } from './endereco.model';

export const sampleWithRequiredData: IEndereco = {
  id: 20388,
  logradouro: 'arrogantly hence scrummage',
  bairro: 'properly',
  cep: 'huzzah',
  numero: 'yowza',
  complemento: 'yuck retool',
  cidade: 'dimly',
  uf: 'burning looks',
};

export const sampleWithPartialData: IEndereco = {
  id: 10961,
  logradouro: 'incidentally',
  bairro: 'sound menopause panel',
  cep: 'wherever circa highly',
  numero: 'until incidentally',
  complemento: 'utterly compulsion',
  cidade: 'finally',
  uf: 'eek um',
};

export const sampleWithFullData: IEndereco = {
  id: 19031,
  logradouro: 'whether remortgage wildly',
  bairro: 'till outhouse',
  cep: 'moral',
  numero: 'why',
  complemento: 'duh',
  cidade: 'really gram',
  uf: 'shoe-horn guide',
};

export const sampleWithNewData: NewEndereco = {
  logradouro: 'jealously',
  bairro: 'given amongst after',
  cep: 'although drat retaliate',
  numero: 'quarrelsomely woot',
  complemento: 'calculating uh-huh silently',
  cidade: 'even thankfully',
  uf: 'wildly zowie',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
