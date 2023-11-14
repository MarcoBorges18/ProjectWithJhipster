import dayjs from 'dayjs/esm';

import { ITipoConsulta, NewTipoConsulta } from './tipo-consulta.model';

export const sampleWithRequiredData: ITipoConsulta = {
  id: 24160,
  nome: 'assign waken scarper',
  tempo: dayjs('2023-11-13'),
  valorConsulta: 7158.77,
};

export const sampleWithPartialData: ITipoConsulta = {
  id: 15524,
  nome: 'fooey',
  tempo: dayjs('2023-11-13'),
  valorConsulta: 13249.79,
};

export const sampleWithFullData: ITipoConsulta = {
  id: 17236,
  nome: 'stud amidst',
  tempo: dayjs('2023-11-13'),
  valorConsulta: 8175.13,
};

export const sampleWithNewData: NewTipoConsulta = {
  nome: 'checkbook silent knotty',
  tempo: dayjs('2023-11-12'),
  valorConsulta: 25344.44,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
