import { IPaciente, NewPaciente } from './paciente.model';

export const sampleWithRequiredData: IPaciente = {
  id: 6886,
  nome: 'jolly rudely',
  email: 'Felix30@bol.com.br',
  ativo: true,
};

export const sampleWithPartialData: IPaciente = {
  id: 26272,
  nome: 'fully hourly',
  email: 'Washington_Batista@bol.com.br',
  ativo: true,
};

export const sampleWithFullData: IPaciente = {
  id: 28034,
  nome: 'last indeed',
  email: 'Miguel.Braga23@hotmail.com',
  cpf: 'mmm med jeopardize',
  telefone: 'lest threat skeleton',
  ativo: true,
};

export const sampleWithNewData: NewPaciente = {
  nome: 'phooey gee',
  email: 'Maite_Souza@hotmail.com',
  ativo: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
