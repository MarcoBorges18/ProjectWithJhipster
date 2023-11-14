import { IEspecialidade, NewEspecialidade } from './especialidade.model';

export const sampleWithRequiredData: IEspecialidade = {
  id: 16185,
  nome: 'betray woot',
};

export const sampleWithPartialData: IEspecialidade = {
  id: 18067,
  nome: 'what',
};

export const sampleWithFullData: IEspecialidade = {
  id: 19142,
  nome: 'pillar delightful',
};

export const sampleWithNewData: NewEspecialidade = {
  nome: 'mortally',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
