import { IPlano } from 'app/entities/plano/plano.model';
import { IEndereco } from 'app/entities/endereco/endereco.model';

export interface IPaciente {
  id: number;
  nome?: string | null;
  email?: string | null;
  cpf?: string | null;
  telefone?: string | null;
  ativo?: boolean | null;
  plano?: Pick<IPlano, 'id'> | null;
  endereco?: Pick<IEndereco, 'id'> | null;
}

export type NewPaciente = Omit<IPaciente, 'id'> & { id: null };
