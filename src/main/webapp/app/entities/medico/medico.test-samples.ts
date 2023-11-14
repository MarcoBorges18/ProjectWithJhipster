import { IMedico, NewMedico } from './medico.model';

export const sampleWithRequiredData: IMedico = {
  id: 296,
  nome: 'midnight binoculars phew',
  email: 'Julio_Xavier@live.com',
  crm: 'coaxingly next id',
  ativo: true,
};

export const sampleWithPartialData: IMedico = {
  id: 22225,
  nome: 'crooked',
  email: 'Antonio_Moraes0@gmail.com',
  crm: 'incidentally pawn yahoo',
  ativo: false,
};

export const sampleWithFullData: IMedico = {
  id: 23358,
  nome: 'until spanish amidst',
  email: 'Nubia.Albuquerque34@bol.com.br',
  crm: 'construction thoughtful',
  ativo: true,
};

export const sampleWithNewData: NewMedico = {
  nome: 'er',
  email: 'Alicia.Batista76@bol.com.br',
  crm: 'aside uncomfortable',
  ativo: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
