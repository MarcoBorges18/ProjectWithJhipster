import { IAgendamento, NewAgendamento } from './agendamento.model';

export const sampleWithRequiredData: IAgendamento = {
  id: 29427,
  valorFinal: 11272.27,
};

export const sampleWithPartialData: IAgendamento = {
  id: 6024,
  valorFinal: 16775.6,
};

export const sampleWithFullData: IAgendamento = {
  id: 24279,
  valorFinal: 19809.77,
};

export const sampleWithNewData: NewAgendamento = {
  valorFinal: 28694.7,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
